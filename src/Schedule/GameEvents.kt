package Schedule

import BwapiWrappers.UnitInfo
import LifeCycle.With
import Tracking.UnitTracker
import java.util.Arrays.asList

/**
 * This class holds all game events, even
 */

class GameEvents {
    val start : Event<Unit> = Event("Start")
    val frame : Event<Int> = Event("Frame")
    val end : Event<Boolean> = Event("End")
    val sendText : Event<String> = Event("SendText")

    val _unitComplete : Event<bwapi.Unit?> = Event("UnitComplete")
    val _unitDestroy : Event<bwapi.Unit?> = Event("UnitDestroy")
    val _unitDiscover : Event<bwapi.Unit?> = Event("UnitDiscover")
    val _unitCreate : Event<bwapi.Unit?> = Event("UnitCreate")
    val _unitEvade : Event<bwapi.Unit?> = Event("UnitEvade")
    val _unitHide : Event<bwapi.Unit?> = Event("UnitHide")
    val _unitShow : Event<bwapi.Unit?> = Event("UnitShow")
    val _unitRenegade : Event<bwapi.Unit?> = Event("UnitRenegade")
    val _unitMorph : Event<bwapi.Unit?> = Event("UnitMorph")

    val unitComplete : Event<UnitInfo> = Event("UnitComplete")
    val unitDestroy : Event<UnitInfo> = Event("UnitDestroy")
    val unitDiscover : Event<UnitInfo> = Event("UnitDiscover")
    val unitCreate : Event<UnitInfo> = Event("UnitCreate")
    val unitEvade : Event<UnitInfo> = Event("UnitEvade")
    val unitHide : Event<UnitInfo> = Event("UnitHide")
    val unitShow : Event<UnitInfo> = Event("UnitShow")
    val unitRenegade : Event<UnitInfo> = Event("UnitRenegade")
    val unitMorph : Event<UnitInfo> = Event("UnitMorph")

    private val unitEvents = listOf(
            Pair(_unitComplete, unitComplete),
            Pair(_unitDestroy, unitDestroy),
            Pair(_unitDiscover, unitDiscover),
            Pair(_unitCreate, unitCreate),
            Pair(_unitEvade, unitEvade),
            Pair(_unitHide, unitHide),
            Pair(_unitShow, unitShow),
            Pair(_unitRenegade, unitRenegade),
            Pair(_unitMorph, unitMorph))
    val unitTracker : UnitTracker = UnitTracker(unitEvents)

    val frame10 = EventCounter(frame, 10)
    val frame24 = EventCounter(frame, 24)
    val frame100 = EventCounter(frame, 100)

    val events : List<iEvent> = listOf(
            start,
            frame,
            end,
            sendText,
            unitComplete,
            unitDestroy,
            unitDiscover,
            unitCreate,
            unitEvade,
            unitHide,
            unitShow,
            unitRenegade,
            unitMorph,
            frame10,
            frame24,
            frame100).filterIsInstance<iEvent>()
}