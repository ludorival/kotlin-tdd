package io.github.ludorival.kotlintdd.dsl

import io.github.ludorival.kotlintdd.Context
import org.junit.jupiter.api.Assertions

class Assertion(val context: Context<*>) {

    infix fun <T> T.`should be equal to`(expected: T) = Assertions.assertEquals(expected, this)

    val `I expect this item is present in my todo list`
        get() = Assertions.assertTrue {
            context.last<TodoList>().items.contains(
                context.last()
            )
        }
}
