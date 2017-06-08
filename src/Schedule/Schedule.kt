package Schedule

import kotlin.coroutines.experimental.*

data class Until<T>(val event:Event<T>, val condition: (T) -> Boolean = { _-> true})

class RunHandle (val label:String) {
    var nextHandle: RunHandle? = null
    var nextAction = {}
    infix fun <T>then (action :suspend SequenceBuilder<Until<T>>.() -> Unit) : RunHandle {
        val ret = RunHandle(label)
        nextHandle = ret
        nextAction = { getNextYieldArg(buildIterator(action), label, ret)}
        return ret
    }
}

fun <T> run(label:String, action: suspend SequenceBuilder<Until<T>>.() -> Unit) : RunHandle {
    val runHandle = RunHandle(label)
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
