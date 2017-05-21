package Events

class GameEvents {
    val start : Event<Unit> = Event("Start")
    val frame : Event<Unit> = Event("Frame")
    val unitComplete : Event<bwapi.Unit> = Event("UnitComplete")
    val unitDestroy : Event<bwapi.Unit> = Event("UnitDestroy")
    val unitDiscover : Event<bwapi.Unit> = Event("UnitDiscover")
    val end : Event<Boolean> = Event("End")
    val sendText : Event<String> = Event("SendText")
    val unitCreate : Event<bwapi.Unit> = Event("UnitCreate")
    val unitEvade : Event<bwapi.Unit> = Event("UnitEvade")
    val unitHide : Event<bwapi.Unit> = Event("UnitHide")
    val unitShow : Event<bwapi.Unit> = Event("UnitShow")
    val unitRenegade : Event<bwapi.Unit> = Event("UnitRenegade")
    val unitMorph : Event<bwapi.Unit> = Event("UnitMorph")
    
}