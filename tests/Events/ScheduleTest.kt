package Events

import org.junit.After
import org.junit.Test

class ScheduleTest {
    val e : Event<Unit> = Event("e")
    var a : Int = 0

    @After
    fun teardown() {
        a=0
        e.clear()
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