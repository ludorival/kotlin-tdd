package io.github.ludorival.kotlintdd.aaa

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class AssumeActAssertTest {


    @Test
    fun `I can use simple assume act assert pattern`() {
        assume {
            1
        } act {
            action.sum(result, 2)
        } assert {
            assertEquals(3, result)
        } and {
            assertEquals(
                """
                ASSUME -> 1
                ACT -> 3
                ASSERT -> *Something*""".trimIndent(), this.toString()
            )
        }
    }

    @Test
    fun `I can use assume act assert pattern with and`() {
        assume {
            1
        } and {
            2
        } act {
            action.sum(first(), last())
        } assert {
            assertEquals(3, result)
        } and {
            assertEquals(
                """
                ASSUME -> 1
                AND -> 2
                ACT -> 3
                ASSERT -> *Something*""".trimIndent(), this.toString()
            )
        }
    }


    @Test
    fun `I can use directly the act instead of the assume`() {
        act {
            action.sum(1, 2)
        } assert {
            assertEquals(result, 3)
        } and {
            assertEquals(
                """
                ACT -> 3
                ASSERT -> *Something*""".trimIndent(), this.toString()
            )
        }
    }


    @Test
    fun `I can support nested steps from the ASSUME`() {
        assume {
            someUseCase()
        } and {
            4
        } act {
            action.sum(results())
        } assert {
            assertEquals(result, 1 + 2 + 3 + 4)
        } and {
            assertEquals(
                """
                ASSUME -> 1
                AND -> 2
                AND -> 3
                AND -> 4
                ACT -> 10
                ASSERT -> *Something*""".trimIndent(), this.toString()
            )
        }
    }

    @Test
    fun `I can support nested steps after the ASSUME`() {
        assume {
            4
        } and {
            someUseCase()
        } act {
            action.sum(results())
        } assert {
            assertEquals(result, 4 + 1 + 2 + 3)
        } and {
            assertEquals(
                """
                ASSUME -> 4
                ASSUME -> 1
                AND -> 2
                AND -> 3
                ACT -> 10
                ASSERT -> *Something*""".trimIndent(), this.toString()
            )
        }
    }

    @Test
    fun `I can support multiple nested steps`() {
        assume {
            4
        } and {
            someNestedUseCase()
        } act {
            action.sum(results())
        } assert {
            assertEquals(4 + 5 + 1 + 2 + 3 + 6, result)
        } and {
            assertEquals(
                """
                ASSUME -> 4
                ASSUME -> 5
                ASSUME -> 1
                AND -> 2
                AND -> 3
                AND -> 6
                ACT -> 21
                ASSERT -> *Something*""".trimIndent(), this.toString()
            )
        }
    }

    @Test
    fun `I can support nested steps by using extension after the ASSUME`() {
        assume {
            4
        } and {
            someUseCase()
        } act {
            action.sum(results())
        } assert {
            assertEquals(result, 4 + 1 + 2 + 3)
        } and {
            assertEquals(
                """
                ASSUME -> 4
                ASSUME -> 1
                AND -> 2
                AND -> 3
                ACT -> 10
                ASSERT -> *Something*""".trimIndent(), this.toString()
            )
        }
    }

    @Test
    fun `I can support multiple nested steps by using extension`() {
        assume {
            4
        } and {
            someNestedUseCase()
        } act {
            action.sum(results())
        } assert {
            assertEquals(4 + 5 + 1 + 2 + 3 + 6, result)
        } and {
            assertEquals(
                """
                ASSUME -> 4
                ASSUME -> 5
                ASSUME -> 1
                AND -> 2
                AND -> 3
                AND -> 6
                ACT -> 21
                ASSERT -> *Something*""".trimIndent(), this.toString()
            )
        }
    }

    @Test
    fun `I cannot support nested steps if it has been created outside of the current context`() {
        val exception = assertThrows<IllegalStateException> {
            assume {
                someUseCaseWithoutExtension()
            } assert {
                println("should not be successful")
            }
        }
        assertEquals(
            """Cannot accept nested step built without extension function. If you want to reuse a collection step, add an extended function like this: 
fun AssumeActAssert.Context.sharedSteps() {
	...
}""", exception.message
        )
    }
}
