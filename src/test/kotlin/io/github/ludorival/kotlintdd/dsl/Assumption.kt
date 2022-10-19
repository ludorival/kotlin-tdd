package io.github.ludorival.kotlintdd.dsl

import io.github.ludorival.kotlintdd.WithContext

class Assumption : WithContext() {

    val one get() = 1

    val two get() = 2

    fun `the number`(value: Int) = value

    val `a todo list` get() = TodoList()

    fun `an item`(name: String) = TodoList.Item(name)
}
