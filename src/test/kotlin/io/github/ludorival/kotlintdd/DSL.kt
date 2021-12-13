package io.github.ludorival.kotlintdd

import org.junit.jupiter.api.Assertions

typealias ActionState = Context<Action, *, *>

fun `the number`(value: Int) = value

val ActionState.`I perform their sum` get() = action.sum(results())

fun ActionState.`I expect the result is`(expected: Int) = Assertions.assertEquals(expected, result)
