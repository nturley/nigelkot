package Tracking

import BwapiWrappers.UnitInfo
import Jobs.IdleJob
import Jobs.MiningJob
import LifeCycle.AI
import Schedule.Event
import Schedule.GameEvents


object UnitTracker {
    val knownUnits : MutableMap<Int, UnitInfo> = mutableMapOf()

    fun init() {
        knownUnits.clear()
        GameEvents._unitEvents.forEach { pair: Pair <Event <bwapi.Unit?>, Event <UnitInfo> > ->
            pair.first.subscribeWithArg("unit wrapper",invoke = {u:bwapi.Unit? ->
                if (u != null) {
                    val id = u.id
                    val unitInfo = knownUnits.getOrPut(id, { UnitInfo(id, u) })
                    pair.second(unitInfo)
                }
            }, priority = Event.OBSERVE)
        }
        GameEvents.unitDestroy.subscribeWithArg("removeDead",
                invoke = {
                    println(it.id.toString() + " died")
                    it.job = IdleJob
                    knownUnits.remove(it.id)
                },
                priority=Event.CLEANUP_DATA)
    }

    val myUnits : List<UnitInfo>
        get() {
            return knownUnits.values.filter { it.base.player.id == AI.myId }
        }
    val myWorkers : List<UnitInfo>
        get() {
            return myUnits.filter { it.base.type.isWorker }
        }
    val myMiners : List<UnitInfo>
        get() {
            return myWorkers.filter { it.job == MiningJob }
        }
}