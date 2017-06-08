package LifeCycle

import BuildOrder.BuildOrderExec
import BuildOrder.Resources
import Debug.Configuration
import Debug.Overlays
import Jobs.assignWorkers
import Schedule.GameEvents
import Tracking.UnitTracker
import bwapi.Game
import bwapi.Text.Size.Enum
import bwta.BWTA

object With {

    lateinit var gameEvents : GameEvents
    lateinit var game : Game
    lateinit var overlays : Overlays
    var myId : Int = -1
    lateinit var reserved : Resources
    lateinit var buildOrderExec : BuildOrderExec

    fun newGame(g:Game) {
        game = g
        g.enableFlag(1)
        g.setTextSize(Enum.Small)
        println("analyzing map...")
        BWTA.readMap()
        BWTA.analyze()
        println("analysis complete.")

        gameEvents = GameEvents()
        overlays = Overlays()
        myId = game.self().id
        reserved = Resources(0,0,0)
        buildOrderExec = BuildOrderExec()
        assignWorkers()
        Configuration.checkToggles()
    }

    val unitTracker:UnitTracker
        get() {
            return gameEvents.unitTracker
        }

}
