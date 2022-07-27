package io.github.ludorival.kotlintdd.gwt

import io.github.ludorival.kotlintdd.BasePattern
import io.github.ludorival.kotlintdd.Context

object UnitTestNoAction : BasePattern<Unit, Unit, Unit>() {

    override fun assumptionReceiver(context: Context<*>) = Unit
    override fun actionReceiver(context: Context<*>) = Unit
    override fun assertionReceiver(context: Context<*>) = Unit

    fun <V> given(block: Unit.() -> V) = assume("GIVEN", block)
}
