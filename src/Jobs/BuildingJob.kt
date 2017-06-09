package Jobs

import BuildOrder.BuildOrderExec
import BuildOrder.BuildUnit
import BwapiWrappers.UnitInfo
import LifeCycle.AI
import Schedule.GameEvents
import Schedule.Until
import bwapi.TilePosition
import bwapi.UnitType


object BuildingJob : Job("building", 2) {

    override fun start(unitInfo: UnitInfo) {
        val myLabel = label(unitInfo)
        val me = unitInfo.id.toString()
        val unitMorphs = GameEvents.unitMorph
        val unitCompletes = GameEvents.unitComplete

        Schedule.run(myLabel) {
            println(me + " build")
            val build = BuildOrderExec.buildQ.pop() as BuildUnit
            AI.reserved.add(build.cost())
            var buildLoc = getLocationAndCommandBuild(unitInfo, build)

            // get the worker back on task if they get distracted
            GameEvents.frame24.subscribe(myLabel,
                    condition={unitInfo.base.isGatheringMinerals || unitInfo.base.isGatheringGas || unitInfo.base.isIdle},
                    invoke={
                        assert(unitInfo.job == BuildingJob)
                        buildLoc = getLocationAndCommandBuild(unitInfo, build)
                    })

            //small differences depending on worker race
            when (unitInfo.base.type) {
                UnitType.Zerg_Drone -> {
                    // wait until something morphs at that location and type
                    yield( Until( unitMorphs, { it.base.tilePosition == buildLoc && it.base.type == build.unitType }))
                    // unreserve the cost
                    AI.reserved.subtract(build.cost())
                }
                UnitType.Protoss_Probe -> {
                    var startEvent = GameEvents.unitCreate
                    if (build.unitType.isRefinery) startEvent = unitMorphs
                    yield( Until( startEvent, { it.base.tilePosition == buildLoc && it.base.type == build.unitType }))
                    println("building started "+ unitInfo.id)
                    unitInfo.job = MiningJob
                    AI.reserved.subtract(build.cost())
                }
                UnitType.Terran_SCV -> {
                    // terran can't release the worker until the building completes
                    var targetID = -1
                    var startEvent = GameEvents.unitCreate
                    if (build.unitType.isRefinery) startEvent = unitMorphs
                    yield( Until( startEvent, {
                        targetID = it.id
                        it.base.buildUnit != null && it.base.buildUnit.id == unitInfo.id
                    }))
                    println(targetID.toString() + "is being built")
                    AI.reserved.subtract(build.cost())
                    yield( Until( unitCompletes, { it.id == targetID }))
                    println("finished building " + targetID.toString())
                    unitInfo.job = MiningJob
                }
            }
        }
    }

    fun getLocationAndCommandBuild(unitInfo: UnitInfo, build:BuildUnit): TilePosition {
        val loc = AI.game.getBuildLocation(build.unitType, AI.game.self().startLocation)
        unitInfo.base.build(build.unitType, loc)
        return loc
    }

}