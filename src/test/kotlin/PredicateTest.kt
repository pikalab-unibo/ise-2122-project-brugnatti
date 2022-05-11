import impl.TypeImpl
import impl.PredicateImpl
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

import org.junit.Test
import resources.res.nameGC

class PredicateTest {
    private val type1= TypeImpl()
    private val predicateEmpty = PredicateImpl("", emptyList())
    private val predicateNotEmpty = PredicateImpl(nameGC, listOf(type1))

    @Test
    fun testEmptyCreation() {
        predicateEmpty.name shouldBe ""
        predicateEmpty.arguments.isEmpty() shouldBe true
    }
    @Test
    fun testNotEmptyCreation() {
        predicateNotEmpty.name shouldBe nameGC
        predicateNotEmpty.arguments.isEmpty() shouldNotBe true
    }
}