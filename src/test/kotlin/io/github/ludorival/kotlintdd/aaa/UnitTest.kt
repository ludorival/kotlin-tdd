package io.github.ludorival.kotlintdd.aaa

import io.github.ludorival.kotlintdd.AssumeActAssert
import io.github.ludorival.kotlintdd.Context
import io.github.ludorival.kotlintdd.dsl.Action
import io.github.ludorival.kotlintdd.dsl.Assertion
import io.github.ludorival.kotlintdd.dsl.Assumption

object UnitTest : AssumeActAssert<Assumption, Action, Assertion>(assumption = Assumption()) {
    override fun <T> actionReceiver(context: Context<T>): Action = Action(context)
    override fun <T> assertionReceiver(context: Context<T>): Assertion = Assertion(context)

}

fun <R> assume(block: Assumption.() -> R) = UnitTest.assume(block)
fun <R> act(block: Action.() -> R) = UnitTest.act(block)
