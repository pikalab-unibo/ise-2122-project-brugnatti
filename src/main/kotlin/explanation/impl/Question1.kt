package explanation.impl

import Domain
import Operator
import Plan
import Problem
import explanation.Question

class Question1(override val problem: Problem, override val plan: Plan, override val focus: Operator) : Question, AbstractQuestion() {
    // 1.
    override var newPredicate = createNewPredicate(focus, "has_done_")

    override var newGroundFluent = createNewGroundFluent(focus, newPredicate)
    override var newFluent = createNewFluent(focus, newPredicate)

    // 2.
    override var oldAction =
        findAction(focus, problem.domain.actions)

    // 3.
    override var newAction = createNewAction(oldAction, newFluent)

    // 4.
    override var hDomain = buildHdomain()

    override fun buildHdomain(): Domain = buildHdomain(problem.domain, newPredicate, newAction)

    override fun buildHproblem(): Problem = buildHproblem(hDomain, problem, newGroundFluent, null)
}
