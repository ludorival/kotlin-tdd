package io.github.ludorival.kotlintdd.gwt

import io.github.ludorival.kotlintdd.`I expect the result is`
import io.github.ludorival.kotlintdd.`I perform their sum`
import io.github.ludorival.kotlintdd.`the number`
import io.github.ludorival.kotlintdd.gwt.UnitTest.given
import org.junit.jupiter.api.Test

class GivenWhenThenWithDSLTest {

    @Test
    fun `I can write my test with my custom DSL`() {
        given {
            `the number`(1)
        } and {
            `the number`(2)
        } `when` {
            `I perform their sum`
        } then {
            `I expect the result is`(3)
        }
    }
}
