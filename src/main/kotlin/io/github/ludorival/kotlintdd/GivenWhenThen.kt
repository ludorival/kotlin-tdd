package io.github.ludorival.kotlintdd

@Suppress("FunctionNaming")
abstract class GivenWhenThen<R1 : Step, R2 : Step, R3 : Step>(
    assumption: R1,
    action: R2,
    assertion: R3
) : BasePattern<R1, R2, R3>(assumption, action, assertion) {
    fun <R> given(block: R1.() -> R) = assume("GIVEN", block)
    fun <R> `when`(block: R2.() -> R) = act("WHEN", block)
}

object SimpleGivenWhenThen :
    GivenWhenThen<Step, Step, Step>(Step(), Step(), Step())

@Suppress("FunctionNaming")
infix fun <T, V, R1 : Step, R2 :
Step, R3 : Step> BasePattern.AssumptionContext<T, R1, R2, R3>.`when`(
    block: R2.(T) -> V
) =
    chainAct("WHEN", block)

infix fun <T, V, R1 : Step, R2 : Step,
    R3 : Step> BasePattern.ActContext<T, R1, R2, R3>.then(block: R3.(T) -> V) =
    chainAssert("THEN", block)

infix fun <T, V, R1 : Step, R2 : Step,
    R3 : Step> BasePattern.AssumptionContext<T, R1, R2, R3>.then(
    block: R3.(T) -> V
) =
    chainAssert("THEN", block)
