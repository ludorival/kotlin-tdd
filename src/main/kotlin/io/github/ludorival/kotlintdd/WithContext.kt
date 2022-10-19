package io.github.ludorival.kotlintdd


open class WithContext {
    private lateinit var context: Context<*>

    val currentContext get() = context

    internal fun with(context: Context<*>) {
        this.context = context
    }
}
