Kotlin Test Driven Design
=========================
![Build status](https://github.com/ludorival/kotlin-tdd/actions/workflows/build.yaml/badge.svg)
![Publish status](https://github.com/ludorival/kotlin-tdd/actions/workflows/publish.yaml/badge.svg)
![GitHub release (latest by date)](https://img.shields.io/github/v/release/ludorival/kotlin-tdd)
> Lightweight library suggesting a TDD implementation with Kotlin

Kotlin-TDD provides a way to write your unit test (or acceptance test)
as you will write an acceptance criteria in natural english language.

Write your test by following the Given When Then pattern.

````kotlin
import io.github.ludorival.kotlintdd.SimpleGivenWhenThen.given

class MyTest {

    @Test
    fun `I can write my test with my custom DSL`() {
        given {
            1
        } and {
            2
        } `when` {
            `I perform their sum`
        } then {
            `I expect the result is`(3)
        }
    }

}
````

Or by using the Assume Act Assert pattern

````kotlin
import io.github.ludorival.kotlintdd.SimpleAssumeActAssert.assume

class MyTest {

    @Test
    fun `I can write my test with my custom DSL`() {
        assume {
            `the number`(1)
        } and {
            `the number`(2)
        } act {
            `I perform their sum`
        } assert {
            `I expect the result is`(3)
        }
    }
}
````

## Coroutine support

> The library supports also **coroutine** functions.

````kotlin
import io.github.ludorival.kotlintdd.coroutine.CoSimpleGivenWhenThen.given
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class MyTest {

    @Test
    fun `I can write my Coroutine test with my custom DSL`() = runTest {
        given {
            1
        } and {
            2
        } `when` {
            `I perform their sum`
        } then {
            `I expect the result is`(3)
        }
    }

}
````
# Getting started

Kotlin-TDD is available via Maven Central. Just add the dependency to your Maven POM or Gradle build config.

**Gradle**

```groovy
testImplementation "io.github.ludorival:kotlin-tdd:$kotlintddVersion"
```

**Maven**

````xml

<dependency>
    <groupId>io.github.ludorival</groupId>
    <artifactId>kotlin-tdd</artifactId>
    <version>${kotlintddVersion}</version>
    <scope>test</scope>
</dependency>
````

# Presentation

Kotlin-TDD is not just a simple list of new functions for TDD. It has also a context management between steps without
stored anything statically. Indeed, each step stores the result as its field, and it is linked to the previous step. It
allows to:

- remove completely the need to create intermediary variable
- navigate between the different results obtained after each step
- focus on test readability, and the effort to have organized test

Let's compare with different approaches when writing tests:
<table>
<thead>
<tr>
<th>Basic test</th>
<th>Kotlin-TDD test</th>
<th>Cucumber test</th>
</tr>
</thead>
<tbody>
<tr>
<td>

```kotlin

@Test
fun shouldInsertANewItemInTodoList() {
    // Given
    val todo = TodoList()
    val item = Item("Eat banana")

    // When
    val addedItem = todo.add(item)

    // Then
    assertTrue(todo.contains(item))
}
```

</td>
<td>

```kotlin

@Test
fun `I should be able to insert a new item in my todo list`() {
    given {
        TodoList()
    } and {
        Item("Eat banana")
    } `when` {
        first<TodoList>().add(it)
    } then {
        assertTrue(first<TodoList>().contains(first<Item>()))
    }
}
```

</td>
<td>

```gherkin

Scenario: I should be able to insert a new item in my todo list
GIVEN a new Todo list
AND the following item to add "Eat banana"
WHEN I add this item in my todo list
THEN I expect this item is well present in my todo list
```

</td>
</tr>
<tr>
<td>
<ul>
<li>👍 Simple</li>
<li>👍 Use standard libraries</li>
<li>👍 Used as well for UT,AT/IT</li>
<li>👎 Less focused on functional meaning</li>
<li>👎 Need to create intermediary variable or comment to keep the code readable</li>
</ul>
</td>
<td>
<ul>
<li>👍 Readable, No variables, no comments</li>
<li>👍 No need to create intermediary variable</li>
<li>👍 No need to learn a new language</li>
<li>👍 Can use it as well for UT/IT/AT</li>
<li>👎 Still quite technical but can be improved by creating your own custom DSL</li>
</ul>
</td>
<td>
<ul>
<li>👍 Natural language understandable by all</li>
<li>👍 More focused on functional meaning</li>
<li>👎 This is yet another language</li>
<li>👎 More suitable for Acceptance Test, too overkill for a unit test</li>
<li>👎 Need a more complex setup</li>
<li>👎 Need to manage ourself the context between steps</li>
</ul>
</td>
</tr>
</tbody>
</table>

# Features

Kotlin-TDD provides a useful mechanism to save the test context **without managing static values**.

![Steps!](http://www.plantuml.com/plantuml/png/SoWkIImgAStDuUAArefLqDMrKt0iBYxDBIZ9pC_ZGZ0LK2Ii51ppKb1aGTB943t9SFN92BKGDipyr2AmKZXB0IQCq03cmlK0tSRba9gN0dGe0000 "")

Steps can be organized by three contexts:

- Assumption: a list of operations to create assumptions
- Action: a list of operations to mutate assumptions to a result
- Assertion: a list of operations to verify the result

As you can see, Kotlin TDD has its own data structure closed to a Linked list to manage the context. There are some
useful operations what you can do inside a context.

## Get the previous step result (`result`)

After a step declaration, you can get the result from the previous step with `result`.

Example :

```kotlin
    @Test
fun `I can use the previous result`() {
    given {
        1
    } `when` {
        it + 2 // result is 1
    } then {
        assertEquals(3, it) // result is 3
    }
}
```

## Get the first result matching a given type or predicate (`first<T>` or `firstOrNull<T>`)

From a step, you can access to the first result matching a given type with `first<T>`.

Example :

```kotlin
    @Test
fun `I can access to the first result`() {
    given {
        "a string"
    } and {
        1 // <-- the first Int
    } and {
        2
    } `when` {
        first<Int>() + it // first<Int>() -> 1
    } then {
        assertEquals(3, it) // result is 3
    }
}
```

This is possible to pass a predicate function as argument to check which is the first result matching this predicate.

```kotlin
    @Test
fun `I can access to the first result matching a predicate`() {
    given {
        1
    } and {
        2 // <-- first int greater than 1
    } and {
        3
    } `when` {
        first<Int> { it > 1 } + it// first<Int>{ it > 1 } -> 2
    } then {
        assertEquals(2 + 3, it) // result is 5
    }
}
```

> `firstOrNull<T>` does the same except it can return a nullable value if any type and predicate match

## Get the last result matching a given type or predicate (`last<T>` or `lastOrNull<T>`)

This is the reverse operation than `first<T>` described above.

Example :

```kotlin
    @Test
fun `I can access to the last integer result`() {
    given {
        1 // <-- the first Int
    } and {
        2 // <-- the last Int
    } and {
        "a string"
    } `when` {
        first<Int>() + last<Int>() // 1 + 2
    } then {
        assertEquals(3, it) // result is 3
    }
}
```

You can also pass a predicate function:

```kotlin
    @Test
fun `I can access to the last result matching a predicate`() {
    given {
        1
    } and {
        2 // <-- last int lower than 3
    } and {
        3
    } `when` {
        it + last<Int> { it < 3 } // 3 + 2
    } then {
        assertEquals(3 + 2, it) // result is 5
    }
}
```

## Get a result at given position (`get<T>`)

If you know exactly in which step to get your result, you can use `get<T>(index)` to access

Example

```kotlin
    @Test
fun `I can access to a result thanks to its position`() {
    given {
        1        // index 0
    } and {
        println("Unit result are skipped") // skipped
    } and {
        "Hello" // index 1
    } and {
        3.5      // index 2
    } `when` {
        println(get<String>(1)) // print "Hello"
    }
}
```

> ⚠️ All seen operations and the next one always skip Unit result type from their response.

## Get all results matching a given type (`results<T>` or `reversedResults<T>`)

You can get all previous results matching a given type thanks to `results<T>()` function.

Example

```kotlin
    @Test
fun `I can access to all integer results from top to bottom`() {
    given {
        1
    } and {
        "Hello"
    } and {
        2
    } `when` {
        results<Int>().reduce(Int::plus) // results -> [1, 2]
    } then {
        assertEquals(3, it)
    }
}
```

The same operation is possible in reverse mode:

Example

```kotlin
    @Test
fun `I can access to all integer results from bottom to top`() {
    given {
        1
    } and {
        "Hello"
    } and {
        2
    } `when` {
        reversedResults<Int>().reduce(Int::plus) // results -> [2, 1]
    } then {
        assertEquals(3, it)
    }
}
```

> `anyResults` and `results<Any>` are equivalent and allow to return all results except Unit

## Use nested context in a step

It can be convenient to factorize any built context in different tests.

Imagine that you have two tests using the same snippet code

```kotlin
    @Test
fun test1() {
    given {
        1
    } and {
        2
    } `when` {
        action.sum(results) // 1 + 2
    } then {
        assertEquals(1 + 2, it)
    }
}

@Test
fun test2() {
    given {
        1
    } and {
        2
    } and {
        3
    } `when` {
        action.sum(results) // 1 + 2 + 3
    } then {
        assertEquals(1 + 2 + 3, result)
    }
}
```

Let's create a function which factorize common code:

```kotlin

fun commonContext() =
    given {
        1
    } and {
        2
    }

@Test
fun test1() {
    given {
        commonContext()
    } `when` {
        action.sum(results) // 1 + 2
    } then {
        assertEquals(1 + 2, it)
    }
}

@Test
fun test2() {
    given {
        commonContext()
    } and {
        3
    } `when` {
        action.sum(results) // 1 + 2 + 3
    } then {
        assertEquals(1 + 2 + 3, it)
    }
}
```

> All nested context will be merged into the current context.
>
> With this approach, you will be able to factorize common assumptions in your test code.
> They can also be shared between acceptance tests and unit test.

## Use your custom DSL

Kotlin-TDD allows to have access to your own *Assumption*, *Action* and *Assertion* instance in each step. This is very
convenient to organize your tests in function of what it produces. 

Let's say you have the following interface defining the core functionnality of your application you are currently building :


```kotlin
interface API {
    fun createTodoList(): TodoList
    fun createItem(name: String): Item
    fun addItem(todoList: TodoList, item: Item): TodoList
}
```


This interface provides methods to create a new todo list, create a new item, and add an item to an existing todo list. 

- Let's create a new class Assumption that will contain operations inside Given step:

```kotlin
class Assumption(private val api: API) : Step() {
	val `a todo list` get() = api.createTodo()

    fun `an item`(name: String) = api.createItem(name)
}
```
> Note that we are reusing the interface API defined above.

- Create an Action class that will contain all possible operations inside the When step:

```kotlin
class Action(private val api: API) : Step() {

  val `I add the last item into my todo list` get() = api.addItem(
    first<TodoList>(), 
    last<Item>()
  )
}
```

- Create an Assertion class for all possible operations inside Then step: 

```kotlin
class Assertion : Step() {
    val `I expect this item is present in my todo list`
        get() = Assertions.assertTrue {
            first<TodoList>().items.contains(
                last<Item>()
            )
        }
}
```
**Given When Then**
Create a file named UnitTest.kt for example and extends the class GivenWhenThen:

```kotlin 
// src/test/kotlin/com/example/kotlintdd/UnitTest.kt
package com.example.kotlintdd
// Implement Test API
val api = object: API {
  override fun createTodoList() = TodoList()

    override fun createItem(name: String) = Item(name)

    override fun addItem(todoList: TodoList, item: Item) = todoList.apply { 
    	add(item)
    }
}
// Use GivenWhenThen Pattern
object UnitTest : GivenWhenThen<Assumption, Action, Assertion>(
    assumption = Assumption(),
    action = Action(),
    assertion = Assertion()
)

// defines the entrypoint on file-level to be automatically recognized by your IDE
fun <R> given(block: Assumption.() -> R) = UnitTest.given(block)
fun <R> `when`(block: Action.() -> R) = UnitTest.`when`(block)
```


**Assume Act Assert**
Or if you want to use Assume Act Assert Pattern, this time you will have to extend the class `AssumeActAssert`:

````kotlin
// UnitTest.kt
// Implement Test API
val api = object: API {
  override fun createTodoList() = TodoList()

    override fun createItem(name: String) = Item(name)

    override fun addItem(todoList: TodoList, item: Item) = todoList.apply { 
    	add(item)
    }
}
object UnitTest : AssumeActAssert<Assumption, Action, Assertion>(
    assumption = Assumption(api),
    action = Action(api),
    assertion = Assertion()
)

// defines the entrypoint on file-level to be automatically recognized by your IDE
fun <R> assume(block: Assumption.() -> R) = UnitTest.assume(block)
fun <R> act(block: Action.() -> R) = UnitTest.act(block)
````

In the various examples we saw, the step do not write in a natural language. Thanks to powerful extendability of Kotlin,
we can provide our custom DSL.

Here is the example **without DSL** of the TodoList test.

```kotlin
@Test
fun `I should be able to insert a new item in my todo list`() {
    given {
        TodoList()
    } and {
        Item("Eat banana")
    } `when` {
        first<TodoList>().add(it)
    } then {
        assertTrue(first<TodoList>().contains(first<Item>()))
    }
}
```

We can rewrite it with a custom DSL

````kotlin
@Test
fun `I should be able to insert a new item in my todo list`() {
    given {
        `a todo list`
    } and {
        `an item`("Eat banana")
    } `when` {
        `I add the last item into my todo list`
    } then {
        `I expect this item is present in my todo list`
    }
}
````

And your DSL can be written like this

````kotlin
// Assumption.kt
class Assumption : Step() {
    val `a todo list` get() = TodoList()

    fun `an item`(name: String) = Item(name)
}

// Action.kt
class Action : Step() {
    val `I add the last item into my todo list`
        get() =
            last<TodoList>().items.add(last())
}

// Assertion.kt
class Assertion : Step() {
    val `I expect this item is present in my todo list`
        get() = Assertions.assertTrue {
            last<TodoList>().items.contains(
                last()
            )
        }
}

````

# Migrate from v1 to v2
In version 2, the DSL has been improved for better readability and usability, especially when you are using your own custom DSL.

Replace all extensions of GWTContext like
```kotlin
val GWTContext<*, *>.`I add the last item into my todo list` get() = 
    first<TodoList>().add(last<Item>())
```

to

```kotlin
val Step.`I add the last item into my todo list` get() = 
    first<TodoList>().add(last<Item>())
```

# License

MIT License
