package io.github.ludorival.kotlintdd


infix fun <T, V, R1, R2, R3> BasePattern.AssumptionContext<T, R1, R2, R3>.act(block: R2.(Context<T>) -> V) =
    chainAct("ACT", block)

infix fun <T, V, R1, R2, R3> BasePattern.ActContext<T, R1, R2, R3>.assert(block: R3.(Context<T>) -> V) =
    chainAssert("ASSERT", block)
