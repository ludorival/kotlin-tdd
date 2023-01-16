package io.github.ludorival.kotlintdd.coroutine.gwt

import io.github.ludorival.kotlintdd.coroutine.then
import io.github.ludorival.kotlintdd.coroutine.`when`
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CoGivenWhenThenWithDSLTest {

    @Test
    fun `I can write my test with my custom DSL`() = runTest {
        given {
            `the number`(1)
        } and {
            `the number`(2)
        } `when` {
            sum(results())
        } then {
            it `should be equal to` 3
        }
    }

    @Test
    fun `I should be able to insert a new item in my todo list`() = runTest {
        given {
            `a todo list`
        } and {
            `an item`("Eat banana")
        } `when` {
            `I add the last item into my todo list`
        } then {
            `I expect this item is present in my todo list`
        }
    }
}
