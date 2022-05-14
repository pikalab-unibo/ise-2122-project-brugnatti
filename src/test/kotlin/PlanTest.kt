import io.kotest.matchers.shouldBe
import org.junit.Test
import resources.Res.actionNotEmpty
import resources.Res.planEmpty
import resources.Res.planNotEmpty

class PlanTest {
    @Test
    fun testEmptyCreation(){
        planEmpty.actions.isEmpty() shouldBe true
    }
    @Test
    fun testNotEmpty(){
        planNotEmpty.actions.isNotEmpty() shouldBe true
        planNotEmpty.actions.size shouldBe 1
        planNotEmpty.actions.forEach{it shouldBe actionNotEmpty}
    }
}