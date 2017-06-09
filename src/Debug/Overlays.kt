package Debug
import BwapiWrappers.UnitInfo
import LifeCycle.AI
import Schedule.GameEvents
import Tracking.UnitTracker

object Overlays {
    fun init() {
        GameEvents.frame.subscribe("overlays", {
            val g = AI.game
            val myUnits = UnitTracker.knownUnits.values.filter { it.base.player.id == AI.myId }
            myUnits.forEach { g.drawTextMap(it.base.left, it.base.bottom, getUnitStr(it)) }
        })
    }

    private fun getUnitStr(unitInfo: UnitInfo): String {
        val unitText = Configuration.toggles.values.filter { it.v }.map {
            when (it.label) {
                "id" -> unitInfo.id.toString()
                "job" -> unitInfo.job.label
                else -> ""
            }
        }.filter { it != "" }
        return unitText.joinToString("\n")
    }
}