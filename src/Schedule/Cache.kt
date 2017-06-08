package Schedule

class Cached<out T> (private val get: () -> T, event: iEvent = Event.Never) {
    private var t : T? = null
    init {
        event.subscribe("cache", {t = null})
    }
    operator fun invoke(): T {
        // convincing the compiler that this is safe
        val _t = t
        val x : T = if (_t !== null) _t else get()
        t = x
        return x
    }
}
