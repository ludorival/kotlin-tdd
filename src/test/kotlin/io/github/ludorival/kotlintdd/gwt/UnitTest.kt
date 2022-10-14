package io.github.ludorival.kotlintdd.gwt

import io.github.ludorival.kotlintdd.Context
import io.github.ludorival.kotlintdd.GivenWhenThen
import io.github.ludorival.kotlintdd.dsl.Action
import io.github.ludorival.kotlintdd.dsl.Assertion
import io.github.ludorival.kotlintdd.dsl.Assumption

object UnitTest : GivenWhenThen<Assumption, Action, Assertion>(assumption = Assumption()) {
    override fun <T> actionReceiver(context: Context<T>): Action = Action(context)
    override fun <T> assertionReceiver(context: Context<T>): Assertion = Assertion(context)
}

fun <R> given(block: Assumption.() -> R) = UnitTest.given(block)
fun <R> `when`(block: Action.() -> R) = UnitTest.`when`(block)
