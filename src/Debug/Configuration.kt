package Debug

import Schedule.GameEvents

class Toggle(val label:String, var v : Boolean = false) {
    fun toggle(){
        v = !v
    }
}

object Configuration {
    init {
        GameEvents.start.subscribe("config", {
            GameEvents.sendText.subscribeWithArg("toggles", { s:String -> toggles[s]?.toggle() })
        })
    }
    val toggles = mapOf(Pair("id",Toggle("id", true)), Pair("job",Toggle("job", true)))
}
