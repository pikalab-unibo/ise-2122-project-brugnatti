import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import resources.TestUtils.axiomEmpty
import resources.TestUtils.axiomNotEmpty
import kotlin.test.Test

class AxiomTest {

    @Test
    fun testEmptyCreation() {
        axiomEmpty.parameters.isEmpty() shouldBe true
        axiomEmpty.context.isEmpty() shouldBe true
        axiomEmpty.implies.isEmpty() shouldBe true
    }

    @Test
    fun testNotEmptyCreation() {
        axiomNotEmpty.parameters.isEmpty() shouldNotBe true
        axiomNotEmpty.context.isEmpty() shouldNotBe true
        axiomNotEmpty.implies.isEmpty() shouldNotBe true
    }
}