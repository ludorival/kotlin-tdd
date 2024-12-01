package io.github.ludorival.kotlintdd.coroutine

import io.github.ludorival.kotlintdd.Context
import io.github.ludorival.kotlintdd.Step


open class CoBasePattern<R1 : Step, R2 : Step, R3 : Step>(
    private val assumption: R1,
    private val action: R2,
    private val assertion: R3
) {

    class AssumptionContext<T, R1 : Step, R2 : Step, R3 : Step> internal constructor(
        private val pattern: CoBasePattern<R1, R2, R3>,
        key: String,
        result: T

    ) :
        Context<T>(result, key) {

        internal suspend fun <V> chainAct(key: String, block: suspend R2.(T) -> V) =
            chain(ActContext(pattern, key, pattern.actionReceiver(this).block(result)))

        suspend infix fun <V> and(block: suspend R1.(T) -> V): AssumptionContext<V, R1, R2, R3> =
            chain(AssumptionContext(pattern, "AND", pattern.assumptionReceiver(this).block(result)))

        internal suspend fun <V> chainAssert(key: String, block: suspend R3.(T) -> V) =
            chain(AssertContext(pattern, key, pattern.assertionReceiver(this).block(result)))

    }

    class ActContext<T, R1 : Step, R2 : Step, R3 : Step> internal constructor(
        private val pattern: CoBasePattern<R1, R2, R3>,
        key: String,
        result: T

    ) :
        Context<T>(result, key) {
        suspend infix fun <V> and(block: suspend R2.(T) -> V): ActContext<V, R1, R2, R3> =
            chain(ActContext(pattern, "AND", pattern.actionReceiver(this).block(result)))

        internal suspend fun <V> chainAssert(key: String, block: suspend R3.(T) -> V) =
            chain(AssertContext(pattern, key, pattern.assertionReceiver(this).block(result)))

    }

    class AssertContext<T, R1 : Step, R2 : Step, R3 : Step> internal constructor(
        private val pattern: CoBasePattern<R1, R2, R3>,
        key: String,
        result: T

    ) :
        Context<T>(result, key) {
        suspend infix fun <V> and(block: suspend R3.(T) -> V): AssertContext<V, R1, R2, R3> =
            chain(AssertContext(pattern, "AND", pattern.assertionReceiver(this).block(result)))

    }

    private fun <T> assumptionReceiver(context: Context<T>): R1 = assumption.apply { with(context) }
    private fun <T> actionReceiver(context: Context<T>): R2 = action.apply { with(context) }
    private fun <T> assertionReceiver(context: Context<T>): R3 = assertion.apply { with(context) }


    suspend fun <V> assume(key: String, block: suspend R1.() -> V): AssumptionContext<V, R1, R2, R3> =
        AssumptionContext(this, key, assumptionReceiver(Context.EMPTY_CONTEXT).block())

    suspend fun <V> act(key: String, block: suspend R2.() -> V): ActContext<V, R1, R2, R3> =
        ActContext(this, key, actionReceiver(Context.EMPTY_CONTEXT).block())

}
