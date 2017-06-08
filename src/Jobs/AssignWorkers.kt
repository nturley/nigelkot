package Jobs

import BwapiWrappers.UnitInfo
import LifeCycle.With

fun assignWorkers() {
    // every time a worker is built, make him a miner
    With.gameEvents.unitComplete.subscribeWithArg(
            label="assign workers",
            condition = {
                it.base.type.isWorker &&
                it.base.player.id == With.myId
            },
            invoke = { worker : UnitInfo ->
                worker.job = MiningJob
            }
    )
}
