package Schedule

import org.junit.After
import org.junit.Test

class ScheduleTest {
    val e : Event<Unit> = Event("e")
    val e1 : Event<Int> = Event("e1")
    var a : Int = 0

    @After
    fun teardown() {
        a=0
        e.clear()
        e1.clear()
    }

    @Test
    fun basic() {
        run("a", {
            a += 1
            yield (Until(e))
            a += 2
            yield (Until(e))
            a += 3
            yield (Until(e))
            a += 4
            yield (Until(Event.Never))
            a += 5
        })
        assert (a==1)
        e(Unit)
        assert(a==3)
        e(Unit)
        assert(a==6)
        e(Unit)
        assert(a==10)
        e(Unit)
        assert(a==10)
    }

    @Test
    fun condition() {
        Schedule.run("a", {
            while (true) {
                yield( Until(e, condition= { a > 3 }))
                a=1
            }
        })
        assert(a==0)
        e(Unit)
        assert(a==0)
        a=4
        e(Unit)
        assert(a==1)
    }

    @Test
    fun conditionArgument() {
        Schedule.run("a", {
            a += 1
            yield (Until(e1, condition= { i:Int -> i==47}))
            a += 2
            yield (Until(e1, condition= { i:Int -> i==12}))
            a += 3
            yield (Until(e1))
        })
        assert (a==1)
        e1(0)
        assert(a==1)
        e1(47)
        assert(a==3)
        e1(48)
        assert(a==3)
        e1(12)
        assert(a==6)
    }

    @Test
    fun chain() {
        run("a", {
            a+=1
            yield(Until(e))
            a+=2
        }).then {run("b",{
            a+=3
            yield(Until(e1))
            a+=4
        })}
        assert(a==1)
        e(Unit)
        assert(a==6)
        e1(5)
        assert(a==10)
    }

}