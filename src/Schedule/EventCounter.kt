package Schedule


class EventCounter(val event: iEvent, val n:Int) : iEvent(event.label + ":" + n) {

    var i : Int = 0
    private var subscriberList : MutableList<Event.Subscriber<Unit>> = mutableListOf()

    override fun init() {
        event.subscribe(label, invoke = { fire() }, priority = Event.DERIVED_EVENT)
    }

    private fun fire() {
        val filtered = subscriberList
                .filterIndexed { index, _ -> index % n == i }
        for (subscriber in filtered) {
            subscriber.fire(Unit)
        }
        i = (i+1)%n
    }

    override fun subscribe(
            label: String,
            invoke: () -> Unit,
            condition: () -> Boolean,
            priority: Int,
            oneShot: Boolean) {
        subscriberList.add(Event.Subscriber(label, { _ -> invoke()},{_ -> condition()}, priority, oneShot))
    }

    override fun unsubscribe(label: String) {
        subscriberList = subscriberList.filter { it.label != label } as MutableList<Event.Subscriber<Unit>>
    }

    override fun clear() {
        subscriberList.clear()
    }

    fun derive(label: String, condition: () -> Boolean) : Event<Unit> {
        val ret : Event<Unit> = Event(label)
        subscribe(
                label,
                condition = condition,
                invoke = {ret(Unit)})
        return ret
    }
}