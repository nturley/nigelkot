package Jobs

import BwapiWrappers.BaseLocationInfo
import BwapiWrappers.UnitInfo
import LifeCycle.AI
import Schedule.GameEvents
import Tracking.UnitTracker


object MiningJob : Job("mining", 1) {

    override fun start(unitInfo:UnitInfo) {
        // once a second, if you aren't mining,
        // find the nearest mineral field
        // and mine it
        getToWork(unitInfo)
        GameEvents.frame10.subscribe(
                label(unitInfo),
                condition = { !unitInfo.base.isGatheringMinerals },
                invoke = { getToWork(unitInfo) })
    }

    fun getToWork(unitInfo: UnitInfo) {
        // mine from a mineral in the closest base
        println(unitInfo.id.toString() + " start mining")
        assert(unitInfo.job == MiningJob)
        val nearestBase = BaseLocationInfo.getNearestBaseLocation(unitInfo.base.position)
        val minerals = nearestBase.base.staticMinerals
        val target = UnitTracker.get(minerals.first())
        unitInfo.gather(target)
    }
}