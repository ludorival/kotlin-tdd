package io.github.ludorival.kotlintdd.aaa

import io.github.ludorival.kotlintdd.act
import io.github.ludorival.kotlintdd.assert
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import io.github.ludorival.kotlintdd.SimpleAssumeActAssert.assume as assumeNoAction

internal class AssumeActAssertTest {


    @Test
    fun `I can use simple assume act assert pattern`() {
        assume {
            1
        } act {
            sum(it.result, 2)
        } assert {
            3 `should be equal to` it.result
        } and {

            """
                ASSUME -> 1
                ACT -> 3
                ASSERT -> *Something*""".trimIndent() `should be equal to` it.toString()

        }
    }

    @Test
    fun `I can use assume act assert pattern with and`() {
        assume {
            1
        } and {
            2
        } act {
            sum(it.first(), it.last())
        } assert {
            it.result `should be equal to` 3
        } and {
            """
                ASSUME -> 1
                AND -> 2
                ACT -> 3
                ASSERT -> *Something*""".trimIndent() `should be equal to` it.toString()
        }
    }


    @Test
    fun `I can use directly the act instead of the assume`() {
        act {
            sum(1, 2)
        } assert {
            assertEquals(it.result, 3)
        } and {
            assertEquals(
                """
                ACT -> 3
                ASSERT -> *Something*""".trimIndent(), it.toString()
            )
        }
    }


    @Test
    fun `I can support nested steps from the ASSUME`() {
        assume {
            someUseCaseWithoutExtension()
        } and {
            4
        } act {
            sum(it.results())
        } assert {
            assertEquals(it.result, 1 + 2 + 3 + 4)
        } and {
            assertEquals(
                """
                ASSUME -> 1
                AND -> 2
                AND -> 3
                AND -> 4
                ACT -> 10
                ASSERT -> *Something*""".trimIndent(), it.toString()
            )
        }
    }

    @Test
    fun `I can support nested steps after the ASSUME`() {
        assume {
            4
        } and {
            someUseCaseWithoutExtension()
        } act {
            sum(it.results())
        } assert {
            assertEquals(it.result, 4 + 1 + 2 + 3)
        } and {
            assertEquals(
                """
                ASSUME -> 4
                ASSUME -> 1
                AND -> 2
                AND -> 3
                ACT -> 10
                ASSERT -> *Something*""".trimIndent(), it.toString()
            )
        }
    }

    @Test
    fun `I can support multiple nested steps`() {
        assume {
            4
        } and {
            someNestedUseCaseWithoutExtension()
        } act {
            sum(it.results())
        } assert {
            assertEquals(4 + 5 + 1 + 2 + 3 + 6, it.result)
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
                ASSERT -> *Something*""".trimIndent(), it.toString()
            )
        }
    }

    @Test
    fun `I can support nested steps by using extension after the ASSUME`() {
        assume {
            4
        } and {
            someUseCaseWithoutExtension()
        } act {
            sum(it.results())
        } assert {
            assertEquals(it.result, 4 + 1 + 2 + 3)
        } and {
            assertEquals(
                """
                ASSUME -> 4
                ASSUME -> 1
                AND -> 2
                AND -> 3
                ACT -> 10
                ASSERT -> *Something*""".trimIndent(), it.toString()
            )
        }
    }

    @Test
    fun `I can support multiple nested steps by using extension`() {
        assume {
            4
        } and {
            someNestedUseCaseWithoutExtension()
        } act {
            sum(it.results())
        } assert {
            assertEquals(4 + 5 + 1 + 2 + 3 + 6, it.result)
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
                ASSERT -> *Something*""".trimIndent(), it.toString()
            )
        }
    }

    @Test
    fun `I can use a pattern without defining an action`() {
        assumeNoAction {
            1
        } act {
            it.result * 3
        } assert {
            assertEquals(3, it.result)
        }
    }


}
