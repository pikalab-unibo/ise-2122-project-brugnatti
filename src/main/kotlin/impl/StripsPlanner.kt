package impl

import Action
import Applicable
import Effect
import Fluent
import FluentBasedGoal
import Plan
import Planner
import Problem
import State
import VariableAssignment
import it.unibo.tuprolog.utils.subsequences
import java.util.*

internal class StripsPlanner : Planner {
    override fun plan(problem: Problem): Sequence<Plan> {
        return plan(
            problem.initialState,
            problem.domain.actions,
            problem.goal as FluentBasedGoal
        )
    }

    private fun Stack<Applicable<*>>.apply(substitution: VariableAssignment){
        val i = listIterator()
        while(i.hasNext()){
            val x= i.next()
            i.set(x.apply(substitution))
        }
    }
    data class ChoicePoint(val stack: Stack<Applicable<*>>, val state:State)
    private fun plan(
        initialState: State,
        actions: Set<Action>,
        goal: FluentBasedGoal
    ): Sequence<Plan> = sequence {
        var currentState = initialState
        var stack = Stack<Applicable<*>>().also { it.push(goal) }

        val choicePoints : Deque<ChoicePoint> = LinkedList()

        val plan = mutableListOf<Action>()
        while(true){
            while (stack.isNotEmpty()) {
                val head = stack.peek()
                when {
                    head is Fluent -> {
                        if (currentState.fluents.any { it.match(head) }) {
                            val substitutions=
                                currentState.fluents.filter { it.match(head) }.map { it.mostGeneralUnifier(head) }
                            stack.pop()
                            val substitution= substitutions.first()
                            for (elem in substitutions.subList(1, substitutions.size)) {
                                val stackCopy: Stack<Applicable<*>> = stack.clone() as Stack<Applicable<*>>
                                stackCopy.apply(elem)
                                choicePoints.add(ChoicePoint(stackCopy, currentState))
                            }
                            stack.apply(substitution)
                            //TODO("applica la sostituzione a tutto lo stack")
                        } else {//2
                            val h = Effect.of(head)
                            stack.pop()
                            val actionsMatched=
                                actions.filter{action -> action.positiveEffects.any { effect -> effect.match(h) }}//2.i
                            val action=actionsMatched.first()
                            for(elem in actionsMatched.subList(1, actionsMatched.size)){//2.iii.a
                                val stackCopy: Stack<Applicable<*>> = stack.clone() as Stack<Applicable<*>>
                                stackCopy.push(elem)
                                stackCopy.addAll(elem.preconditions)
                                choicePoints.add(ChoicePoint(stackCopy, currentState))
                            }
                            stack.push(action)//2.ii
                            stack.addAll(action.preconditions) //2.iv

                            val effectsMatched=
                                action.positiveEffects.filter { effect -> effect.match(h) }
                            val effect= effectsMatched.first()
                            for(elem in effectsMatched.subList(1, effectsMatched.size)){
                                val stackCopy: Stack<Applicable<*>> = stack.clone() as Stack<Applicable<*>>
                                stackCopy.apply(h.mostGeneralUnifier(elem))
                                choicePoints.add(ChoicePoint(stackCopy, currentState))
                            }
                            stack.apply(h.mostGeneralUnifier(effect))
                            //TODO("retrieve dell'azione - push dell'azione -push delle precondizioni dell'azione")
                        }
                    }
                    (head is FluentBasedGoal) -> {
                        stack.pop()
                        for (fluent in head.targets)
                            stack.push(fluent)
                    }
                    (head is Action) -> {
                        stack.pop()
                        val states = currentState.apply(head).toList()// necessario perché se no si arrabbia e mi dice la seq può essere iterata solo una volta
                        currentState=states.first()
                        for (elem in states.subList(1, states.size)){
                            choicePoints.add(ChoicePoint(stack, elem))
                        }
                        plan.add(head)
                        //TODO("applicare l'azione a currentState e aggiornarlo")
                    }
                    else -> {
                        break
                    }
                }
            }

            yield(Plan.of(plan))

            if(!choicePoints.isEmpty()) {
                val choicePoint=choicePoints.pollFirst()
                stack = choicePoint.stack
                currentState = choicePoint.state
            }else{
                return@sequence
            }
        }
    }
}
