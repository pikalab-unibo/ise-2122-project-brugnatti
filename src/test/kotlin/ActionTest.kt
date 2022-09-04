import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import resources.BlockWorldDomain.Actions
import resources.BlockWorldDomain.Fluents
import resources.BlockWorldDomain.Types
import resources.BlockWorldDomain.Values
import resources.BlockWorldDomain.actionEmpty
import resources.BlockWorldDomain.actionNotEmpty
import resources.BlockWorldDomain.actions
import resources.BlockWorldDomain.name
import resources.BlockWorldDomain.predicateNotEmpty
import resources.BlockWorldDomain.size
import resources.BlockWorldDomain.substitution
import resources.BlockWorldDomain.type1
import resources.BlockWorldDomain.types
import resources.BlockWorldDomain.variableNotEmpty
import resources.BlockWorldDomain.variables

class ActionTest : AnnotationSpec() {
    private val variable = Variable.of("different value")
    private val substitution2 = VariableAssignment.of(variableNotEmpty, variable)
    private val fluent = Fluent.of(
        predicateNotEmpty,
        true,
        List<Value>(size) { variable }
    )
    private val action = Action.of(
        name,
        mapOf(variableNotEmpty to type1),
        setOf(fluent),
        setOf(Effect.of(fluent, true))
    )

    @Test
    fun testEmptyCreation() {
        actionEmpty.name.isEmpty() shouldBe true
        actionEmpty.parameters.isEmpty() shouldBe true
        actionEmpty.preconditions.isEmpty() shouldBe true
        actionEmpty.effects.isEmpty() shouldBe true
    }

    @Test
    fun testNotEmptyCreation() {
        actionNotEmpty.name shouldBe name
        actionNotEmpty.parameters.isEmpty() shouldNotBe true
        actionNotEmpty.parameters.forEach { it.value shouldBe type1 }
        actionNotEmpty.preconditions.isEmpty() shouldNotBe true
        actionNotEmpty.effects.isEmpty() shouldNotBe true
    }

    @Test
    fun testApplyWorksAsExpected() {
        actionNotEmpty.apply(substitution) shouldBe actionNotEmpty
        actionNotEmpty.apply(substitution2) shouldBe action
    }

    @Test
    fun testActionObjectWorksAsExpected() {
        Actions.pick shouldBeIn actions

        Actions.pick.parameters.isEmpty() shouldNotBe true
        Actions.pick.parameters.forEach { it.key shouldBeIn variables }
        Actions.pick.parameters.forEach { it.value shouldBeIn types }

        Actions.pick.preconditions.isEmpty() shouldNotBe true
        Actions.pick.effects.isEmpty() shouldNotBe true
    }

    @Test
    fun testActionObjectVariableAssignmentX2XworksAsExpected() {
        Actions.pick.apply(VariableAssignment.of(Values.X, Values.X)) shouldBe
            Action.of(
                "pick",
                mapOf(
                    Values.X to Types.blocks
                ),
                setOf(Fluents.armEmpty, Fluents.clearX),
                setOf(
                    Effect.of(Fluents.atXArm),
                    Effect.negative(Fluents.armEmpty),
                    Effect.negative(Fluents.clearX),
                    Effect.negative(Fluents.atXFloor)
                )
            )
    }

    @Test
    fun testActionObjectVariableAssignmentX2YworksAsExpected() {
        Actions.pick.apply(VariableAssignment.of(Values.X, Values.Y)) shouldBe
            Action.of(
                "pick",
                mapOf(
                    Values.X to Types.blocks
                ),
                setOf(Fluents.armEmpty, Fluents.clearY),
                setOf(
                    Effect.of(Fluents.atYArm),
                    Effect.negative(Fluents.armEmpty),
                    Effect.negative(Fluents.clearY),
                    Effect.negative(Fluents.atYFloor)
                )
            )
    }
}
