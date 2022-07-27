package io.github.ludorival.kotlintdd.gwt

import io.github.ludorival.kotlintdd.BasePattern
import io.github.ludorival.kotlintdd.Context
import io.github.ludorival.kotlintdd.dsl.Action
import io.github.ludorival.kotlintdd.dsl.Assertion
import io.github.ludorival.kotlintdd.dsl.Assumption

object UnitTest : BasePattern<Assumption, Action, Assertion>() {
    override fun assumptionReceiver(context: Context<*>): Assumption = Assumption()
    override fun actionReceiver(context: Context<*>): Action = Action(context)
    override fun assertionReceiver(context: Context<*>): Assertion = Assertion(context)
}

fun <R> given(block: Assumption.() -> R) = UnitTest.assume("GIVEN", block)
fun <R> `when`(block: Action.() -> R) = UnitTest.act("WHEN", block)
