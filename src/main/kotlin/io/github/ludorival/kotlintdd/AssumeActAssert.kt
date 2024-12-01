package io.github.ludorival.kotlintdd

abstract class AssumeActAssert<R1 : Step, R2 : Step, R3 : Step>(
    assumption: R1,
    action: R2,
    assertion: R3
) : BasePattern<R1, R2, R3>(assumption, action, assertion) {
    fun <R> assume(block: R1.() -> R) = assume("ASSUME", block)
    fun <R> act(block: R2.() -> R) = act("ACT", block)
}

object SimpleAssumeActAssert :
    AssumeActAssert<Step, Step, Step>(Step(), Step(), Step())

infix fun <T, V, R1 : Step, R2 : Step, R3 : Step> BasePattern.AssumptionContext<T, R1, R2, R3>.act(
    block: R2.(T) -> V
) =
    chainAct("ACT", block)

infix fun <T, V, R1 : Step, R2 : Step, R3 : Step> BasePattern.ActContext<T, R1, R2, R3>.assert(
    block: R3.(T) -> V
) =
    chainAssert("ASSERT", block)
