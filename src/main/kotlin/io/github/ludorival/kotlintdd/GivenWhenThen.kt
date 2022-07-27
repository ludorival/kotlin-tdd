package io.github.ludorival.kotlintdd

infix fun <T, V, R1, R2, R3> BasePattern.AssumptionContext<T, R1, R2, R3>.`when`(block: R2.(Context<T>) -> V) =
    chainAct("WHEN", block)

infix fun <T, V, R1, R2, R3> BasePattern.ActContext<T, R1, R2, R3>.then(block: R3.(Context<T>) -> V) =
    chainAssert("THEN", block)

infix fun <T, V, R1, R2, R3> BasePattern.AssumptionContext<T, R1, R2, R3>.then(block: R3.(Context<T>) -> V) =
    chainAssert("THEN", block)
