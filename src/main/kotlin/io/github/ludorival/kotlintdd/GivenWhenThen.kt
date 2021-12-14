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

        infix fun <R> given(block: GWTContext<A, Unit>.() -> R): Context<A, R> = initialize(Step.GIVEN, action, block)

        infix fun <R> and(block: GWTContext<A, T>.() -> R): Context<A, R> = chain(Step.AND, block())

        infix fun <R> `when`(block: GWTContext<A, T>.() -> R): Context<A, R> = chain(Step.WHEN, block())

        infix fun then(block: GWTContext<A, T>.() -> Unit): Context<A, Unit> = chain(Step.THEN, block())

        companion object {
            internal fun <A, R> initialize(step: Step, action: A, block: GWTContext<A, Unit>.() -> R): Context<A, R> =
                Context(step, action, Unit).let { it.initialize(it.block()) }
        }
    }

    enum class Step : BaseStep {
        GIVEN,
        AND,
        WHEN,
        THEN
    }

    val action: A

    fun <R> given(block: GWTContext<A, Unit>.() -> R) = Context.initialize(Step.GIVEN, action, block)

    fun <R> `when`(block: GWTContext<A, Unit>.() -> R) = Context.initialize(Step.WHEN, action, block)

}

typealias GWTContext<A, T> = GivenWhenThen.Context<A, T>
