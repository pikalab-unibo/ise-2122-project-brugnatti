import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.Test
import resources.res.axiomEmpty
import resources.res.axiomNotEmpty
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