package io.github.ludorival.kotlintdd.gwt

import io.github.ludorival.kotlintdd.GivenWhenThen
import io.github.ludorival.kotlintdd.dsl.Action
import io.github.ludorival.kotlintdd.dsl.Assertion
import io.github.ludorival.kotlintdd.dsl.Assumption

object UnitTest : GivenWhenThen<Assumption, Action, Assertion>(
    assumption = Assumption(),
    action = Action(),
    assertion = Assertion()
)

fun <R> given(block: Assumption.() -> R) = UnitTest.given(block)
fun <R> `when`(block: Action.() -> R) = UnitTest.`when`(block)
