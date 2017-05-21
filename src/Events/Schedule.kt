package Events

import kotlin.coroutines.experimental.*

object Schedule {
    fun run(action: suspend SequenceBuilder<Until>.() -> Unit) {
        getNextYield(buildSequence(action).iterator())
    }

    fun getNextYield(iter:Iterator<Until>) {
        val nextStep = iter.next()
        if (nextStep.event != Event.Never) {
            nextStep.event.subscribe(nextStep.label,
                     condition = nextStep.condition,
                     invoke= { getNextYield(iter) },
                     oneShot= true)
        }
    }

}
