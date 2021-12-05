package com.ludorival.kotlintdd

class Action {

    fun sum(value1: Int, value2: Int) = value1 + value2

    fun sum(list: List<Int>) = list.reduce(Int::plus)

    fun divide(list: List<Int>) = list.reduce(Int::div)

}
