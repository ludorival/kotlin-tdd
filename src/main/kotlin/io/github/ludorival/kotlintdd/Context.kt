package io.github.ludorival.kotlintdd

open class Context<T>(
    val result: T,
    val key: String
) {

    private var _previous: Context<*>? = null
    val previous get() = _previous
    private var head: Context<*>? = null
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

    private val <T> T.isANestedContext get() = this is Context<*>

    fun hasANestedContext() = result.isANestedContext
    fun hasASupportedResult() = result != null && result != Unit && !hasANestedContext()


    companion object {
        val EMPTY_CONTEXT = Context(Unit, "")
    }
}

