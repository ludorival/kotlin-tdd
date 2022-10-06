package io.github.ludorival.kotlintdd

abstract class GivenWhenThen<R1, R2, R3>(
    assumption: R1? = null,
    action: R2? = null,
    assertion: R3? = null
) : BasePattern<R1, R2, R3>(assumption, action, assertion) {
    fun <R> given(block: R1.() -> R) = assume("GIVEN", block)
    fun <R> `when`(block: R2.() -> R) = act("WHEN", block)
}

infix fun <T, V, R1, R2, R3> BasePattern.AssumptionContext<T, R1, R2, R3>.`when`(block: R2.(Context<T>) -> V) =
    chainAct("WHEN", block)

infix fun <T, V, R1, R2, R3> BasePattern.ActContext<T, R1, R2, R3>.then(block: R3.(Context<T>) -> V) =
    chainAssert("THEN", block)

infix fun <T, V, R1, R2, R3> BasePattern.AssumptionContext<T, R1, R2, R3>.then(block: R3.(Context<T>) -> V) =
    chainAssert("THEN", block)
