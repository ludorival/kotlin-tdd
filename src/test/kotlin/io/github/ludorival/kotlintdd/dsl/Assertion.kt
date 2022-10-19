package io.github.ludorival.kotlintdd.dsl

import io.github.ludorival.kotlintdd.WithContext
import org.junit.jupiter.api.Assertions

class Assertion : WithContext() {

    infix fun <T> T.`should be equal to`(expected: T) = Assertions.assertEquals(expected, this)

    val `I expect this item is present in my todo list`
        get() = Assertions.assertTrue {
            currentContext.last<TodoList>().items.contains(
                currentContext.last()
            )
        }
}
