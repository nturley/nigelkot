package Schedule

import org.junit.Test

class CachedTest {
    val e : Event<Unit> = Event("e")
    var a : Int = 0
    val c : Cached<Int> = Cached({ incr() }, e)

    fun incr() :Int {
        a += 1
        return a
    }

    @Test
    fun basic() {
        assert(a==0)
        assert(c()==1)
        assert(a==1)
        assert(c()==1)
        e(Unit)
        assert(a==1)
        assert(c()==2)
        assert(a==2)
        e(Unit)
        e(Unit)
        assert(c()==3)
    }

}