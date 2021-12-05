package com.ludorival.kotlintdd

import com.ludorival.kotlintdd.UnitTest.given
import org.junit.jupiter.api.Test

class KotlinTDDWithDSLTest {

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
