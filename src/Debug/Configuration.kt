package Debug

import LifeCycle.With

class Toggle(val label:String, var v : Boolean = false) {
    fun toggle(){
        v = !v
    }
}

object Configuration {
    val toggles = mapOf(Pair("id",Toggle("id", true)), Pair("job",Toggle("job", true)))

    fun checkToggles() {
        With.gameEvents.sendText.subscribeWithArg("toggles", {s:String -> toggles[s]?.toggle() })
    }

}
