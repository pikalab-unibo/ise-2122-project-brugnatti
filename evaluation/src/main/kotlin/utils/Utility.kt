import core.Planner
import core.utility.then
import explanation.Explainer
import explanation.Question
import explanation.impl.ExplanationPresenterImpl
import java.lang.management.ManagementFactory

/**
 * Method responsible for measuring the time required to calculate an [Explanation] for a [Question].
 */
fun measureTimeMillis(question: Question, explanationType: String): Long {
    val start = System.currentTimeMillis()
    if (explanationType.startsWith("c") || explanationType.startsWith("C")) {
        ExplanationPresenterImpl(
            Explainer.of(Planner.strips()).explain(question)
        ).presentContrastiveExplanation()
    } else {
        ExplanationPresenterImpl(
            Explainer.of(Planner.strips()).explain(question)
        ).present()
    }
    return System.currentTimeMillis() - start
}

/**
 * Method responsible for measuring the memory (heap) required to calculate an [Explanation] for a [Question].
 */
fun measureMemory(question: Question, explanationType: String): Long {
    val mbean = ManagementFactory.getMemoryMXBean()
    val beforeHeapMemoryUsage = mbean.heapMemoryUsage
    if (explanationType.startsWith("c") || explanationType.startsWith("C")) {
        val instance = ExplanationPresenterImpl(
            Explainer.of(Planner.strips()).explain(question)
        ).presentContrastiveExplanation()
    } else {
        ExplanationPresenterImpl(Explainer.of(Planner.strips()).explain(question)).present()
    }
    val afterHeapMemoryUsage = mbean.heapMemoryUsage
    val result = afterHeapMemoryUsage.used - beforeHeapMemoryUsage.used
    return (result > 0).then(result) ?: 0
}

/**
 * Method responsible for measuring the memory required to calculate an [Explanation] for a [Question].
*/
fun measureMemory2(question: Question, explanationType: String): Long {
    val beforeMemoryUsage = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()

    if (explanationType.startsWith("c") || explanationType.startsWith("C")) {
        val instance = ExplanationPresenterImpl(
            Explainer.of(Planner.strips()).explain(question)
        ).presentContrastiveExplanation()
    } else {
        ExplanationPresenterImpl(
            Explainer.of(Planner.strips()).explain(question)
        ).present()
    }
    val afterMemoryUsage = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()
    return afterMemoryUsage - beforeMemoryUsage
}
