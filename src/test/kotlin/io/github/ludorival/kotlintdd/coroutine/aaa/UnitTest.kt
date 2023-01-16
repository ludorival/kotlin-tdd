package io.github.ludorival.kotlintdd.coroutine.aaa

import io.github.ludorival.kotlintdd.coroutine.CoAssumeActAssert
import io.github.ludorival.kotlintdd.dsl.Action
import io.github.ludorival.kotlintdd.dsl.Assertion
import io.github.ludorival.kotlintdd.dsl.Assumption

object UnitTest : CoAssumeActAssert<Assumption, Action, Assertion>(
    assumption = Assumption(),
    action = Action(),
    assertion = Assertion()
)

suspend fun <R> assume(block: suspend Assumption.() -> R) = UnitTest.assume(block)
suspend fun <R> act(block: suspend Action.() -> R) = UnitTest.act(block)
