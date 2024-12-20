package io.github.ludorival.kotlintdd.dsl

import io.github.ludorival.kotlintdd.Step
@Suppress("VariableNaming")
class Action : Step() {

    fun sum(value1: Int, value2: Int) = value1 + value2

    fun sum(list: List<Int>) = list.reduce(Int::plus)

    fun divide(list: List<Int>) = list.reduce(Int::div)


    val `I add the last item into my todo list`
        get() =
            last<TodoList>().items.add(last())
}
