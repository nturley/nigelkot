# nigelkot
bwapi kotlin bot

Here's some of the tools I've got

# Events

Events allow you (the subscriber) to be notified every time an event is fired (usually by a publisher).

This is useful for subscribing to BWAPI events (unit created, unit destroyed, etc), and it's easy to create derived events (myUnitDestroyed, foundEnemyBase)

Subscribers can provide
* a condition which must be true before notifying
* a priority level to give an ordering to when subscribers are notified.
* "oneshot" which automatically unsubscribes after it is invoked.

Events can be cleared or you can unsubscribe all subscriptions with a label.

Example
```kotlin
val e:Event<Unit> = Event("e")
var a:Int = 0
e.subscribe("a", {a = 1})
assert(a==0)
e()
assert(a==1)
```

Argument Example
```kotlin
val e:Event<Int> = Event("e")
var a:Int = 0
e.subscribeWithArg("a", { i:Int -> a = i})
assert(a==0)
e(2)
assert(a==2)
```

# Event Counter
Event counter allows you to subscribe to every Nth event.

Sometimes, you want something to occur on an event, but not every single event (usually for performance reasons). For instance you may want to update a state variable every ten frames. Ideally, you also don't pile with every other ten-frame subscription on the same frame.

Example
```kotlin
val e : Event<Unit> = Event("e")
val e3 : EventCounter = EventCounter(e,3)
var a : Int = 0
e3.subscribe("a", { a += 1 })
assert(a == 0)
for (i in 1..6) e()
assert(a == 2)
```

# Cached
This class caches a value that is invalidated on an event. This is useful for avoiding expensive calls to determine answers to questions that have already been asked.

Example
```kotlin
val e : Event<Unit> = Event("e")
var accesses : Int = 0
val accessCache : Cached<Int> = Cached({ access() }, e)

fun access() :Int {
    accesses += 1
    return accesses
}

assert( accesses == 0)
assert( accessCache() == 1)
assert( accesses == 1)
assert( accessCache() == 1)
e()
assert( acesses == 1)
assert( accessCache() == 2)
assert( acesses == 2)
```

# Schedule.run

This uses fancy-pants experimental kotlin coroutines to suspend a function until an event occurs.

This is really neat. Typically, agents have a sequence of steps to accomplish a task, and each step occurs in a different event occurrence. Which means that you have break up the steps into separate functions and store state as class members and usually your code starts looking like a statemachine, even though the steps are pretty much sequential.

With coroutines, you can just write a single sequence of steps in a single function and function locals are saved and restored while the function is suspended.

If you want to abandon a coroutine before it's next event fires, simply unsubscribe the label from the event.

You use it by calling Schedule.run with your function and yielding Until's. The Until instance specifies when to resume this function.

Example
```kotlin
val e : Event<Unit> = Event("e")
var a : Int = 0

Schedule.run {
    a += 1
    yield (Until("a", e))
    a += 2
    yield (Until("a", e))
    a += 3
    yield (Until("a", Event.Never))
}
assert (a == 1)
e()
assert(a == 3)
e() 
assert(a == 6)
e()
assert(a == 6)
```

If your event has an argument and you want to add conditions to that argument, use runArg and UntilArg

```kotlin
val e1 : Event<Int> = Event("e1")
var a : Int = 0
Schedule.runArg {
    a += 1
    yield (UntilArg("a",e1, condition= {i:Int -> i == 47}))
    a += 2
    yield (UntilArg("b",e1, condition= {i:Int -> i == 12}))
    a += 3
    yield (UntilArg("c",e1))
}
assert (a == 1)
e1(0)
assert(a == 1)
e1(47)
assert(a == 3)
e1(47)
assert(a == 3)
e1(12)
assert(a == 6)
```

I'm still trying to figure out how to have a schedule.run that can yield Until objects whose event arguments are all of different types.

Until I do, all yields in a run must have the same event arg type. Which is annoying but probably acceptable for many use cases.
