package Jobs

import BwapiWrappers.UnitInfo
import LifeCycle.With

abstract class Job (val label:String, val priority:Int) {
    open fun label(unitInfo: UnitInfo):String {
        return unitInfo.id.toString() + " job"
    }
    open fun start(unitInfo: UnitInfo) {}
    open fun stop(unitInfo:UnitInfo) {
        With.gameEvents.events.forEach { it.unsubscribe(label(unitInfo)) }
        With.gameEvents.frame10.unsubscribe(label(unitInfo))
        println(unitInfo.id.toString() + " stop " + label)
    }
}

object IdleJob : Job("idle", 0)