package LifeCycle

import BuildOrder.BuildOrderExec
import BuildOrder.Resources
import BuildOrder.startingBuildOrder
import Debug.Overlays
import Jobs.assignWorkers
import Schedule.GameEvents
import Tracking.UnitTracker
import bwapi.Game
import bwapi.Text.Size.Enum
import bwta.BWTA

object AI {

    lateinit var game : Game
    var myId : Int = -1
    lateinit var reserved : Resources

    fun newGame(g:Game) {
        game = g
        g.enableFlag(1)
        g.setTextSize(Enum.Small)
        println("analyzing map...")
        BWTA.readMap()
        BWTA.analyze()
        println("analysis complete.")

        // clear all subscribers to all events
        println("clear events")
        GameEvents._events.forEach { it.clear() }
        // resubscribe derived events
        GameEvents._derivedEvents.forEach { it.init() }

        myId = game.self().id
        reserved = Resources(0,0,0)

        // initialize subsystems
        assignWorkers()
        UnitTracker.init()
        BuildOrderExec.init()
        Overlays.init()
    }

}
