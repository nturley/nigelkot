package Jobs

import BwapiWrappers.UnitInfo
import Schedule.GameEvents

abstract class Job (val label:String, val priority:Int) {
    open fun label(unitInfo: UnitInfo):String {
        return unitInfo.id.toString() + " job"
    }
    open fun start(unitInfo: UnitInfo) {}
    open fun stop(unitInfo:UnitInfo) {
        GameEvents.events.forEach { it.unsubscribe(label(unitInfo)) }
        GameEvents.frame10.unsubscribe(label(unitInfo))
        GameEvents.frame24.unsubscribe(label(unitInfo))
        println(unitInfo.id.toString() + " stop " + label)
    }
}

object IdleJob : Job("idle", 0)