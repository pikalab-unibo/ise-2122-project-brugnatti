import io.kotest.matchers.shouldBe
import kotlin.test.Test
import resources.TestUtils.objNotEmpty
import resources.TestUtils.objectSetEmpty
import resources.TestUtils.objectSetNotEmpty
import resources.TestUtils.type1

class ObjectSetTest {

    @Test
    fun testEmptyCreation(){
        objectSetEmpty.map.isEmpty() shouldBe true
    }

    @Test
    fun testNotEmpty(){
        objectSetNotEmpty.map.isNotEmpty() shouldBe true
        objectSetNotEmpty.map.size shouldBe 1
        objectSetNotEmpty.map.keys.forEach { it shouldBe type1 }
        objectSetNotEmpty.map.values.forEach { it.toString().replace("[", "").replace("]", "") shouldBe objNotEmpty.toString() }

    }
}