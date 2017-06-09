package BuildOrder

import Jobs.BuildingJob
import LifeCycle.AI
import Schedule.GameEvents
import Tracking.UnitTracker
import bwapi.Race
import bwapi.UnitType
import java.util.*

object BuildOrderExec {
    val buildQ = ArrayDeque<Buildable>()
    fun init() {
        buildQ.clear()
        when (AI.game.self().race) {
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
        GameEvents.frame10.subscribe(
                label="buildQ exec",
                condition={buildQ.isNotEmpty()},
                invoke = invoke@ {
                    val toBuild = buildQ.peek()
                    if (!toBuild.canAfford()) return@invoke
                    if (toBuild.whatBuilds().isWorker) {
                        // find a suitable worker
                        UnitTracker.myMiners.first().job = BuildingJob
                    } else {
                        UnitTracker.myUnits.first {it.canBuild(toBuild)}.build(buildQ.pop())
                    }
                })
    }
}