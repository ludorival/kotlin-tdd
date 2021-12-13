package io.github.ludorival.kotlintdd.aaa

import io.github.ludorival.kotlintdd.AAAContext
import io.github.ludorival.kotlintdd.Action
import io.github.ludorival.kotlintdd.AssumeActAssert

object UnitTest : AssumeActAssert<Action> {
    override val action: Action = Action()
}

fun <R> assume(block: AAAContext<Action, Unit>.() -> R) = UnitTest.assume(block)
fun <R> act(block: AAAContext<Action, Unit>.() -> R) = UnitTest.act(block)
