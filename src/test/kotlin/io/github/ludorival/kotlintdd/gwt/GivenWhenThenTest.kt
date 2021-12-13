package io.github.ludorival.kotlintdd.gwt

import io.github.ludorival.kotlintdd.GivenWhenThen.Step.AND
import io.github.ludorival.kotlintdd.GivenWhenThen.Step.GIVEN
import io.github.ludorival.kotlintdd.GivenWhenThen.Step.THEN
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import io.github.ludorival.kotlintdd.gwt.UnitTestNoAction.given as givenNoAction

internal class GivenWhenThenTest {

    @Test
    fun `I should get the first and the last value in the chained state`() {
        given {
            1
        } and {
            2
        } `when` {
            action.sum(first(), last())
        } then {
            assertEquals(3, result)
        } and {
            assertEquals(
                """
                GIVEN -> 1
                AND -> 2
                WHEN -> 3
                THEN -> *Something*""".trimIndent(), this.toString()
            )
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
            action.sum(first(), last { it == 2 })
        } then {
            assertEquals(3, result)
        }
    }

    @Test
    fun `I cannot find none result matching a predicate from the last`() {
        given {
            1
        } and {
            2
        } and {
            3
        } then {
            assertNull(lastOrNull<Int> { it > 3 })
        }
    }

    @Test
    fun `I can loop on each state until a condition`() {
        given {
            1
        } and {
            2
        } and {
            3
        } then {
            val list = ArrayList<Any>()
            eachUntil<Any> {
                list.add(it.step)
                it.step == THEN
            }
            assertEquals(listOf(AND, AND, GIVEN), list)
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
            get<Int>(0) * get<Int>(2) - get<Int>(1)
        } then {
            assertEquals(1 * 3 - 2, result)
        }
    }

    @Test
    fun `I should get all results from the latest chained state`() {
        given {
            1
        } and {
            2
        } `when` {
            action.sum(results())
        } then {
            assertEquals(3, result)
        }
    }

    @Test
    fun `I can use a pattern without defining an action`() {
        givenNoAction {
            1
        } `when` {
            result * 3
        } then {
            assertEquals(3, result)
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
            assertEquals(anyResults(), listOf(1, 2, "3"))
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
            assertEquals(anyReversedResults(), listOf("3", 2, 1))
        }
    }

    @Test
    fun `Unit result should be ignored from the results`() {
        given {
            1
        } and {
            2
        } and {
            println(result)
        } then {
            assertEquals(anyResults(), listOf(1, 2))
        }
    }

    @Test
    fun `I should use the right order when I use results`() {
        given {
            100
        } and {
            10
        } `when` {
            action.divide(results())  // 100 / 10
        } then {
            assertEquals(10, result)
        }
    }

    @Test
    fun `I should use the reverse order when I use reverseResults`() {
        given {
            100
        } and {
            10
        } `when` {
            action.divide(reversedResults())  // 10 / 100
        } then {
            assertEquals(10 / 100, result)
        }
    }

    @Test
    fun `I can use directly the when instead of the given`() {
        `when` {
            action.sum(1, 2)
        } then {
            assertEquals(3, result)
        } then {
            assertEquals(
                """
                WHEN -> 3
                THEN -> *Something*""".trimIndent(), this.toString()
            )
        }
    }


    @Test
    fun `I can use when + and + then`() {
        `when` {
            action.sum(1, 2)
        } and {
            action.sum(result, 3)
        } then {
            assertEquals(6, result)
        } then {
            assertEquals(
                """
                WHEN -> 3
                AND -> 6
                THEN -> *Something*""".trimIndent(), this.toString()
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
            action.sum(results())
        } then {
            assertEquals(1 + 2 + 3 + 4, result)
        } and {
            assertEquals(
                """
                GIVEN -> 1
                AND -> 2
                AND -> 3
                AND -> 4
                WHEN -> 10
                THEN -> *Something*""".trimIndent(), this.toString()
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
            action.sum(results())
        } then {
            assertEquals(4 + 1 + 2 + 3, result)
        } and {
            assertEquals(
                """
                GIVEN -> 4
                GIVEN -> 1
                AND -> 2
                AND -> 3
                WHEN -> 10
                THEN -> *Something*""".trimIndent(), this.toString()
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
            action.sum(results())
        } then {
            assertEquals(4 + 5 + 1 + 2 + 3 + 6, result)
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
                THEN -> *Something*""".trimIndent(), this.toString()
            )
        }
    }

    private fun someUseCase() = given {
        1
    } and {
        2
    } and {
        3
    }

    private fun someNestedUseCase() = given {
        5
    } and {
        someUseCase()
    } and {
        6
    }


}
