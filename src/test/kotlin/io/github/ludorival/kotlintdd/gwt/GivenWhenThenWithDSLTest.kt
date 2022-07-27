package io.github.ludorival.kotlintdd.gwt

import io.github.ludorival.kotlintdd.`when`
import io.github.ludorival.kotlintdd.then
import org.junit.jupiter.api.Test

class GivenWhenThenWithDSLTest {

    @Test
    fun `I can write my test with my custom DSL`() {
        given {
            `the number`(1)
        } and {
            `the number`(2)
        } `when` {
            sum(it.results())
        } then {
            it.result `should be equal to` 3
        }
    }

    @Test
    fun `I should be able to insert a new item in my todo list`() {
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
