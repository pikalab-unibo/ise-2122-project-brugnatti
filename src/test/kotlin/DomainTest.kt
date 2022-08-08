import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import resources.TestUtils.Domains
import resources.TestUtils.actions
import resources.TestUtils.domainEmpty
import resources.TestUtils.domainNotEmpty
import resources.TestUtils.name
import resources.TestUtils.predicates
import resources.TestUtils.types

class DomainTest : AnnotationSpec() {
    @Test
    fun testEmptyCreation() {
        domainEmpty.name.isEmpty() shouldBe true
        domainEmpty.predicates.isEmpty() shouldBe true
        domainEmpty.actions.isEmpty() shouldBe true
        domainEmpty.types.isEmpty() shouldBe true
        domainEmpty.axioms.isEmpty() shouldBe true
    }

    @Test
    fun testNotEmptyCreation() {
        domainNotEmpty.name shouldBe name
        domainNotEmpty.predicates.isEmpty() shouldBe false
        domainNotEmpty.actions.isEmpty() shouldBe false
        domainNotEmpty.types.isEmpty() shouldBe false
        domainNotEmpty.axioms.isEmpty() shouldBe false
    }

    @Test
    fun testDomainObjectWorksAsExpected() {
        Domains.blockWorld.name shouldBe "block_world"
        Domains.blockWorld.predicates.isEmpty() shouldNotBe true
        Domains.blockWorld.actions.isEmpty() shouldBe false
        Domains.blockWorld.types.isEmpty() shouldBe false
        Domains.blockWorld.predicates.forEach { it shouldBeIn predicates }
        Domains.blockWorld.actions.forEach { it shouldBeIn actions }
        Domains.blockWorld.types.forEach { it shouldBeIn types }
    }

}
