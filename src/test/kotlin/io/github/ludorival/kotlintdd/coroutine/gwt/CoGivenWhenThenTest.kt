package io.github.ludorival.kotlintdd.coroutine.gwt

import io.github.ludorival.kotlintdd.Context
import io.github.ludorival.kotlintdd.coroutine.then
import io.github.ludorival.kotlintdd.coroutine.`when`
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import io.github.ludorival.kotlintdd.coroutine.CoSimpleGivenWhenThen.given as givenNoAction

@OptIn(ExperimentalCoroutinesApi::class)
internal class CoGivenWhenThenTest {

    @Test
    fun `I should get the first and the last value in the chained state`() = runTest {
        given {
            1
        } and {
            2
        } `when` {
            sum(first(), last())
        } then {
            assertEquals(3, it)
        } and {
            assertEquals(
                """
                GIVEN -> 1
                AND -> 2
                WHEN -> 3
                THEN -> *Something*""".trimIndent(), toString()
            )
        }
    }

    @Test
    fun `A step can return a null value`() = runTest {
        `when` {
            null
        } then {
            assertEquals(false, currentContext.hasASupportedResult())
        }
    }


    @Test
    fun `I should get the last result with a predicate`() = runTest {
        given {
            1
        } and {
            2
        } and {
            3
        } `when` {
            sum(first(), last { it == 2 })
        } then {
            assertEquals(3, it)
        }
    }

    @Test
    fun `I cannot find none result matching a predicate when I use lastOrNull`() = runTest {
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
    fun `I cannot find none Type result matching a predicate when I use lastOrNull`() = runTest {
        given {
            1
        } and {
            2
        } and {
            3
        } then {
            assertNull(lastOrNull<Double>())
        }
    }

    @Test
    fun `I should not expect to return the nested context when I use lastOrNull`() = runTest {
        given {
            someUseCase()
        } and {
            2
        } then {
            assertNull(lastOrNull<Context<*>>())
        }
    }

    @Test
    fun `I can find a result matching a predicate when I use lastOrNull`() = runTest {
        given {
            1
        } and {
            2
        } and {
            3
        } then { result ->
            assertEquals(3, result)
            assertEquals(2, lastOrNull<Int> { it < 3 })
        }
    }

    @Test
    fun `I should expect an exception when get the last result whereas it has not been found`() = runTest {
        assertThrows<NullPointerException> {
            given {
                1
            } and {
                2
            } and {
                3
            } then {
                assertNull(last<Int> { it > 3 })
            }
        }

    }

    @Test
    fun `I cannot find none result matching a predicate from the first`() = runTest {
        given {
            1
        } and {
            2
        } and {
            3
        } then {
            assertNull(firstOrNull<Int> { it > 3 })
        }
    }

    @Test
    fun `I cannot find result matching a predicate from the first`() = runTest {
        given {
            1
        } and {
            2
        } and {
            3
        } then {
            assertEquals(2, firstOrNull<Int> { it > 1 })
        }
    }

    @Test
    fun `I can loop on each state`() = runTest {
        given {
            1
        } and {
            2
        } and {
            3
        } then {
            val list = ArrayList<Any>()
            forEach<Any> {
                list.add(it.key)
            }
            assertEquals(listOf("AND", "AND", "GIVEN"), list)
        }
    }

    @Test
    fun `I should have access to result by their index`() = runTest {
        given {
            1
        } and {
            2
        } and {
            3
        } `when` {
            get<Int>(0) * get<Int>(2) - get<Int>(1)
        } then {
            assertEquals(1 * 3 - 2, it)
        }
    }

    @Test
    fun `I should get all results from the latest chained state`() = runTest {
        given {
            1
        } and {
            2
        } `when` {
            sum(results())
        } then {
            assertEquals(3, it)
        }
    }

    @Test
    fun `I can use a pattern without defining an action`() = runTest {
        givenNoAction {
            1
        } `when` {
            it * 3
        } then {
            assertEquals(3, it)
        }
    }

    @Test
    fun `I should get all any results from the latest chained state`() = runTest {
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
    fun `I should get all any reversed results from the latest chained state`() = runTest {
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
    fun `Unit result should be ignored from the results`() = runTest {
        given {
            1
        } and {
            2
        } and {
            println(it)
        } then {
            assertEquals(false, currentContext.hasASupportedResult())
            assertEquals(anyResults(), listOf(1, 2))
        }
    }

    @Test
    fun `I should use the right order when I use results`() = runTest {
        given {
            100
        } and {
            10
        } `when` {
            divide(results())  // 100 / 10
        } then {
            assertEquals(10, it)
        }
    }

    @Test
    fun `I should use the reverse order when I use reverseResults`() = runTest {
        given {
            100
        } and {
            10
        } `when` {
            divide(reversedResults())  // 10 / 100
        } then {
            assertEquals(10 / 100, it)
        }
    }

    @Test
    fun `I can use directly the when instead of the given`() = runTest {
        `when` {
            sum(1, 2)
        } then {
            assertEquals(3, it)
        } and {
            assertEquals(
                """
                WHEN -> 3
                THEN -> *Something*""".trimIndent(), toString()
            )
        }
    }


    @Test
    fun `I can use when + and + then`() = runTest {
        `when` {
            sum(1, 2)
        } and {
            sum(it, 3)
        } then {
            assertEquals(6, it)
        } and {
            assertEquals(
                """
                WHEN -> 3
                AND -> 6
                THEN -> *Something*""".trimIndent(), toString()
            )
        }

    }

    @Test
    fun `Nested context are not considered as supported`() = runTest {
        `when` {
            someUseCase()
        } then {
            assertEquals(false, currentContext.hasASupportedResult())
        } and {
            assertEquals(
                """
                GIVEN -> 1
                AND -> 2
                AND -> 3
                THEN -> *Something*""".trimIndent(), toString()
            )
        }
    }

    @Test
    fun `I can support nested steps from the given`() = runTest {
        given {
            someUseCase()
        } and {
            4
        } `when` {
            sum(results())
        } then {
            assertEquals(1 + 2 + 3 + 4, it)
        } and {
            assertEquals(
                """
                GIVEN -> 1
                AND -> 2
                AND -> 3
                AND -> 4
                WHEN -> 10
                THEN -> *Something*""".trimIndent(), toString()
            )
        }
    }

    @Test
    fun `I can support nested steps after the given`() = runTest {
        given {
            4
        } and {
            someUseCase()
        } `when` {
            sum(results())
        } then {
            assertEquals(4 + 1 + 2 + 3, it)
        } and {
            assertEquals(
                """
                GIVEN -> 4
                GIVEN -> 1
                AND -> 2
                AND -> 3
                WHEN -> 10
                THEN -> *Something*""".trimIndent(), toString()
            )
        }
    }

    @Test
    fun `I can support multiple nested steps`() = runTest {
        given {
            4
        } and {
            someNestedUseCase()
        } `when` {
            sum(results())
        } then {
            assertEquals(4 + 5 + 1 + 2 + 3 + 6, it)
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
                THEN -> *Something*""".trimIndent(), toString()
            )
        }
    }

    @Test
    fun `I can support nested steps by using extension after the given`() = runTest {
        given {
            4
        } and {
            someUseCase()
        } `when` {
            sum(results())
        } then {
            assertEquals(4 + 1 + 2 + 3, it)
        } and {
            assertEquals(
                """
                GIVEN -> 4
                GIVEN -> 1
                AND -> 2
                AND -> 3
                WHEN -> 10
                THEN -> *Something*""".trimIndent(), toString()
            )
        }
    }

    @Test
    fun `I can support multiple nested by using extension steps`() = runTest {
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
                AND -> 6""".trimIndent(), toString()
            )
            sum(results())
        } then {
            assertEquals(4 + 5 + 1 + 2 + 3 + 6, it)
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
                THEN -> *Something*""".trimIndent(), toString()
            )
        }
    }

    @Test
    fun `I can access to the previous results inside a nested step using extension function`() = runTest {
        given {
            1
        } and {
            val results = anyResults()
            given {
                assertEquals(listOf(1), results)
                assertEquals(1, it)
            }
        }
    }


}
