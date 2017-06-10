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
    var supplyUnit :BuildUnit? = null
    fun init() {
        startingBuildOrder()
        GameEvents.unitComplete.subscribeWithArg("buildQ exec",
                condition = {it.base.player.id == AI.myId},
                invoke = {
                    if (AI.reserved.supply > 0) AI.reserved.supply -= it.base.type.supplyProvided()
                })
        GameEvents.frame10.subscribe(
                label="buildQ exec",
                condition={buildQ.isNotEmpty()},
                invoke = invoke@ {
                    var toBuild = buildQ.peek()
                    var supplyCost = toBuild.cost().supply
                    // if there is an element at 1, also add its supply cost
                    buildQ.elementAtOrNull(1)?.let{ supplyCost += it.cost().supply}

                    // if I'm going to be supply blocked, insert a supply unit to my buildQ
                    if (supplyUnit != null && supplyCost > AI.game.self().supplyTotal()-AI.game.self().supplyUsed() + AI.reserved.supply) {
                        toBuild = supplyUnit
                        buildQ.addFirst(supplyUnit)
                    }
                    if (!toBuild.canAfford()) return@invoke
                    if (toBuild.whatBuilds().isWorker) {
                        // find a suitable worker
                        UnitTracker.myMiners.first().job = BuildingJob
                        AI.reserved.supply += toBuild.supplyProvided()
                    } else {
                        if (UnitTracker.myUnits.any { it.canBuild(toBuild) })
                        UnitTracker.myUnits.first {it.canBuild(toBuild)}.build(buildQ.pop())
                        AI.reserved.supply += toBuild.supplyProvided()
                    }
                })
    }
}