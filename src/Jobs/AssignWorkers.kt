package Jobs

import BwapiWrappers.UnitInfo
import LifeCycle.AI
import Schedule.GameEvents
import Tracking.UnitTracker

fun assignWorkers() {
    println("assign workers")

    // every time a worker is built, make him a miner
    GameEvents.unitComplete.subscribeWithArg(
            label="assign workers",
            condition = {
                it.base.type.isWorker &&
                it.base.player.id == AI.myId
            },
            invoke = { worker : UnitInfo ->
                worker.job = MiningJob
            }
    )

    // everytime a refinery is built, assign three miners to harvest from it
    GameEvents.unitComplete.subscribeWithArg(
            label="assign workers",
            condition = {
                it.base.type.isRefinery && it.base.player.id == AI.myId
            },
            invoke = { refinery : UnitInfo ->
                UnitTracker.myMiners.slice(0..2).forEach {
                    it.targetUnit = refinery
                    it.job = HarvestGasJob
                }

            }
    )
}
