package io.github.ludorival.kotlintdd.coroutine

import io.github.ludorival.kotlintdd.Step

@Suppress("FunctionNaming")
abstract class CoGivenWhenThen<R1 : Step, R2 : Step, R3 : Step>(
    assumption: R1,
    action: R2,
    assertion: R3
) : CoBasePattern<R1, R2, R3>(assumption, action, assertion) {
    suspend fun <R> given(block: suspend R1.() -> R) = assume("GIVEN", block)
    suspend fun <R> `when`(block: suspend R2.() -> R) = act("WHEN", block)
}

object CoSimpleGivenWhenThen :
    CoGivenWhenThen<Step, Step, Step>(Step(), Step(), Step())

@Suppress("FunctionNaming")
suspend infix fun <T, V, R1 : Step, R2 :
Step, R3 : Step> CoBasePattern.AssumptionContext<T, R1, R2, R3>.`when`(
    block: suspend R2.(T) -> V
) =
    chainAct("WHEN", block)

suspend infix fun <T, V, R1 : Step, R2 : Step,
    R3 : Step> CoBasePattern.ActContext<T, R1, R2, R3>.then(block: suspend R3.(T) -> V) =
    chainAssert("THEN", block)

suspend infix fun <T, V, R1 : Step, R2 : Step,
    R3 : Step> CoBasePattern.AssumptionContext<T, R1, R2, R3>.then(
    block: suspend R3.(T) -> V
) =
    chainAssert("THEN", block)
