package io.github.ludorival.kotlintdd


abstract class BasePattern<R1, R2, R3>(
    private val assumption: R1? = null,
    private val action: R2? = null,
    private val assertion: R3? = null
) {

    class AssumptionContext<T, R1, R2, R3> internal constructor(
        private val pattern: BasePattern<R1, R2, R3>,
        key: String,
        result: T

    ) :
        Context<T>(result, key) {


        internal fun <V> chainAct(key: String, block: R2.(Context<T>) -> V) =
            chain(ActContext(pattern, key, pattern.actionReceiver(this).block(this)))

        infix fun <V> and(block: R1.(Context<T>) -> V): AssumptionContext<V, R1, R2, R3> =
            chain(AssumptionContext(pattern, "AND", pattern.assumptionReceiver(this).block(this)))

        internal fun <V> chainAssert(key: String, block: R3.(Context<T>) -> V) =
            chain(AssertContext(pattern, key, pattern.assertionReceiver(this).block(this)))

    }

    class ActContext<T, R1, R2, R3> internal constructor(
        private val pattern: BasePattern<R1, R2, R3>,
        key: String,
        result: T

    ) :
        Context<T>(result, key) {
        infix fun <V> and(block: R2.(Context<T>) -> V): ActContext<V, R1, R2, R3> =
            chain(ActContext(pattern, "AND", pattern.actionReceiver(this).block(this)))

        internal fun <V> chainAssert(key: String, block: R3.(Context<T>) -> V) =
            chain(AssertContext(pattern, key, pattern.assertionReceiver(this).block(this)))

    }

    class AssertContext<T, R1, R2, R3> internal constructor(
        private val pattern: BasePattern<R1, R2, R3>,
        key: String,
        result: T

    ) :
        Context<T>(result, key) {
        infix fun <V> and(block: R3.(Context<T>) -> V): AssertContext<V, R1, R2, R3> =
            chain(AssertContext(pattern, "AND", pattern.assertionReceiver(this).block(this)))

    }

    open fun assumptionReceiver(context: Context<*>): R1 = assumption ?: error("Expect a valid assumption object")
    open fun actionReceiver(context: Context<*>): R2 = action ?: error("Expect a valid action object")
    open fun assertionReceiver(context: Context<*>): R3 = assertion ?: error("Expect a valid assertion object")


    fun <V> assume(key: String, block: R1.() -> V): AssumptionContext<V, R1, R2, R3> =
        AssumptionContext(this, key, assumptionReceiver(Context.EMPTY_CONTEXT).block())

    fun <V> act(key: String, block: R2.() -> V): ActContext<V, R1, R2, R3> =
        ActContext(this, key, actionReceiver(Context.EMPTY_CONTEXT).block())

}
