package LifeCycle

import BuildOrder.BuildOrderExec
import BuildOrder.Resources
import BuildOrder.startingBuildOrder
import BwapiWrappers.BaseLocationInfo
import BwapiWrappers.RegionInfo
import Debug.Overlays
import Jobs.assignWorkers
import Schedule.GameEvents
import Tracking.UnitTracker
import bwapi.Game
import bwapi.Player
import bwapi.Text.Size.Enum
import bwapi.TilePosition
import bwta.BWTA
import bwta.BaseLocation

object AI {

    lateinit var game : Game
    var myId : Int = -1
    lateinit var self : Player
    lateinit var startLocation: TilePosition
    lateinit var startBase: BaseLocationInfo
    lateinit var startRegion: RegionInfo
    lateinit var reserved : Resources

    fun newGame(g:Game) {
        game = g
        g.enableFlag(1)
        g.setTextSize(Enum.Small)

        //reset map info
        RegionInfo.init()
        println("analyzing map...")
        BWTA.readMap()
        BWTA.analyze()
        println("analysis complete.")

        // clear all subscribers to all events
        println("clear events")
        GameEvents._events.forEach { it.clear() }
        // resubscribe derived events
        GameEvents._derivedEvents.forEach { it.init() }

        self = game.self()
        myId = self.id

        RegionInfo.init()

        startLocation = self.startLocation
        startBase = BaseLocationInfo.getNearestBaseLocation(startLocation)
        startRegion = startBase.region()

        reserved = Resources(0,0,0)

        // initialize subsystems
        assignWorkers()
        UnitTracker.init()
        BuildOrderExec.init()
        Overlays.init()
    }

}
