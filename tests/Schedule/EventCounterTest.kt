package Schedule

import org.junit.After

import org.junit.Test

class EventCounterTest {
    val e : Event<Unit> = Event("e")
    val e2 : EventCounter = EventCounter(e,2)
    val e3 : EventCounter = EventCounter(e,3)
    var a : Int = 0

    @After
    fun tearDown() {
        e2.clear()
        e3.clear()
        a = 0
    }

    @Test
    fun basic2() {
        e2.subscribe("a", {a+=1})
        assert(a==0)
        for (i in 1..4) e(Unit)
        assert(a==2)
    }

    @Test
    fun basic3() {
        e3.subscribe("a", {a+=1})
        assert(a==0)
        for (i in 1..6) e(Unit)
        assert(a==2)
    }

    @Test
    fun condition() {
        e2.subscribe(
                "a",
                condition={a<2},
                invoke = {a+=1})
        assert(a==0)
        for (i in 1..10) e(Unit)
        assert(a==2)
    }

    @Test
    fun derive() {
        val d = e2.derive("d", {a==2})
        d.subscribe("d", {a=10})
        assert(a==0)
        e(Unit)
        e(Unit)
        assert(a==0)
        a=2
        e(Unit)
        e(Unit)
        assert(a==10)
        d.clear()
    }

    @Test
    fun unsubscribe() {
        e2.subscribe("a", {a+=1})
        e2.subscribe("b", {a+=1})
        assert(a==0)
        for (i in 1..4) e(Unit)
        assert(a==4)
        e2.unsubscribe("a")
        for (i in 1..4) e(Unit)
        assert(a==6)
    }

    @Test
    fun clear() {
        e2.subscribe("a", {a+=1})
        e2.subscribe("b", {a+=1})
        assert(a==0)
        for (i in 1..4) e(Unit)
        assert(a==4)
        e2.clear()
        for (i in 1..4) e(Unit)
        assert(a==4)
    }

}