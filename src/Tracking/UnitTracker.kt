package Tracking

import BwapiWrappers.UnitInfo
import Jobs.MiningJob
import LifeCycle.With
import Schedule.Event
import Schedule.GameEvents


class UnitTracker(unitEvents: List <Pair <Event <bwapi.Unit?>, Event <UnitInfo> > >) {
    val knownUnits : MutableMap<Int, UnitInfo> = mutableMapOf()
    init {
        unitEvents.forEach { pair: Pair <Event <bwapi.Unit?>, Event <UnitInfo> > ->
            pair.first.subscribeWithArg("unit wrapper",invoke = {u:bwapi.Unit? ->
                if (u != null) {
                    val id = u.id
                    val unitInfo = knownUnits.getOrPut(id, { UnitInfo(id, u) })
                    pair.second(unitInfo)
                }
            }, priority = 100)
        }
    }
    val myUnits : List<UnitInfo>
        get() {
            return knownUnits.values.filter { it.base.player.id == With.myId }
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