package io.github.ludorival.kotlintdd.coroutine.gwt

import io.github.ludorival.kotlintdd.coroutine.CoGivenWhenThen
import io.github.ludorival.kotlintdd.dsl.Action
import io.github.ludorival.kotlintdd.dsl.Assertion
import io.github.ludorival.kotlintdd.dsl.Assumption

object UnitTest : CoGivenWhenThen<Assumption, Action, Assertion>(
    assumption = Assumption(),
    action = Action(),
    assertion = Assertion()
)

suspend fun <R> given(block: suspend Assumption.() -> R) = UnitTest.given(block)
suspend fun <R> `when`(block: suspend Action.() -> R) = UnitTest.`when`(block)
