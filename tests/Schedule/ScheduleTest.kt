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
        Schedule.run {
            a += 1
            yield (Until("a",e))
            a += 2
            yield (Until("b",e))
            a += 3
            yield (Until("c",e))
            a += 4
            yield (Until("d", Event.Never))
            a += 5
        }
        assert (a==1)
        e()
        assert(a==3)
        e()
        assert(a==6)
        e()
        assert(a==10)
        e()
        assert(a==10)
    }

    @Test
    fun conditionArgument() {
        Schedule.runArg {
            a += 1
            yield (UntilArg("a",e1, condition= {i:Int -> i==47}))
            a += 2
            yield (UntilArg("b",e1, condition= {i:Int -> i==12}))
            a += 3
            yield (UntilArg("c",e1))
        }
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
    fun condition() {
        Schedule.run {
            while (true) {
                yield( Until("a", e, condition= { a > 3 }))
                a=1
            }
        }
        assert(a==0)
        e()
        assert(a==0)
        a=4
        e()
        assert(a==1)
    }
}