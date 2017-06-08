package Schedule

/**
 * Collects subscribers that are notified whenever an event occurs.
 * Publishers simply call fire with the arguments of this event occurrence.
 * Subscribers can provide conditions to filter out which occurrences they care about.
 * Subscribers can also provide a priority to order the notifications.
 * Subscribers can also provide a label that they can use to unsubscribe from the event.
 * The condition is evaluated just before invoke is called
 * subscribing and unsubscribing resolves the next time the event fires
 */
class Event<T>(label:String) : iEvent(label) {

    companion object {
        val DERIVED_EVENT : Int = -100
        val CACHE_REFRESH : Int = -99

        val Never : Event<Unit> = Event("never")
    }

    class Subscriber<T>(val label:String,
                           val invoke : (T) -> Unit,
                           val condition : (T) -> Boolean,
                           val priority : Int,
                           val singleShot : Boolean
    ) : Comparable<Subscriber<T>> {
        var active : Boolean = true

        override fun compareTo(other: Subscriber<T>): Int {
            if (priority == other.priority) return 1
            return other.priority - priority
        }

        fun fire(argval:T) {
            if (condition(argval)) {
                invoke(argval)
                if (singleShot) active = false
            }
        }
    }

    fun derive(label: String, condition: () -> Boolean) : Event<Unit> {
        val ret : Event<Unit> = Event(label)
        subscribe(
                label,
                condition = condition,
                invoke = {ret(Unit)},
                priority = DERIVED_EVENT)
        return ret
    }

    fun deriveWithArg(label: String, condition: (T) -> Boolean) : Event<T> {
        val ret : Event<T> = Event(label)
        subscribeWithArg(
                label,
                condition = condition,
                invoke = {ret(it)},
                priority = DERIVED_EVENT)
        return ret
    }

    private var subscriberList : MutableList<Subscriber<T>> = mutableListOf()

    /**
     * Notifies all subscribers that an event has occurred
     * subscribers that have unsubscribed before this is called
     * will not be invoked. Subscribers whose conditions evaluate
     * to false at the time of potential invocation will not
     * be invoked. Oneshot subscribers will be unsubscribed if they
     * invoke
     * @param arg arguments for this event firing
     */
    operator fun invoke(arg: T) {
        val filtered = subscriberList
                .filter { it.active }
                .sorted()
        for (subscriber in filtered) {
            subscriber.fire(arg)
        }

        subscriberList = if (filtered.isNotEmpty()) subscriberList.filter { it.active } as MutableList<Subscriber<T>> else mutableListOf()
    }

    /**
     * Calls invocation function with the argument every time this event occurs if the condition is true
     * If a null event argument is given, invoke will not be called
     * @param label a label for removing the subscription later
     * @param invoke the function to call when the event fires
     * @param condition only call invocation if this condition is true
     * @param priority higher priority subscriptions will be invoked before lower
     * @param oneShot indicates whether the subscriber should be removed after calling invoke
     */
    fun subscribeWithArg(
                            label : String,
                            invoke : (T) -> Unit,
                            condition : (T) -> Boolean = {_ -> true},
                            priority : Int = 0,
                            oneShot : Boolean = false) {
        val s = Subscriber(
                label,
                invoke,
                condition,
                priority,
                oneShot)
        subscriberList.add(s)
    }

    /**
     * Calls invocation function every time this event occurs if the condition is true
     * event arguments are ignored
     * @param label a label for removing the subscription later
     * @param invoke the function to call when the event fires
     * @param condition only call invocation if this condition is true
     * @param priority higher priority subscriptions will be invoked before lower
     * @param oneShot indicates whether the subscriber should be removed after calling invoke
     */
    override fun subscribe(
            label : String,
            invoke : () -> Unit,
            condition : () -> Boolean,
            priority : Int,
            oneShot : Boolean) {
        subscriberList.add(Subscriber(label, { _ -> invoke()},{_ -> condition()}, priority, oneShot))
    }

    /**
     * Removes all subscriptions whose label is equal to withLabel
     * @param label the label to compare against
     */
    override fun unsubscribe(label : String) {
        subscriberList = subscriberList.filter { it.label != label } as MutableList<Subscriber<T>>
    }

    /**
     * Removes all subscriptions
     */
    override fun clear() {
        subscriberList = mutableListOf()
    }

    override fun toString() : String {
        return label + ":\n" + subscriberList.map { it.label }.joinToString(",\n")
    }
}
