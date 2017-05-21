package Schedule

import kotlin.coroutines.experimental.*

data class Until(val label: String, val event:Event<Unit>, val condition: () -> Boolean = {true})
data class UntilArg<T>(val label:String, val event:Event<T>, val condition: (T) -> Boolean = {_-> true})

fun run(action: suspend SequenceBuilder<Until>.() -> Unit) {
    getNextYield(buildSequence(action).iterator())
}

fun <T> runArg(action: suspend SequenceBuilder<UntilArg<T>>.() -> Unit) {
    getNextYieldArg(buildSequence(action).iterator())
}

fun getNextYield(iter:Iterator<Until>) {
    val nextStep = iter.next()
    if (nextStep.event != Event.Never) {
        nextStep.event.subscribe(
                nextStep.label,
                condition = nextStep.condition,
                invoke= { getNextYield(iter) },
                oneShot= true)
    }
}

fun <T> getNextYieldArg(iter:Iterator<UntilArg<T>>) {
    val nextStep = iter.next()
    if (nextStep.event != Event.Never) {
        nextStep.event.subscribeWithArg(nextStep.label,
                condition = nextStep.condition,
                invoke= { getNextYieldArg(iter) },
                oneShot= true)
    }
}
