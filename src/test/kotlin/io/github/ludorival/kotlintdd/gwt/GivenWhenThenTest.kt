package io.github.ludorival.kotlintdd.gwt

import io.github.ludorival.kotlintdd.Context
import io.github.ludorival.kotlintdd.`when`
import io.github.ludorival.kotlintdd.then
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import io.github.ludorival.kotlintdd.gwt.UnitTestNoAction.given as givenNoAction

internal class GivenWhenThenTest {

    @Test
    fun `I should get the first and the last value in the chained state`() {
        given {
            1
        } and {
            2
        } `when` {
            sum(it.first(), it.last())
        } then {
            assertEquals(3, it.result)
        } and {
            assertEquals(
                """
                GIVEN -> 1
                AND -> 2
                WHEN -> 3
                THEN -> *Something*""".trimIndent(), it.toString()
            )
        }
    }

    @Test
    fun `A step can return a null value`() {
        `when` {
            null
        } then {
            assertEquals(false, it.hasASupportedResult())
        }
    }


    @Test
    fun `I should get the last result with a predicate`() {
        given {
            1
        } and {
            2
        } and {
            3
        } `when` {
            sum(it.first(), it.last { it == 2 })
        } then {
            assertEquals(3, it.result)
        }
    }

    @Test
    fun `I cannot find none result matching a predicate when I use lastOrNull`() {
        given {
            1
        } and {
            2
        } and {
            3
        } then {
            assertNull(it.lastOrNull<Int> { it > 3 })
        }
    }

    @Test
    fun `I cannot find none Type result matching a predicate when I use lastOrNull`() {
        given {
            1
        } and {
            2
        } and {
            3
        } then {
            assertNull(it.lastOrNull<Double>())
        }
    }

    @Test
    fun `I should not expect to return the nested context when I use lastOrNull`() {
        given {
            someUseCase()
        } and {
            2
        } then {
            assertNull(it.lastOrNull<Context<*>>())
        }
    }

    @Test
    fun `I can find a result matching a predicate when I use lastOrNull`() {
        given {
            1
        } and {
            2
        } and {
            3
        } then { context ->
            assertEquals(2, context.lastOrNull<Int> { it < 3 })
        }
    }

    @Test
    fun `I should expect an exception when get the last result whereas it has not been found`() {
        assertThrows<NullPointerException> {
            given {
                1
            } and {
                2
            } and {
                3
            } then { context ->
                assertNull(context.last<Int> { it > 3 })
            }
        }

    }

    @Test
    fun `I cannot find none result matching a predicate from the first`() {
        given {
            1
        } and {
            2
        } and {
            3
        } then { context ->
            assertNull(context.firstOrNull<Int> { it > 3 })
        }
    }

    @Test
    fun `I cannot find result matching a predicate from the first`() {
        given {
            1
        } and {
            2
        } and {
            3
        } then { context ->
            assertEquals(2, context.firstOrNull<Int> { it > 1 })
        }
    }

    @Test
    fun `I can loop on each state`() {
        given {
            1
        } and {
            2
        } and {
            3
        } then { context ->
            val list = ArrayList<Any>()
            context.forEach<Any> {
                list.add(it.key)
            }
            assertEquals(listOf("AND", "AND", "GIVEN"), list)
        }
    }

    @Test
    fun `I should have access to result by their index`() {
        given {
            1
        } and {
            2
        } and {
            3
        } `when` {
            it.get<Int>(0) * it.get<Int>(2) - it.get<Int>(1)
        } then {
            assertEquals(1 * 3 - 2, it.result)
        }
    }

    @Test
    fun `I should get all results from the latest chained state`() {
        given {
            1
        } and {
            2
        } `when` {
            sum(it.results())
        } then {
            assertEquals(3, it.result)
        }
    }

    @Test
    fun `I can use a pattern without defining an action`() {
        givenNoAction {
            1
        } `when` {
            it.result * 3
        } then {
            assertEquals(3, it.result)
        }
    }

    @Test
    fun `I should get all any results from the latest chained state`() {
        given {
            1
        } and {
            2
        } and {
            "3"
        } then {
            assertEquals(it.anyResults(), listOf(1, 2, "3"))
        }
    }

    @Test
    fun `I should get all any reversed results from the latest chained state`() {
        given {
            1
        } and {
            2
        } and {
            "3"
        } then {
            assertEquals(it.anyReversedResults(), listOf("3", 2, 1))
        }
    }

    @Test
    fun `Unit result should be ignored from the results`() {
        given {
            1
        } and {
            2
        } and {
            println(it.result)
        } then {
            assertEquals(false, it.hasASupportedResult())
            assertEquals(it.anyResults(), listOf(1, 2))
        }
    }

    @Test
    fun `I should use the right order when I use results`() {
        given {
            100
        } and {
            10
        } `when` {
            divide(it.results())  // 100 / 10
        } then {
            assertEquals(10, it.result)
        }
    }

    @Test
    fun `I should use the reverse order when I use reverseResults`() {
        given {
            100
        } and {
            10
        } `when` {
            divide(it.reversedResults())  // 10 / 100
        } then {
            assertEquals(10 / 100, it.result)
        }
    }

    @Test
    fun `I can use directly the when instead of the given`() {
        `when` {
            sum(1, 2)
        } then {
            assertEquals(3, it.result)
        } and {
            assertEquals(
                """
                WHEN -> 3
                THEN -> *Something*""".trimIndent(), it.toString()
            )
        }
    }


    @Test
    fun `I can use when + and + then`() {
        `when` {
            sum(1, 2)
        } and {
            sum(it.result, 3)
        } then {
            assertEquals(6, it.result)
        } and {
            assertEquals(
                """
                WHEN -> 3
                AND -> 6
                THEN -> *Something*""".trimIndent(), it.toString()
            )
        }

    }

    @Test
    fun `Nested context are not considered as supported`() {
        `when` {
            someUseCase()
        } then {
            assertEquals(false, it.hasASupportedResult())
        } and {
            assertEquals(
                """
                GIVEN -> 1
                AND -> 2
                AND -> 3
                THEN -> *Something*""".trimIndent(), it.toString()
            )
        }
    }

    @Test
    fun `I can support nested steps from the given`() {
        given {
            someUseCase()
        } and {
            4
        } `when` {
            sum(it.results())
        } then {
            assertEquals(1 + 2 + 3 + 4, it.result)
        } and {
            assertEquals(
                """
                GIVEN -> 1
                AND -> 2
                AND -> 3
                AND -> 4
                WHEN -> 10
                THEN -> *Something*""".trimIndent(), it.toString()
            )
        }
    }

    @Test
    fun `I can support nested steps after the given`() {
        given {
            4
        } and {
            someUseCase()
        } `when` {
            sum(it.results())
        } then {
            assertEquals(4 + 1 + 2 + 3, it.result)
        } and {
            assertEquals(
                """
                GIVEN -> 4
                GIVEN -> 1
                AND -> 2
                AND -> 3
                WHEN -> 10
                THEN -> *Something*""".trimIndent(), it.toString()
            )
        }
    }

    @Test
    fun `I can support multiple nested steps`() {
        given {
            4
        } and {
            someNestedUseCase()
        } `when` {
            sum(it.results())
        } then {
            assertEquals(4 + 5 + 1 + 2 + 3 + 6, it.result)
        } and {
            assertEquals(
                """
                GIVEN -> 4
                GIVEN -> 5
                GIVEN -> 1
                AND -> 2
                AND -> 3
                AND -> 6
                WHEN -> 21
                THEN -> *Something*""".trimIndent(), it.toString()
            )
        }
    }

    @Test
    fun `I can support nested steps by using extension after the given`() {
        given {
            4
        } and {
            someUseCase()
        } `when` {
            sum(it.results())
        } then {
            assertEquals(4 + 1 + 2 + 3, it.result)
        } and {
            assertEquals(
                """
                GIVEN -> 4
                GIVEN -> 1
                AND -> 2
                AND -> 3
                WHEN -> 10
                THEN -> *Something*""".trimIndent(), it.toString()
            )
        }
    }

    @Test
    fun `I can support multiple nested by using extension steps`() {
        given {
            4
        } and {
            someNestedUseCase()
        } `when` {
            assertEquals(
                """
                GIVEN -> 4
                GIVEN -> 5
                GIVEN -> 1
                AND -> 2
                AND -> 3
                AND -> 6""".trimIndent(), it.toString()
            )
            sum(it.results())
        } then {
            assertEquals(4 + 5 + 1 + 2 + 3 + 6, it.result)
        } and {
            assertEquals(
                """
                GIVEN -> 4
                GIVEN -> 5
                GIVEN -> 1
                AND -> 2
                AND -> 3
                AND -> 6
                WHEN -> 21
                THEN -> *Something*""".trimIndent(), it.toString()
            )
        }
    }

    @Test
    fun `I can access to the previous results inside a nested step using extension function`() {
        given {
            1
        } and {
            given {
                assertEquals(listOf(1), it.anyResults())
                assertEquals(1, it.result)
            }
        }
    }


}
