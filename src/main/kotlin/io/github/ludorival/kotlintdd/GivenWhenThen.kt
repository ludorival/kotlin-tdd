package io.github.ludorival.kotlintdd

abstract class GivenWhenThen<R1 : WithContext, R2 : WithContext, R3 : WithContext>(
    assumption: R1,
    action: R2,
    assertion: R3
) : BasePattern<R1, R2, R3>(assumption, action, assertion) {
    fun <R> given(block: R1.() -> R) = assume("GIVEN", block)
    fun <R> `when`(block: R2.() -> R) = act("WHEN", block)
}

object SimpleGivenWhenThen :
    GivenWhenThen<WithContext, WithContext, WithContext>(WithContext(), WithContext(), WithContext())

infix fun <T, V, R1 : WithContext, R2 : WithContext, R3 : WithContext> BasePattern.AssumptionContext<T, R1, R2, R3>.`when`(
    block: R2.(Context<T>) -> V
) =
    chainAct("WHEN", block)

infix fun <T, V, R1 : WithContext, R2 : WithContext, R3 : WithContext> BasePattern.ActContext<T, R1, R2, R3>.then(block: R3.(Context<T>) -> V) =
    chainAssert("THEN", block)

infix fun <T, V, R1 : WithContext, R2 : WithContext, R3 : WithContext> BasePattern.AssumptionContext<T, R1, R2, R3>.then(
    block: R3.(Context<T>) -> V
) =
    chainAssert("THEN", block)
