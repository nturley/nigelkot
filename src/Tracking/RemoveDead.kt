package Tracking

import LifeCycle.With

fun removeDeadUnits() {
    With.gameEvents.unitDestroy.subscribeWithArg("removeDead",
            invoke = {
                println(it.id.toString() + " died")
                With.unitTracker.knownUnits.remove(it.id)
            },
            priority=-100)
}
