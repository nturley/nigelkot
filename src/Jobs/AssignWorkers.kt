package Jobs

import BwapiWrappers.UnitInfo
import LifeCycle.AI
import Schedule.GameEvents

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
}
