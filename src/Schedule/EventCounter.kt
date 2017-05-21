package Schedule


class EventCounter(event: Event<Unit>, val n:Int) {

    val label = event.label + ":" + n
    var i : Int = 0
    private var subscriberList : MutableList<Event.Subscriber<Unit>> = mutableListOf()

    init {
        assert(n > 1)
        event.subscribe(label, invoke = { fire() }, priority = Event.DERIVED_EVENT)
    }

    private fun fire() {
        val filtered = subscriberList
                .filterIndexed { index, _ -> index % n == i }
        for (subscriber in filtered) {
            subscriber.fire(null)
        }
        i = (i+1)%n
    }

    fun subscribe(
            label : String,
            invoke : () -> Unit,
            condition : () -> Boolean = { true}) {
        subscriberList.add(Event.Subscriber(label, { _ -> invoke()},{_ -> condition()}, 0, false))
    }

    fun unsubscribe(label: String) {
        subscriberList = subscriberList.filter { it.label != label } as MutableList<Event.Subscriber<Unit>>
    }

    fun clear() {
        subscriberList.clear()
    }

    fun derive(label: String, condition: () -> Boolean) : Event<Unit> {
        val ret = Event<Unit>(label)
        subscribe(
                label,
                condition = condition,
                invoke = {ret()})
        return ret
    }
}