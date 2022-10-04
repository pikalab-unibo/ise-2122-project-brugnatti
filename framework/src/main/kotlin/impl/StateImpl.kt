package impl

import Action
import Fluent
import Operator
import State
import VariableAssignment
import impl.res.toPddl
import impl.res.toTerm
import it.unibo.tuprolog.core.Fact
import it.unibo.tuprolog.core.Struct
import it.unibo.tuprolog.core.Tuple
import it.unibo.tuprolog.solve.Solution
import it.unibo.tuprolog.solve.Solver
import it.unibo.tuprolog.solve.flags.FlagStore
import it.unibo.tuprolog.solve.flags.Unknown
import it.unibo.tuprolog.theory.Theory

internal data class StateImpl(override val fluents: Set<Fluent>) : State {

    init {
        require(fluents.all { it.isGround }) {
            "States cannot contain non-ground fluents, while the following fluents were provided: $fluents"
        }
    }

    override fun apply(action: Operator): Sequence<State> =
        mguForActionPreconditions(action).map { action.apply(it) }.map {
            val (addList, removeList) = it.getAddAndRemoveLists()
            val fluents = (fluents - removeList) + addList
            State.of(fluents)
        }

    fun apply(substitution: VariableAssignment): State =
        copy(fluents = fluents.map { it.apply(substitution) }.toSet())

    override fun isApplicable(action: Action): Boolean =
        action.preconditions.all { precondition ->
            fluents.any { precondition.match(it) }
        }

    /**
     * Convert the current state into a Prolog theory where each fluent is a fact
     * Convert the current action into a Prolog goal where each precondition is a sub-goal
     * Then create a Prolog solver out of the aforementioned theory ...
     * ... and query the solver with the aforementioned query ...
     * ... then consider only positive answers ...
     * ... and finally convert each answer's substitution into a VariableAssignment
     */
    private fun mguForActionPreconditions(action: Action): Sequence<VariableAssignment> {
        val stateAsTheory = Theory.of(fluents.map { it.toTerm() }.map { Fact.of(it) })
        val preconditionsAsQuery = Tuple.wrapIfNeeded(action.preconditions.map { it.toTerm() }) as Struct
        return Solver.prolog.solverOf(
            staticKb = stateAsTheory,
            flags = FlagStore.DEFAULT + (Unknown.name to Unknown.FAIL)
        ).solve(preconditionsAsQuery)
            .filterIsInstance<Solution.Yes>()
            .map { it.substitution.toPddl() }
    }

    private fun Action.getAddAndRemoveLists(): Pair<Set<Fluent>, Set<Fluent>> {
        val addList: MutableSet<Fluent> = mutableSetOf()
        val removeList: MutableSet<Fluent> = mutableSetOf()
        for (effect in effects) {
            if (effect.isPositive) {
                addList.add(effect.fluent)
            } else {
                removeList.add(effect.fluent)
            }
        }
        return addList to removeList
    }

    override fun toString(): String =
        fluents.map { it.toString() }.sorted().joinToString(", ", "state(", ")")
}