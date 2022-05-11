package impl

import Action
import Axiom
import Domain
import Predicate
import Type

class DomainImpl(
    override val name: String,
    override val predicates: Set<Predicate>,
    override val actions: Set<Action>,
    override val types: Set<Type>,
    override val axioms: Set<Axiom>
) : Domain