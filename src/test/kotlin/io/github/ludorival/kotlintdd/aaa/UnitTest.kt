package io.github.ludorival.kotlintdd.aaa

import io.github.ludorival.kotlintdd.AssumeActAssert
import io.github.ludorival.kotlintdd.dsl.Action
import io.github.ludorival.kotlintdd.dsl.Assertion
import io.github.ludorival.kotlintdd.dsl.Assumption

object UnitTest : AssumeActAssert<Assumption, Action, Assertion>(
    assumption = Assumption(),
    action = Action(),
    assertion = Assertion()
)

fun <R> assume(block: Assumption.() -> R) = UnitTest.assume(block)
fun <R> act(block: Action.() -> R) = UnitTest.act(block)
