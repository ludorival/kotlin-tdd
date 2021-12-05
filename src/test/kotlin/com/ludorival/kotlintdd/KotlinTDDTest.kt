package com.ludorival.kotlintdd

import com.ludorival.kotlintdd.UnitTest.`when`
import com.ludorival.kotlintdd.UnitTest.act
import com.ludorival.kotlintdd.UnitTest.assume
import com.ludorival.kotlintdd.UnitTest.given
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import com.ludorival.kotlintdd.UnitTestNoAction.given as givenNoAction

internal class KotlinTDDTest {

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
        }
    }

    @Test
    fun `I should get the first and the last value in the chained state with AAA pattern`() {
        assume {
            1
        } and {
            2
        } act {
            action.sum(first(), last())
        } assert {
            assertEquals(3, result)
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
        } then {
            assertEquals(anyResults(), listOf(1, 2))
        }
    }

    @Test
    fun `I should get all any reversed results from the latest chained state`() {
        given {
            1
        } and {
            2
        } then {
            assertEquals(anyReversedResults(), listOf(2, 1))
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
            assertEquals(result, 10)
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
            assertEquals(result, 10 / 100)
        }
    }

    @Test
    fun `I can use directly the when instead of the given`() {
        `when` {
            action.sum(1, 2)
        } then {
            assertEquals(result, 3)
        }
    }

    @Test
    fun `I can use directly the act instead of the assume`() {
        act {
            action.sum(1, 2)
        } assert {
            assertEquals(result, 3)
        }
    }
}
