package Tracking

import Jobs.IdleJob
import Schedule.Event
import Schedule.GameEvents

fun removeDeadUnits() {
    GameEvents.unitDestroy.subscribeWithArg("removeDead",
            invoke = {
                println(it.id.toString() + " died")
                it.job = IdleJob
                UnitTracker.knownUnits.remove(it.id)
            },
            priority=Event.CLEANUP_DATA)
}
