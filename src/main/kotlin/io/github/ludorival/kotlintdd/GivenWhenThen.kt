package io.github.ludorival.kotlintdd

import io.github.ludorival.kotlintdd.Context as BaseContext
import io.github.ludorival.kotlintdd.Step as BaseStep

interface GivenWhenThen<A> {

    class Context<A, T> internal constructor(
        step: Step,
        action: A,
        result: T,
        previous: BaseContext<A, *, Step>? = null
    ) :
        BaseContext<A, T, Step>(
            step,
            action,
            result,
            previous
        ) {
        @Suppress("UNCHECKED_CAST")
        override fun <R, C : BaseContext<A, R, Step>> createState(
            step: Step,
            result: R,
            previous: BaseContext<A, *, Step>?
        ): C = Context(step, action, result, previous) as C

        infix fun <R> and(block: GWTContext<A, T>.() -> R): Context<A, R> = chain(Step.AND, block)

        infix fun <R> `when`(block: GWTContext<A, T>.() -> R): Context<A, R> = chain(Step.WHEN, block)

        infix fun then(block: GWTContext<A, T>.() -> Unit): Context<A, Unit> = chain(Step.THEN, block)

    }

    enum class Step : BaseStep {
        GIVEN,
        AND,
        WHEN,
        THEN
    }

    val action: A

    private fun <R> initialize(step: Step, block: BaseContext<A, Unit, Step>.() -> R): Context<A, R> =
        Context(step, action, Unit).initialize(block)

    fun <R> given(block: GWTContext<A, Unit>.() -> R) = initialize(Step.GIVEN, block)

    fun <R> `when`(block: GWTContext<A, Unit>.() -> R) = initialize(Step.WHEN, block)

}

typealias GWTContext<A, T> = BaseContext<A, T, GivenWhenThen.Step>
