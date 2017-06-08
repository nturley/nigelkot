package Jobs

import BuildOrder.BuildUnit
import BwapiWrappers.UnitInfo
import LifeCycle.With
import Schedule.Until
import bwapi.TilePosition
import bwapi.UnitType


object BuildingJob : Job("building", 2) {
    val buildTargets = mutableMapOf<UnitInfo, BuildUnit>()

    override fun start(unitInfo: UnitInfo) {
        Schedule.run(label(unitInfo), {
            println(unitInfo.id.toString()+" build")
            val build = With.buildOrderExec.buildQ.pop() as BuildUnit
            With.reserved.add(build.cost())
            buildTargets[unitInfo] = build
            var buildLoc = getLocationAndCommandBuild(unitInfo)

            // get the worker back on task if they get distracted
            With.gameEvents.frame24.subscribe(label(unitInfo),
                    condition={unitInfo.base.isGatheringMinerals || unitInfo.base.isGatheringGas || unitInfo.base.isIdle},
                    invoke={ buildLoc = getLocationAndCommandBuild(unitInfo)})

            when (unitInfo.base.type) {

                UnitType.Zerg_Drone -> {
                    yield(Until(With.gameEvents.unitMorph, { it.base.tilePosition == buildLoc && it.base.type == build.unitType }))
                    unitInfo.job = IdleJob
                    With.reserved.subtract(build.cost())
                    // TODO: remove this after unit tracker starts removing destroyed units
                    if (build.unitType.isRefinery) {
                        With.unitTracker.knownUnits.remove(unitInfo.id)
                    }
                }
                UnitType.Protoss_Probe -> {
                    var startEvent = With.gameEvents.unitCreate
                    if (build.unitType.isRefinery) startEvent = With.gameEvents.unitMorph
                    yield(Until(startEvent, { it.base.tilePosition == buildLoc && it.base.type == build.unitType }))
                    unitInfo.job = MiningJob
                    With.reserved.subtract(build.cost())
                }
                UnitType.Terran_SCV -> {
                    // terran can't release the worker until the building completes
                    var targetID = -1
                    var startEvent = With.gameEvents.unitCreate
                    if (build.unitType.isRefinery) startEvent = With.gameEvents.unitMorph
                    yield(Until(startEvent, {
                        targetID = it.id
                        it.base.buildUnit.id == unitInfo.id
                    }))
                    println(targetID.toString() + "is being built")
                    With.reserved.subtract(build.cost())
                    yield(Until(With.gameEvents.unitComplete, { it.id == targetID }))
                    println("finished building " + targetID.toString())
                    unitInfo.job = MiningJob
                }
            }
        })
    }

    fun getLocationAndCommandBuild(unitInfo: UnitInfo): TilePosition {
        val build = buildTargets[unitInfo]
        val loc = With.game.getBuildLocation(build?.unitType, With.game.self().startLocation)
        unitInfo.base.build(build?.unitType, loc)
        return loc
    }

}