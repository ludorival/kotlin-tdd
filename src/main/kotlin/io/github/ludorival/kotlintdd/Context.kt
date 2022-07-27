package io.github.ludorival.kotlintdd

import java.util.LinkedList

abstract class Context<T>(
    val result: T,
    val key: String
) {

    private var _previous: Context<*>? = null
    val previous get() = _previous
    private var head: Context<*>? = null
    private var tail: Context<*>? = null
    private val isRoot get() = previous == null && this.result == Unit

    internal fun <V, C : Context<V>> chain(context: C): C {
        val resolvedRoot = this.head ?: this
        val that = this
        val resolvedPrevious: Context<*>? = when {
            context.result is Context<*> -> context.result.run {
                this.head?._previous = that
                this.head = resolvedRoot
                this
            }
            result is Context<*>         -> result
            isRoot                       -> null
            else                         -> that
        }
        return context.apply {
            this.head = resolvedRoot
            this._previous = resolvedPrevious
        }
    }


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

    private val <T> T.isANestedContext get() = this is Context<*>

    fun hasANestedContext() = result.isANestedContext
    fun hasASupportedResult() = result != null && result != Unit && !hasANestedContext()

    fun anyResults() = anyReversedResults().asReversed()

    inline fun <reified T> results() = reversedResults<T>().asReversed()

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> forEach(action: (it: Context<T>) -> Unit) {
        var loop: Context<*>? = this
        while (loop != null) {
            if (loop.result is T && !loop.hasANestedContext()) {
                action(loop as Context<T>)
            }
            loop = loop.previous
        }
    }

    inline fun <reified T> lastOrNull(predicate: (it: T) -> Boolean = { true }): T? {
        var loop: Context<*>? = this
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
            list.add("${it.key} -> ${it.result.display()}")
        }
        return list.asReversed().joinToString("\n")
    }

    companion object {
        val EMPTY_CONTEXT = object : Context<Unit>(Unit, "") {}
    }
}

