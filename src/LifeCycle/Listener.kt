package LifeCycle

import Schedule.GameEvents
import bwapi.DefaultBWListener

/**
 * This object initializes the AI class,
 * wraps raw BWListener events with an exception handler,
 * and converts the events into the GameEvent interface
 */
object Listener : DefaultBWListener() {
    val mirror:bwapi.Mirror = bwapi.Mirror()
    private var lastFrame:Int = 0

    fun initialize() {
        mirror.module.setEventListener(this)
        mirror.startGame()
    }

    private fun safeExecute(exec : () -> Unit) {
        try {
            exec()
        } catch (e:Exception) {
            val callStack = e.stackTrace
            println(e)
            println(callStack)
        }
    }

    override fun onStart() {
        safeExecute {
            // this will automatically clear game state
            AI.newGame(mirror.game)
            println("fire start event")
            GameEvents.start(Unit)

        }
    }

    override fun onFrame() {
        safeExecute {
            val frame = AI.game.frameCount
            if (lastFrame != frame) {
                lastFrame = frame
                GameEvents.frame(frame)
            }
        }
    }

    override fun onEnd(p0: Boolean) { safeExecute { GameEvents.end(p0) } }
    override fun onSendText(p0: String?) { safeExecute { if (p0 != null) GameEvents.sendText(p0) } }
    override fun onUnitComplete(p0: bwapi.Unit?) { safeExecute { GameEvents._unitComplete(p0) }}
    override fun onUnitMorph(p0: bwapi.Unit?) { safeExecute { GameEvents._unitMorph(p0) }}
    override fun onUnitDestroy(p0: bwapi.Unit?) { safeExecute { GameEvents._unitDestroy(p0) }}
    override fun onUnitDiscover(p0: bwapi.Unit?) { safeExecute { GameEvents._unitDiscover(p0) }}
    override fun onUnitCreate(p0: bwapi.Unit?) { safeExecute { GameEvents._unitCreate(p0) }}
    override fun onUnitHide(p0: bwapi.Unit?) { safeExecute { GameEvents._unitHide(p0) }}
    override fun onUnitRenegade(p0: bwapi.Unit?) { safeExecute { GameEvents._unitRenegade(p0) }}
    override fun onUnitEvade(p0: bwapi.Unit?) { safeExecute { GameEvents._unitEvade(p0) }}
}

