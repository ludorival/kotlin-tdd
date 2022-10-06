package io.github.ludorival.kotlintdd

abstract class AssumeActAssert<R1, R2, R3>(
    assumption: R1? = null,
    action: R2? = null,
    assertion: R3? = null
) : BasePattern<R1, R2, R3>(assumption, action, assertion) {
    fun <R> assume(block: R1.() -> R) = assume("ASSUME", block)
    fun <R> act(block: R2.() -> R) = act("ACT", block)
}

infix fun <T, V, R1, R2, R3> BasePattern.AssumptionContext<T, R1, R2, R3>.act(block: R2.(Context<T>) -> V) =
    chainAct("ACT", block)

infix fun <T, V, R1, R2, R3> BasePattern.ActContext<T, R1, R2, R3>.assert(block: R3.(Context<T>) -> V) =
    chainAssert("ASSERT", block)
