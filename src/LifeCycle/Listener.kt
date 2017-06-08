package LifeCycle

import Schedule.GameEvents
import bwapi.DefaultBWListener

/**
 * This object initializes the With class,
 * wraps raw BWListener events with an exception handler,
 * and converts the events into the GameEvent interface
 */
object Listener : DefaultBWListener() {
    val mirror:bwapi.Mirror = bwapi.Mirror()
    private var lastFrame:Int = 0
    private lateinit var events:GameEvents

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
            With.newGame(mirror.game)
            events = With.gameEvents
            events.start(Unit)
        }
    }

    override fun onFrame() {
        safeExecute {
            val frame = With.game.frameCount
            if (lastFrame != frame) {
                lastFrame = frame
                events.frame(frame)
            }
        }
    }

    override fun onEnd(p0: Boolean) { safeExecute { events.end(p0) } }
    override fun onSendText(p0: String?) { safeExecute { if (p0 != null) events.sendText(p0) } }
    override fun onUnitComplete(p0: bwapi.Unit?) { safeExecute { events._unitComplete(p0) }}
    override fun onUnitMorph(p0: bwapi.Unit?) { safeExecute { events._unitMorph(p0) }}
    override fun onUnitDestroy(p0: bwapi.Unit?) { safeExecute { events._unitDestroy(p0) }}
    override fun onUnitDiscover(p0: bwapi.Unit?) { safeExecute { events._unitDiscover(p0) }}
    override fun onUnitCreate(p0: bwapi.Unit?) { safeExecute { events._unitCreate(p0) }}
    override fun onUnitHide(p0: bwapi.Unit?) { safeExecute { events._unitHide(p0) }}
    override fun onUnitRenegade(p0: bwapi.Unit?) { safeExecute { events._unitRenegade(p0) }}
    override fun onUnitEvade(p0: bwapi.Unit?) { safeExecute { events._unitEvade(p0) }}
}

