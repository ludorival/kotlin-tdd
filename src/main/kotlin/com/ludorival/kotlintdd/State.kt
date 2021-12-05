package com.ludorival.kotlintdd

import java.util.LinkedList

class State<A, T>(val action: A, val result: T, val previous: State<A, *>? = null) {

    private fun <R> chain(block: State<A, T>.() -> R): State<A, R> =
        State(action, block(), previous = this)

    infix fun <R> and(block: State<A, T>.() -> R) = chain(block)

    //#region Given When Then pattern
    infix fun <R> `when`(block: State<A, T>.() -> R) = chain(block)

    infix fun then(block: State<A, T>.() -> Unit) = chain(block)
    //#endregion

    //#region Assume Act Assert pattern
    infix fun <R> act(block: State<A, T>.() -> R) = chain(block)

    infix fun assert(block: State<A, T>.() -> Unit) = chain(block)
    //#endregion

    fun anyReversedResults(): List<Any> {
        val list = LinkedList<Any>()
        var loop: State<A, *>? = this
        while (loop != null) {
            if (loop.result != null && loop.result != Unit)
                list.add(loop.result!!)
            loop = loop.previous
        }
        return list
    }

    inline fun <reified T> reversedResults(): List<T> {
        val list = LinkedList<T>()
        var loop: State<A, *>? = this
        while (loop != null) {
            if (loop.result != null && loop.result is T)
                list.add(loop.result as T)
            loop = loop.previous
        }
        return list
    }

    fun anyResults() = anyReversedResults().asReversed()

    inline fun <reified T> results() = reversedResults<T>().asReversed()

    inline fun <reified T> lastOrNull(predicate: T.() -> Boolean = { true }): T? {
        var loop: State<A, *>? = this
        while (loop != null) {
            val result: T? = if (loop.result is T) loop.result as T else null
            if (result?.predicate() == true)
                return result
            loop = loop.previous
        }
        return null
    }

    inline fun <reified T> last(predicate: T.() -> Boolean = { true }): T = lastOrNull(predicate)!!

    inline fun <reified T> firstOrNull(predicate: T.() -> Boolean = { true }) =
        reversedResults<T>().last { it.predicate() }

    inline fun <reified T> first(predicate: T.() -> Boolean = { true }) = firstOrNull(predicate)!!

    inline fun <reified T> get(index: Int) = anyReversedResults().let { it[it.size - 1 - index] } as T
}
