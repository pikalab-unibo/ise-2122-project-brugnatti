package explanation

import Plan
import State
import explanation.impl.SimulatorImpl

/**
 * A [Simulator] able to simulate the execution of a plan.
 */
interface Simulator {
    /**
     * Method used to simulate the execution of a plan.
     */
    fun simulate(plan: Plan, state: State): List<State>

    companion object{
        /**
         * Factory method for a [Simulator] creation.
         */
        fun of() : Simulator = SimulatorImpl()

    }
}