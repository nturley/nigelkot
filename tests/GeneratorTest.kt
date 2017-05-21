import org.junit.Test
import kotlin.coroutines.experimental.*

class GeneratorTest {
    @Test
    fun main() {
        val fibonacciSeq = buildSequence {
            println("A")
            yield(2)
            println("B")
            yield(5)
            println("C")
            yield(-1)
        }
        var nextToRun = 1
        val it = fibonacciSeq.iterator()
        for (i in 1..8) {
            println(i)
            if (i == nextToRun)
                nextToRun += it.next()
        }
    }
}

