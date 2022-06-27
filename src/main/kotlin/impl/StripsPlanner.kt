package impl

import Action
import Effect
import Fluent
import FluentBasedGoal
import Plan
import Planner
import Problem
import State
import VariableAssignment
import java.util.*

internal class StripsPlanner : Planner {
    override fun plan(problem: Problem): Sequence<Plan> {
        return plan(
            problem.initialState,
            problem.domain.actions,
            problem.goal as FluentBasedGoal
        )
    }

    private fun plan(
        initialState: State,
        actions: Set<Action>,
        goal: FluentBasedGoal
    ): Sequence<Plan> = sequence {
        var currentState = initialState
        val stack = Stack<Any>().also { it.push(goal) }
        val plan = mutableListOf<Action>()
        while (stack.isNotEmpty()) {
            val head = stack.peek()
            if (head is Fluent){
                stack.pop()//Aggiunto IO
                if(currentState.fluents.any { it.match(head) }) {
                    val substitution=currentState.fluents.filter{it.match(head)}.map{it.mostGeneralUnifier(head)}
                for (elem in stack) {
                    if (elem is Fluent)
                        elem.apply(substitution[0])
                    else if (elem is Action)
                        elem.apply(substitution[0])
                }

                //TODO("applica la sostituzione a tutto lo stack")
            } } else if (head is Effect && actions.any { a -> a.positiveEffects.any { it.match(head) } }) {
                stack.pop()
                actions.forEach { a ->
                    a.positiveEffects.forEach {
                        if (it.match(head)) {
                            stack.push(a)
                            a.preconditions.forEach { p -> stack.push(p) }
                            if (it.match(head)) {
                                stack.push(it)
                            }
                        }
                        for (elem in stack)
                            if (elem is FluentBasedGoal)
                                elem.apply(it.mostGeneralUnifier(head))
                    }
                    /*TODO("retrieve dell'azione")
                    TODO("push dell'azione")
                    TODO("push delle precondizioni dell'azione")

                     */
                }
            } else if (head is FluentBasedGoal) {//non entra sebbene la testa dovrebbe essere un fluentBasedGoalImpl
                stack.pop()
                for (fluent in head.targets) {
                    stack.push(fluent)
                }
            } else if (head is Action) {
                stack.pop()//aggiunto IO
                currentState = currentState.apply(head).toList()[0]
                plan.add(head)
                //TODO("applicare l'azione a currentState e aggiornarlo")
            } else {
                // do nothing
            }
        }
        yield(Plan.of(plan))
    }
}
