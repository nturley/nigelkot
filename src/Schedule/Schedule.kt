package Schedule

import kotlin.coroutines.experimental.*

data class Until<T>(val event:Event<T>, val condition: (T) -> Boolean = { _-> true})

class RunHandle {
    var nextAction = {}
    infix fun then (action:()->Unit) {
        nextAction = action
    }
}

fun <T> run(label:String, action: suspend SequenceBuilder<Until<T>>.() -> Unit) : RunHandle {
    val runHandle = RunHandle()
    getNextYieldArg(buildIterator(action), label, runHandle)
    return runHandle
}

fun <T> getNextYieldArg(iter:Iterator<Until<T>>, label:String, runHandle: RunHandle) {
    if (iter.hasNext()) {
        val nextStep = iter.next()
        nextStep.event.subscribeWithArg(label,
                condition = nextStep.condition,
                invoke = { getNextYieldArg(iter, label, runHandle) },
                oneShot = true)
    } else {
        runHandle.nextAction()
    }
}
