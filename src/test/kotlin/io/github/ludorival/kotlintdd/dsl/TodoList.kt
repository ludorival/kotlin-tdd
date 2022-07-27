package io.github.ludorival.kotlintdd.dsl

data class TodoList(val items: MutableList<Item> = mutableListOf()) {
    data class Item(val name: String)
}
