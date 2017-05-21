package Events

import org.junit.After
import org.junit.Assert.*
import org.junit.Test

class EventTest {
    var a : Int = 0
    val e :Event<Unit> = Event("e")
    val e1:Event<Int> = Event("e1")

    @After
    fun tearDown() {
        a = 0
        e.clear()
        e1.clear()
    }

    @Test
    fun basic() {
        e.subscribe("a", {a = 1})
        assertTrue(a == 0)
        e()
        assertTrue(a == 1)
    }

    @Test
    fun basic1() {
        e1.subscribeWithArg("a", { i:Int-> a = i})
        assertTrue(a == 0)
        e1(2)
        assert(a == 2)
    }

    @Test
    fun condition() {
        e.subscribe(
                "a",
                condition = {a < 2},
                invoke = {a += 1})
        assert(a == 0)
        e()
        assert(a == 1)
        e()
        assert(a == 2)
        e()
        assert(a == 2)
    }

    @Test
    fun condition1() {
        e1.subscribeWithArg(
                "a",
                condition = {i:Int -> i == 47},
                invoke = { _ -> a += 1})
        assert(a == 0)
        e1(46)
        assert(a == 0)
        e1(47)
        assert(a == 1)
    }

    @Test
    fun oneShot() {
        e.subscribe("a", {a+=1}, oneShot = true)
        assert(a == 0)
        e()
        e()
        e()
        assert(a == 1)
    }

    @Test
    fun clear() {
        e.subscribe("a1", {a+=1})
        e.subscribe("a2", {a+=1})
        assert(a==0)
        e.clear()
        e()
        assert(a==0)
    }

    @Test
    fun unsubscribe() {
        e.subscribe("a1", {a+=1})
        e.subscribe("a2", {a+=1})
        assert(a==0)
        e.unsubscribe("a1")
        e()
        assert(a==1)
    }

    @Test
    fun priority() {
        e.subscribe("2", {assert(a==0)}, priority = 5)
        e.subscribe("0", {assert(a==1)})
        e.subscribe("-2",{assert(a==2)}, priority = -2)
        e.subscribe("-1",{a=2}, priority=-1)
        e.subscribe("1", {a=1}, priority = 1)
        assert(a==0)
        e()
        assert(a==2)
    }

    @Test
    fun derive() {
        val d = e.derive("d", {a<2})
        d.subscribe("d", {a+=1})
        assert(a==0)
        e()
        e()
        e()
        assert(a==2)
    }



}