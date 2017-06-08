package Debug
import BwapiWrappers.UnitInfo
import LifeCycle.With
import bwapi.Color

class Overlays {
    init {
        With.gameEvents.frame.subscribe("overlays", {
            val g = With.game
            val myUnits = With.unitTracker.knownUnits.values.filter { it.base.player.id == With.myId }
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