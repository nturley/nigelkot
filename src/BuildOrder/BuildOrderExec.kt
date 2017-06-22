package BuildOrder

import LifeCycle.AI
import Schedule.GameEvents
import Tracking.UnitTracker
import java.util.*

object BuildOrderExec {
    val toBuildQ = ArrayDeque<Buildable>()
    val inProgress = mutableListOf<BuildUnit>()
    var supplyUnit :BuildUnit? = null
    fun init() {
        startingBuildOrder()
        GameEvents.unitComplete.subscribeWithArg("toBuildQ exec",
                condition = {it.base.player.id == AI.myId},
                invoke = {
                    if (AI.reserved.supply > 0) AI.reserved.supply -= it.base.type.supplyProvided()
                })
        GameEvents.frame10.subscribe(
                label="toBuildQ exec",
                condition={ toBuildQ.isNotEmpty()},
                invoke = invoke@ {
                    var toBuild = toBuildQ.peek()
                    // This checks if we have (or are building) enough supply for the next two units in our queue
                    // this might be the best I can do without knowing how close we are to be done
                    // TODO: Resource Depots make supply planning more complicated
                    // --------------------------------------------------------------------------
                    // BETTER CRITERIA:
                    // calculate the estimated start time of the next two units if I do NOT insert a supply unit
                    // calculate the estimated start time of the next two units if I do insert a supply unit
                    // insert a supply unit if it improves the start time of the next two units
                    var supplyCost = toBuild.cost().supply
                    // if there is an element at 1, also add its supply cost
                    toBuildQ.elementAtOrNull(1)?.let{ supplyCost += it.cost().supply}

                    if (supplyUnit != null && supplyCost > AI.game.self().supplyTotal()-AI.game.self().supplyUsed() + AI.reserved.supply) {
                        toBuild = supplyUnit
                        toBuildQ.addFirst(supplyUnit)
                    }
                    if (!toBuild.canAfford()) return@invoke
                    if (toBuild.whatBuilds().isWorker) {
                        // find a suitable worker
                        if (UnitTracker.myMiners.isNotEmpty()) {
                            UnitTracker.myMiners.first().job = BuildingJob
                            AI.reserved.supply += toBuild.supplyProvided()
                        }
                    } else {
                        if (UnitTracker.myUnits.any { it.canBuild(toBuild) }) {
                            val builder = UnitTracker.myUnits.first { it.canBuild(toBuild) }
                            builder.build(toBuildQ.pop())
                            AI.reserved.supply += toBuild.supplyProvided()
                        }
                    }
                })
    }
}