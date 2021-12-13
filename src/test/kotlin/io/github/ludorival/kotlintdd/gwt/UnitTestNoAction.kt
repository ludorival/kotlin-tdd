package io.github.ludorival.kotlintdd.gwt

import io.github.ludorival.kotlintdd.GivenWhenThen

object UnitTestNoAction : GivenWhenThen<Unit> {
    override val action: Unit = Unit
}
