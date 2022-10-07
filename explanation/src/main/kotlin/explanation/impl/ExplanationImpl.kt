package explanation.impl

import Action
import FluentBasedGoal
import NotUnifiableException
import Operator
import Plan
import State
import explanation.Explanation
import explanation.Question
import explanation.Simulator
import it.unibo.tuprolog.core.Substitution

/**
 *
 */
data class ExplanationImpl(
    override val originalPlan: Plan,
    override var novelPlan: Plan,
    override val question: Question
) : Explanation {
    override val addList: List<Operator> by lazy {
        this.novelPlan.operators.filter {
            !this.originalPlan.operators.contains(it)
        }
    }
    override val deleteList: List<Operator> by lazy {
        this.originalPlan.operators.filter {
            !this.novelPlan.operators.contains(it)
        }
    }
    override val existingList: List<Operator> by lazy {
        this.originalPlan.operators.filter {
            this.novelPlan.operators.contains(it)
        }
    }
    private val simulator = Simulator.of()

    private infix fun <T> Boolean.then(param: T): T? = if (this) param else null

    private fun retrieveOperator() = novelPlan.operators.filter { it.name.contains("^") }.getOrNull(0)

    private fun retrieveAction() = novelPlan.operators.map { operator ->
        question.problem.domain.actions.first {
            it.name == operator.name.filter { char -> char.isLetter() } &&
                operator.name.contains("^")
        }
    }.first()

    private fun makeFinalOperator(action: Action, operator: Operator): Operator {
        var newOperator = Operator.of(action)
        for (arg in operator.args) {
            newOperator = newOperator.apply(
                VariableAssignment.of(
                    operator.parameters.keys.toList()[operator.args.indexOf(arg)],
                    arg
                )
            )
        }
        return newOperator
    }

    init {
        when (question){
            is Question3 -> {
                val operatorsToKeep = question.plan.operators.subList(0, question.focusOn).toMutableList()
                novelPlan = Plan.of(operatorsToKeep.also { it.add(question.focus) }.also { it.addAll(novelPlan.operators) })
            }
            is Question1, is Question2 -> {
                val operator = retrieveOperator()
                if (operator != null) {
                    val operatorFinal = makeFinalOperator(retrieveAction(), operator)
                    novelPlan = Plan.of(
                        novelPlan.operators.toMutableList()
                            .subList(0, novelPlan.operators.indexOf(operator)).also {
                                it.add(operatorFinal)
                            }.also {
                                it.addAll(
                                    novelPlan.operators.subList(novelPlan.operators.indexOf(operator) + 1, novelPlan.operators.size)
                                )
                            }
                    )
                }
            }
        }
    }


    private fun finalStateComplaintWithGoal(goal: FluentBasedGoal, currentState: State): Boolean {
        var indice = 0
        for (fluent in goal.targets) {
            when (fluent.isGround) {
                true-> ((currentState.fluents.contains(fluent)) then indice++) ?: indice--
                else-> for (fluentState in currentState.fluents) {
                        val tmp = try {
                            fluentState.mostGeneralUnifier(fluent)
                        } catch (_: NotUnifiableException) {
                            null
                        }
                        // TODO(Se questa roba è sensata va fixata anche nell'altro modulo)
                        if (tmp != Substitution.empty() && tmp != Substitution.empty() && tmp != null) {
                            indice++
                            break
                        }
                    }
            }
        }
        return goal.targets.size == indice
    }

    override fun isPlanValid(): Boolean {
        val states = simulator.simulate(novelPlan, question.problem.initialState)
        return (states.isNotEmpty() then
                states.all { finalStateComplaintWithGoal(question.problem.goal as FluentBasedGoal, it) }) ?: false
    }

    override fun toString(): String =
        """${ExplanationImpl::class.simpleName}(
            |  ${ExplanationImpl::originalPlan.name}=${this.originalPlan},
            |  ${ExplanationImpl::novelPlan.name}=${this.novelPlan},
            |  the novel plan is valid: ${this.isPlanValid()},
            |  - Diff(original plan VS new plan):
            |  ${ExplanationImpl::addList.name}=$addList,
            |  ${ExplanationImpl::deleteList.name}=$deleteList,
            |  ${ExplanationImpl::existingList.name}=$existingList
            |)
        """.trimMargin()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ExplanationImpl

        if (this.originalPlan != other.originalPlan) return false
        if (this.novelPlan != other.novelPlan) return false

        return true
    }

    // TODO(Ma è necessario l'override dell'hashcode)
    override fun hashCode(): Int {
        var result = this.originalPlan.hashCode()
        result = 31 * result + this.novelPlan.hashCode()
        return result
    }
}
