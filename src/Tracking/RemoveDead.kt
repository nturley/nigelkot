package Tracking

import LifeCycle.With
import Schedule.Event

fun removeDeadUnits() {
    With.gameEvents.unitDestroy.subscribeWithArg("removeDead",
            invoke = {
                println(it.id.toString() + " died")
                With.unitTracker.knownUnits.remove(it.id)
            },
            priority=Event.CLEANUP_DATA)
}
