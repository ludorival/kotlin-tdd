package io.github.ludorival.kotlintdd.gwt

import io.github.ludorival.kotlintdd.Action
import io.github.ludorival.kotlintdd.GWTContext
import io.github.ludorival.kotlintdd.GivenWhenThen

object UnitTest : GivenWhenThen<Action> {
    override val action: Action = Action()
}

fun <R> given(block: GWTContext<Action, Unit>.() -> R) = UnitTest.given(block)
fun <R> `when`(block: GWTContext<Action, Unit>.() -> R) = UnitTest.`when`(block)
