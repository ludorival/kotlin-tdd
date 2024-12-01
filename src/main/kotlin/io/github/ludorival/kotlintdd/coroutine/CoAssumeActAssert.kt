package io.github.ludorival.kotlintdd.coroutine

import io.github.ludorival.kotlintdd.Step

abstract class CoAssumeActAssert<R1 : Step, R2 : Step, R3 : Step>(
    assumption: R1, action: R2, assertion: R3
) : CoBasePattern<R1, R2, R3>(assumption, action, assertion) {
    suspend fun <R> assume(block: suspend R1.() -> R) = assume("ASSUME", block)
    suspend fun <R> act(block: suspend R2.() -> R) = act("ACT", block)
}

object CoSimpleAssumeActAssert :
    CoAssumeActAssert<Step, Step, Step>(Step(), Step(), Step())

suspend infix fun <T, V,
    R1 : Step,
    R2 : Step,
    R3 : Step> CoBasePattern.AssumptionContext<T, R1, R2, R3>.act(
    block: suspend R2.(T) -> V
) = chainAct("ACT", block)

suspend infix fun <T, V,
    R1 : Step,
    R2 : Step,
    R3 : Step> CoBasePattern.ActContext<T, R1, R2, R3>.assert(
    block: suspend R3.(T) -> V
) = chainAssert("ASSERT", block)
