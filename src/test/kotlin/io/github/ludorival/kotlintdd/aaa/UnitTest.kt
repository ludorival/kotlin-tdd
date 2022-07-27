package io.github.ludorival.kotlintdd.aaa

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

fun <R> assume(block: Assumption.() -> R) = UnitTest.assume("ASSUME", block)
fun <R> act(block: Action.() -> R) = UnitTest.act("ACT", block)
