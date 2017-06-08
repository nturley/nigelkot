package BuildOrder

import Jobs.BuildingJob
import LifeCycle.With
import bwapi.Race
import bwapi.UnitType
import java.util.*

class BuildOrderExec {

    val buildQ = ArrayDeque<Buildable>()
    init {
        when (With.game.self().race) {
            Race.Terran -> {
                val worker = BuildUnit(UnitType.Terran_SCV)
                buildQ.add(worker)
                buildQ.add(BuildUnit(UnitType.Terran_Refinery))
                buildQ.add(BuildUnit(UnitType.Terran_Supply_Depot))
            }
            Race.Protoss -> {
                val worker = BuildUnit(UnitType.Protoss_Probe)
                buildQ.add(worker)
                buildQ.add(BuildUnit(UnitType.Protoss_Assimilator))
                buildQ.add(BuildUnit(UnitType.Protoss_Pylon))
            }
            Race.Zerg -> {
                val worker = BuildUnit(UnitType.Zerg_Drone)
                buildQ.add(worker)
                buildQ.add(BuildUnit(UnitType.Zerg_Extractor))
                buildQ.add(BuildUnit(UnitType.Zerg_Spawning_Pool))
            }
        }
        With.gameEvents.frame10.subscribe(
                label="buildQ exec",
                condition={buildQ.isNotEmpty()},
                invoke = invoke@ {
                    val toBuild = buildQ.peek()
                    if (!toBuild.canAfford()) return@invoke
                    if (toBuild.whatBuilds().isWorker) {
                        // find a suitable worker
                        With.unitTracker.myMiners.first().job = BuildingJob
                    } else {
                        With.unitTracker.myUnits.first {it.canBuild(toBuild)}.build(buildQ.pop())
                    }
                })
    }
}