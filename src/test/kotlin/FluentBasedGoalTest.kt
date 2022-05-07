import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import org.junit.Test
import resources.res.state

class FluentBasedGoalTest{

    private val fluentBasedGoalEmpty: FluentBasedGoal= mockkClass(FluentBasedGoal::class){
        every { fluent } returns emptySet()
        every { isStatisfiedBy(state) } returns false
    }

    private val fluentBasedGoalNotEmpty: FluentBasedGoal= mockkClass(FluentBasedGoal::class){
        every { fluent } returns mockk(relaxed=true)
        every { isStatisfiedBy(state) } returns true
    }
    @Test
    fun testEmptyCreation(){
        fluentBasedGoalEmpty.fluent.isEmpty() shouldBe true
        fluentBasedGoalEmpty.isStatisfiedBy(state) shouldBe false
    }
    @Test
    fun testNotEmptyCreation(){
        fluentBasedGoalNotEmpty.fluent.isEmpty() shouldBe false
        fluentBasedGoalNotEmpty.isStatisfiedBy(state) shouldBe true
    }

}
