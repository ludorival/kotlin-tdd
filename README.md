Kotlin Test Driven Design
=========================
![Build status](https://github.com/ludorival/kotlin-tdd/actions/workflows/build.yaml/badge.svg)
![Publish status](https://github.com/ludorival/kotlin-tdd/actions/workflows/publish.yaml/badge.svg)
![Maven Central](https://img.shields.io/maven-central/v/io.github.ludorival/kotlin-tdd)
> Lightweight library suggesting a TDD implementation with Kotlin

Kotlin-TDD provides a way to write your unit test (or acceptance test)
as you will write an acceptance criteria in natural english language.

Write your test by following the Given When Then pattern.

````kotlin
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
````

Or by using the Assume Act Assert pattern

````kotlin
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
````

# Getting started

Kotlin-TDD is available via Maven Central. Just add the dependency to your Maven POM or Gradle build config.

> Look at the latest version on the Version badge on top of this page

**Gradle**

```groovy
testCompile "io.github.ludorival:kotlin-tdd:$kotlinTddVersion"
```

**Maven**

````xml
<dependency>
<groupId>io.github.ludorival</groupId>
<artifactId>kotlin-tdd</artifactId>
<version>version</version>
<scope>test</scope>
</dependency>
````

# Usage

Kotlin-TDD provides a useful mechanism to save the test context **without managing static values**. Each step can
perform an **action** changing the next context.

Kotlin-TDD allows to have access to an *Action* instance in each step. This is very convenient when you want to provide
a different implementation when you are running in Unit Test or Acceptance/Integration test.

Let's create a new class Action for example

````kotlin
class Action {
    
    fun sum(value1: Int, value2: Int) = value1 + value2

    fun sum(list: List<Int>) = list.reduce(Int::plus)

    fun divide(list: List<Int>) = list.reduce(Int::div)
    
}
````

According to your flavor (GWT or AAA pattern), you will have to implement a dedicated interface.

**Given When Then**

Create a file named `UnitTest.kt` for example and implements the interface `GivenWhenThen`:

````kotlin
// UnitTest.kt

object UnitTest : GivenWhenThen<Action> {
    override val action: Action = Action()
}
// defines the entrypoint on file-level to be automatically recognized by your IDE
fun <R> given(block: GWTContext<Action, Unit>.() -> R) = UnitTest.given(block)
fun <R> `when`(block: GWTContext<Action, Unit>.() -> R) = UnitTest.`when`(block)
````

**Assume Act Assert**

This time you will have to implement the interface `GivenWhenThen`:

````kotlin
// UnitTest.kt

object UnitTest : AssumeActAssert<Action> {
    override val action: Action = Action()
}
// defines the entrypoint on file-level to be automatically recognized by your IDE
fun <R> assume(block: AAAContext<Action, Unit>.() -> R) = UnitTest.assume(block)
fun <R> act(block: AAAContext<Action, Unit>.() -> R) = UnitTest.act(block)
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
        first<TodoList>().add(result)
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
           result + 2 // result is 1
        } then {
            assertEquals(3, result) // result is 3
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
        }`when` {
           first<Int>() + result // first<Int>() -> 1
        } then {
            assertEquals(3, result) // result is 3
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
        }`when` {
           first<Int>{ it > 1 } + result // first<Int>{ it > 1 } -> 2
        } then {
            assertEquals(2 + 3, result) // result is 5
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
            assertEquals(3, result) // result is 3
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
        }`when` {
           result + last<Int> { it < 3 } // 3 + 2
        } then {
            assertEquals(3 + 2, result) // result is 5
        }
    }
```

## Get a result at given position (`get<T>`)

If you know exactly in which step to get your result, you can use `get<T>(index)` to access it.

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
        }`when` {
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
        }`when` {
           results<Int>().reduce(Int::plus) // results -> [1, 2]
        } then {
            assertEquals(3, result)
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
        }`when` {
           reversedRsults<Int>().reduce(Int::plus) // results -> [2, 1]
        } then {
            assertEquals(3, result)
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
            assertEquals(1 + 2, result)
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
        }`when` {
           action.sum(results) // 1 + 2 + 3
        } then {
            assertEquals(1 + 2 + 3, result)
        }
    }
```

Let's create an extended function which factorize common code:

```kotlin

    fun GWTContext<Action, *>.commonContext() = 
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
            assertEquals(1 + 2, result)
        }
    }
    @Test
    fun test2() {
        given { 
            commonContext()  
        } and {
            3     
        }`when` {
           action.sum(results) // 1 + 2 + 3
        } then {
            assertEquals(1 + 2 + 3, result)
        }
    }
```

> All nested context will be merged into the current context.
>
> With this approach, you will be able to

## Use your custom DSL

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
        first<TodoList>().add(result)
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
// DSL.kt

val `a todo list` get() = TodoList()

fun `an item`(name: String) = Item(name)

val GWTContext<*, *>.`I add the last item into my todo list` get() = 
    first<TodoList>().add(last<Item>())

val GWTContext<*, *>.`I expect this item is present in my todo list` get() =
    assertTrue(first<TodoList>().contains(last<Item>()))
````
