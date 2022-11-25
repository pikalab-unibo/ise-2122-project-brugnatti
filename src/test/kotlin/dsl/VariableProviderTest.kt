package dsl

import dsl.provider.VariableProvider
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import resources.domain.BlockWorldDomain.Values

class VariableProviderTest : AnnotationSpec() {
    private val variableProvider = VariableProvider.of()

    @Test
    fun testPredicateExists() {
        variableProvider.addVariable(Values.X)
        variableProvider.addVariable(Values.Y)
        variableProvider.findVariable(Values.X.name) shouldNotBe null
        variableProvider.findVariable(Values.Y.name) shouldNotBe null
    }

    @Test
    fun testPredicateNotExists() {
        variableProvider.findVariable(Values.W.name) shouldBe null
        variableProvider.findVariable("nothing") shouldBe null
    }

    @Test
    fun testVariableProviderWorksAsExpected() {
        val variables = variableProvider.getVariables()
        variables.size shouldBe 2
        Values.X.name shouldBeIn variables.keys
        Values.Y.name shouldBeIn variables.keys
    }
}