import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import resources.TestUtils.Expressions.binaryExpression
import resources.TestUtils.Expressions
import resources.TestUtils.Fluents
import resources.TestUtils.Operands

class BinaryExpressionTest : AnnotationSpec() {

    @Test
    fun testExpressionObjectWorkAsExpected() {
        binaryExpression.expression1 shouldBe Fluents.atBFloor
        binaryExpression.expression2 shouldBe Expressions.unaryExpressionNotAFloor
        binaryExpression.operand shouldBe Operands.and
    }
}
