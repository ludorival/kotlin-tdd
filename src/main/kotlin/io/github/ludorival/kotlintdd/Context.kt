package io.github.ludorival.kotlintdd

import java.util.LinkedList

abstract class Context<A, T, S : Step>(
    val step: S,
    val action: A,
    val result: T,
    val previous: Context<A, *, S>? = null,
    private val root: Context<A, *, S>? = null
) {

    private val isRoot get() = previous == null && this.result == Unit

    @Suppress("UNCHECKED_CAST")
    protected fun <R, C : Context<A, R, S>> chain(
        step: S, result: R, factory: (
            step: S,
            action: A,
            result: R,
            previous: Context<A, *, S>?,
            root: Context<A, *, S>?
        ) -> C
    ): C {
        val root: Context<A, *, S> = this.root ?: this
        val nestedContext: Context<A, *, S>? = if (result.isANestedContext) result as Context<A, *, S> else null
        val previous = when {
            nestedContext != null && nestedContext.root != root -> canNotSupportNestedExceptionWithDifferentRoot()
            nestedContext != null                               -> nestedContext
            isRoot                                              -> null
            else                                                -> this
        }
        return factory(step, action, result, previous, root)
    }


    private fun canNotSupportNestedExceptionWithDifferentRoot(): Nothing = throw IllegalStateException(
        "Cannot accept nested step built without extension function. " +
            "If you want to reuse a collection step, add an extended function like this: \n" +
            "fun ${this::class.qualifiedName?.substringAfter("kotlintdd.")}.sharedSteps() {\n" +
            "\t...\n}"
    )

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

    private val <T> T.isANestedContext get() = this is Context<*, *, *>

    fun hasANestedContext() = result.isANestedContext
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
