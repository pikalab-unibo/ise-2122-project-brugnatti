package explanation

import domain.BlockWorldDomain.Operators.pickA
import domain.BlockWorldDomain.Operators.putdownA
import domain.BlockWorldDomain.Problems
import explanation.impl.QuestionPlanSatisfiability
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe

class Question5Test : AnnotationSpec() {
    private val explainer = Explainer.of(Planner.strips())

    @Test
    fun `Plan valid`() {
        val q5 = QuestionPlanSatisfiability(Problems.pickX, Plan.of(listOf(pickA)), pickA, 0)
        val explanation = Explanation.of(q5, explainer)
        println(explanation.toString())
        explanation.isPlanValid() shouldBe true
    }

    @Test
    fun `Plan not valid`() {
        val q5 = QuestionPlanSatisfiability(
            Problems.pickX,
            Plan.of(listOf(pickA, putdownA)),
            pickA,
            0
        )
        val explanation = Explanation.of(q5, explainer)
        println(explanation.toString())
        explanation.isPlanValid() shouldBe false
    }
}
