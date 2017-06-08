package Jobs

import BwapiWrappers.UnitInfo
import LifeCycle.With


object MiningJob : Job("mining", 1) {

    override fun start(unitInfo:UnitInfo) {
        // every ten frames, if you aren't mining,
        // find the nearest mineral field
        // and mine it
        With.gameEvents.frame10.subscribe(
                label(unitInfo),
                condition = { !unitInfo.base.isGatheringMinerals },
                invoke = {
                    assert(unitInfo.job == MiningJob)
                    println(unitInfo.id.toString() + " start mining")
                    val target = With.game.neutralUnits.filter { it.type.isMineralField }.minBy { it.getDistance(unitInfo.base) }
                    unitInfo.base.gather(target)
                })
    }
}