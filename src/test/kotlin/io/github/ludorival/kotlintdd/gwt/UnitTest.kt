package io.github.ludorival.kotlintdd.gwt

import io.github.ludorival.kotlintdd.Context
import io.github.ludorival.kotlintdd.GivenWhenThen
import io.github.ludorival.kotlintdd.dsl.Action
import io.github.ludorival.kotlintdd.dsl.Assertion
import io.github.ludorival.kotlintdd.dsl.Assumption

object UnitTest : GivenWhenThen<Assumption, Action, Assertion>(assumption = Assumption()) {
    override fun actionReceiver(context: Context<*>): Action = Action(context)
    override fun assertionReceiver(context: Context<*>): Assertion = Assertion(context)
}

fun <R> given(block: Assumption.() -> R) = UnitTest.given(block)
fun <R> `when`(block: Action.() -> R) = UnitTest.`when`(block)
