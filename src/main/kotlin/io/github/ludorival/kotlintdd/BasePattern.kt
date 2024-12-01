package io.github.ludorival.kotlintdd


open class BasePattern<R1 : Step, R2 : Step, R3 : Step>(
    private val assumption: R1,
    private val action: R2,
    private val assertion: R3
) {

    class AssumptionContext<T, R1 : Step, R2 : Step, R3 : Step> internal constructor(
        private val pattern: BasePattern<R1, R2, R3>,
        key: String,
        result: T

    ) :
        Context<T>(result, key) {


        internal fun <V> chainAct(key: String, block: R2.(T) -> V) =
            chain(ActContext(pattern, key, pattern.actionReceiver(this).block(result)))

        infix fun <V> and(block: R1.(T) -> V): AssumptionContext<V, R1, R2, R3> =
            chain(AssumptionContext(pattern, "AND", pattern.assumptionReceiver(this).block(result)))

        internal fun <V> chainAssert(key: String, block: R3.(T) -> V) =
            chain(AssertContext(pattern, key, pattern.assertionReceiver(this).block(result)))

    }

    class ActContext<T, R1 : Step, R2 : Step, R3 : Step> internal constructor(
        private val pattern: BasePattern<R1, R2, R3>,
        key: String,
        result: T

    ) :
        Context<T>(result, key) {
        infix fun <V> and(block: R2.(T) -> V): ActContext<V, R1, R2, R3> =
            chain(ActContext(pattern, "AND", pattern.actionReceiver(this).block(result)))

        internal fun <V> chainAssert(key: String, block: R3.(T) -> V) =
            chain(AssertContext(pattern, key, pattern.assertionReceiver(this).block(result)))

    }

    class AssertContext<T, R1 : Step, R2 : Step, R3 : Step> internal constructor(
        private val pattern: BasePattern<R1, R2, R3>,
        key: String,
        result: T

    ) :
        Context<T>(result, key) {
        infix fun <V> and(block: R3.(T) -> V): AssertContext<V, R1, R2, R3> =
            chain(AssertContext(pattern, "AND", pattern.assertionReceiver(this).block(result)))

    }

    private fun <T> assumptionReceiver(context: Context<T>): R1 = assumption.apply { with(context) }
    private fun <T> actionReceiver(context: Context<T>): R2 = action.apply { with(context) }
    private fun <T> assertionReceiver(context: Context<T>): R3 = assertion.apply { with(context) }


    fun <V> assume(key: String, block: R1.() -> V): AssumptionContext<V, R1, R2, R3> =
        AssumptionContext(this, key, assumptionReceiver(Context.EMPTY_CONTEXT).block())

    fun <V> act(key: String, block: R2.() -> V): ActContext<V, R1, R2, R3> =
        ActContext(this, key, actionReceiver(Context.EMPTY_CONTEXT).block())

}
