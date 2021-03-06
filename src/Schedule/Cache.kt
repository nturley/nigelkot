package Schedule

class Cached<out T> (event: iEvent = Event.Never, private val get: () -> T) {
    private var t : T? = null
    init {
        event.subscribe("cache", {t = null}, priority = Event.CACHE_REFRESH)
    }
    operator fun invoke(): T {
        // convincing the compiler that this is safe
        val _t = t
        val x : T = if (_t !== null) _t else get()
        t = x
        return x
    }
}
