import io.kotest.matchers.shouldBe
import resources.TestUtils
import resources.TestUtils.effectEmpty
import resources.TestUtils.effectNotEmpty
import resources.TestUtils.fluentEmpty
import resources.TestUtils.fluentNotEmpty
import resources.TestUtils.substitution
import kotlin.test.Test

class EffectTest {
    private val variable = Variable.of("different value")

    private val substitution2 = VariableAssignment.of(TestUtils.variableNotEmpty, variable)
    private val fluent = Fluent.of(
        TestUtils.name, List<Value>(TestUtils.size) { variable }, TestUtils.predicateNotEmpty, true
    )

    private val effect = Effect.of(fluent, true)

    @Test
    fun testEmptyCreation() {
        effectEmpty.fluent shouldBe fluentEmpty
        effectEmpty.isPositive shouldBe false

    }

    @Test
    fun testNotEmptyCreation() {
        effectNotEmpty.fluent shouldBe fluentNotEmpty
        effectNotEmpty.isPositive shouldBe true
    }

    @Test
    fun testApplyWorksAsExpected() {
        effectNotEmpty.apply(substitution) shouldBe effectNotEmpty
        effectNotEmpty.apply(substitution2) shouldBe effect
    }
}