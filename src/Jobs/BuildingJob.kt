package Jobs

import BuildOrder.BuildUnit
import BwapiWrappers.UnitInfo
import LifeCycle.With
import Schedule.Until
import bwapi.TilePosition
import bwapi.UnitType


object BuildingJob : Job("building", 2) {

    override fun start(unitInfo: UnitInfo) {
        val myLabel = label(unitInfo)
        val me = unitInfo.id.toString()
        val unitMorphs = With.gameEvents.unitMorph
        val unitCompletes = With.gameEvents.unitComplete

        Schedule.run(myLabel, {
            println(me + " build")
            val build = With.buildOrderExec.buildQ.pop() as BuildUnit
            With.reserved.add(build.cost())
            var buildLoc = getLocationAndCommandBuild(unitInfo, build)

            // get the worker back on task if they get distracted
            With.gameEvents.frame24.subscribe(myLabel,
                    condition={unitInfo.base.isGatheringMinerals || unitInfo.base.isGatheringGas || unitInfo.base.isIdle},
                    invoke={ buildLoc = getLocationAndCommandBuild(unitInfo, build)})

            //small differences depending on worker race
            when (unitInfo.base.type) {
                UnitType.Zerg_Drone -> {
                    // wait until something morphs at that location and type
                    yield( Until( unitMorphs, { it.base.tilePosition == buildLoc && it.base.type == build.unitType }))
                    // stop the job
                    unitInfo.job = IdleJob
                    // unreserve the cost
                    With.reserved.subtract(build.cost())
                }
                UnitType.Protoss_Probe -> {
                    var startEvent = unitCompletes
                    if (build.unitType.isRefinery) startEvent = unitMorphs
                    yield( Until( startEvent, { it.base.tilePosition == buildLoc && it.base.type == build.unitType }))
                    unitInfo.job = MiningJob
                    With.reserved.subtract(build.cost())
                }
                UnitType.Terran_SCV -> {
                    // terran can't release the worker until the building completes
                    var targetID = -1
                    var startEvent = With.gameEvents.unitCreate
                    if (build.unitType.isRefinery) startEvent = unitMorphs
                    yield( Until( startEvent, {
                        targetID = it.id
                        it.base.buildUnit.id == unitInfo.id
                    }))
                    println(targetID.toString() + "is being built")
                    With.reserved.subtract(build.cost())
                    yield( Until( unitCompletes, { it.id == targetID }))
                    println("finished building " + targetID.toString())
                    unitInfo.job = MiningJob
                }
            }
        })
    }

    fun getLocationAndCommandBuild(unitInfo: UnitInfo, build:BuildUnit): TilePosition {
        val loc = With.game.getBuildLocation(build.unitType, With.game.self().startLocation)
        unitInfo.base.build(build.unitType, loc)
        return loc
    }

}