package io.github.ludorival.kotlintdd

import io.github.ludorival.kotlintdd.Context as BaseContext
import io.github.ludorival.kotlintdd.Step as BaseStep

interface AssumeActAssert<A> {

    class Context<A, T> internal constructor(
        step: Step,
        action: A,
        result: T,
        previous: BaseContext<A, *, Step>? = null,
        root: BaseContext<A, *, Step>? = null
    ) :
        BaseContext<A, T, Step>(
            step,
            action,
            result,
            previous,
            root
        ) {

        infix fun <R> assume(block: AAAContext<A, T>.() -> R): Context<A, R> =
            chain(Step.ASSUME, this.block(), ::Context)

        infix fun <R> and(block: AAAContext<A, T>.() -> R): Context<A, R> = chain(Step.AND, this.block(), ::Context)

        infix fun <R> act(block: AAAContext<A, T>.() -> R): Context<A, R> = chain(Step.ACT, this.block(), ::Context)

        infix fun assert(block: AAAContext<A, T>.() -> Unit): Context<A, Unit> =
            chain(Step.ASSERT, this.block(), ::Context)

        companion object {
            internal fun <A, R> initialize(step: Step, action: A, block: AAAContext<A, Unit>.() -> R): Context<A, R> =
                Context(step, action, Unit).let { it.chain(step, it.block(), ::Context) }
        }
    }

    enum class Step : BaseStep {
        ASSUME,
        AND,
        ACT,
        ASSERT,

    }

    val action: A

    fun <R> assume(block: AAAContext<A, Unit>.() -> R) = Context.initialize(Step.ASSUME, action, block)

    fun <R> act(block: AAAContext<A, Unit>.() -> R) = Context.initialize(Step.ACT, action, block)
}

typealias AAAContext<A, T> = AssumeActAssert.Context<A, T>
