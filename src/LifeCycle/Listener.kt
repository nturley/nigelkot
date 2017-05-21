package LifeCycle

import bwapi.DefaultBWListener

object Listener : DefaultBWListener() {
    val mirror:bwapi.Mirror = bwapi.Mirror()
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
            With.game = mirror.game
        }
    }
}

