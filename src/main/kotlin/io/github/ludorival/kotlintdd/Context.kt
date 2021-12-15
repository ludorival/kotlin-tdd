package io.github.ludorival.kotlintdd

import java.util.LinkedList

abstract class Context<A, T, S : Step>(
    val step: S,
    val action: A,
    val result: T,
    val previous: Context<A, *, S>? = null
) {

    protected fun <R, C : Context<A, R, S>> chain(step: S, result: R): C {
        return createState(step, result, previous = getPreviousFrom(result))
    }

    internal fun <R, C : Context<A, R, S>> initialize(result: R): C {
        return createState(step, result, previous = getPreviousFrom(result, false))
    }


    @Suppress("UNCHECKED_CAST")
    private fun <R> getPreviousFrom(result: R, chainWithThis: Boolean = true): Context<A, *, S>? {
        var previous: Context<A, *, S>? = if (chainWithThis) this else null
        if (result is Context<*, *, *>) {
            val list = LinkedList<Context<A, *, S>>()
            (result as Context<A, *, S>).forEach<Any> {
                list.add(it)
            }
            if (chainWithThis)
                list.add(this)
            previous = list.reduceRight { state, acc -> createState(state.step, state.result, acc) }
        }
        return previous
    }

    protected abstract fun <R, C : Context<A, R, S>> createState(
        step: S,
        result: R,
        previous: Context<A, *, S>?
    ): C


    fun anyReversedResults(): List<Any> {
        return reversedResults()
    }

    inline fun <reified T> reversedResults(): List<T> {
        val list = LinkedList<T>()
        forEach<T> {
            if (it.hasASupportedResult())
                list.add(it.result)
        }
        return list
    }

    fun hasANestedContext() = result is Context<*, *, *>
    fun hasASupportedResult() = result != null && result != Unit && !hasANestedContext()

    fun anyResults() = anyReversedResults().asReversed()

    inline fun <reified T> results() = reversedResults<T>().asReversed()

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> forEach(action: (it: Context<A, T, S>) -> Unit) {
        var loop: Context<A, *, S>? = this
        while (loop != null) {
            if (loop.result is T && !loop.hasANestedContext()) {
                action(loop as Context<A, T, S>)
            }
            loop = loop.previous
        }
    }

    inline fun <reified T> lastOrNull(predicate: (it: T) -> Boolean = { true }): T? {
        var loop: Context<A, *, S>? = this
        while (loop != null) {
            if (loop.result is T && !loop.hasANestedContext() && predicate(loop.result as T)) {
                return loop.result as T
            }
            loop = loop.previous
        }
        return null
    }

    inline fun <reified T> last(predicate: (it: T) -> Boolean = { true }): T = lastOrNull(predicate)!!

    inline fun <reified T> firstOrNull(predicate: (it: T) -> Boolean = { true }) =
        reversedResults<T>().lastOrNull(predicate)

    inline fun <reified T> first(predicate: (it: T) -> Boolean = { true }) = firstOrNull(predicate)!!

    inline fun <reified T> get(index: Int) = anyReversedResults().let { it[it.size - 1 - index] } as T

    private fun Any?.display() = if (this is Unit) "*Something*" else toString()

    override fun toString(): String {
        val list = LinkedList<String>()
        forEach<Any> {
            list.add("${it.step} -> ${it.result.display()}")
        }
        return list.asReversed().joinToString("\n")
    }
}
