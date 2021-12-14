I will start this article by asking you two questions :

1. Do you like writing Tests ?

![](https://dz2cdn1.dzone.com/storage/temp/15452495-1639481749690.gif)

2. Do you practice TDD in your development ?

![](https://dz2cdn1.dzone.com/storage/temp/15452502-1639482028607.gif)

Most of you, me included, I was considering testing as a luxe for developers since this is the first task we sacrifice
when we try to build a Minimum Viable Product.

But we are wrong to do that because Tests

* reduce possibles production regressions
  * What saves us time when searching the cause
* validate functional design
  * Make sure we respect the target solution
* increase the overall quality of your program
  * The program is more reliable and we are more confident to push a new version on production

Well, ok we are agree to do more test but TDD is this something applicable in a daily practice ?

TDD as  Test Driven Development 
--------------------------------

Test Driven Development refers to a style of programming favoring the testing before writing the production code.
Basically you have to follow those steps:

* write Test
* run Test (the test should fail)
* write Production Code
* run Test (the test should pass)
* refactor until the code is conform
* repeat, “accumulating” unit tests over time

_If you want to know more about TDD, there are a tons of articles related to this topic explained in detail the benefits
of TDD (for example [What is Test Driven Development (TDD)?](https://www.agilealliance.org/glossary/tdd/))._

There are many ways to implement TDD like Unit Testing, Feature testing, etc.

However, in practice, this is not really natural and we have to struggle with ourself to really use it.

Kotlin for TDD ?
----------------

Kotlin has many advantages prior of Java for writing TDD. Particularly :

* Kotlin reduces java boilerplate thanks to function extension which can improve your test readability
* Kotlin supports the infix notation allowing to write a code more in natural english

 ```kotlin

        val canOrder = user.isAuthenticated and user.hasACreditCard
 ```

* Kotlin supports backticks function name, really convenient to write tests

 ```kotlin

        class MyTestCase {
             @Test fun `ensure everything works`() { /*...*/ }
        
             @Test fun ensureEverythingWorks_onAndroid() { /*...*/ }
        }
 ```

* Kotlin is interoperable with Java meaning that you can use Kotlin to write your tests whereas your production code is
  written in Java

That's why I consider Kotlin as a best choice to implement TDD. It came to me an idea to suggest a library to setup a
TDD environment.

> [Kotlin TDD](https://github.com/ludorival/kotlin-tdd)

This library will help you to write any test like

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

Seems cool, right ?

Actually, Kotlin-TDD provides two flavour

* `GivenWhenThen` exposing the `given`, `and`, `when` and `then` infix functions
* `AssumeActAssert` exposing the `assume`, `and`, `act` and `assert` infix functions

Indeed the same test above can be written by following the AAA pattern

```kotlin
    @Test
    fun `I should be able to insert a new item in my todo list`() {
        assume {
            `a todo list`
        } and {
            `an item`("Eat banana")
        } act {
            `I add the last item into my todo list`
        } assert {
            `I expect this item is present in my todo list`
        }
    }
```

Let's try to use it in a concrete example.

Let's assume you have a requirement to create a Todo List application and one of the acceptance criteria is

> As a user, I should see my new item when it has been added in my Todo list

We will try to practice TDD thanks to this library.

### 1 - Setup Kotlin-TDD

Let's start by importing this dependancy in your project.

_I assume Junit 5 is installed in your project._

**With gradle**

```groovy
testCompile "io.github.ludorival:kotlin-tdd:1.1.0"
```

or **with Maven**

```xml
    <dependency>
    <groupId>io.github.ludorival</groupId>
    <artifactId>kotlin-tdd</artifactId>
    <version>1.1.0</version>
    <scope>test</scope>
    </dependency>
```

### 2 - Write your interface Action

Inside of each step, you can have an access to a field named action. It has no really use in the library but it allows
you to use a common instance throughout your tests. Here we will used to expose different possible actions in the
application.

```kotlin
    // src/test/kotlin/com/example/kotlintdd/Action.kt
    package com.example.kotlintdd
    
    interface Action {
      
      fun createTodoList(): TodoList
      
      fun createItem(name: String): Item
      
      fun addItem(todoList: TodoList, item: Item): TodoList
    }
```

Don't worry if you see compilation errors, it's part of the TDD process ;). Indeed compilation failure is considered as
a test failure in TDD.

### 3 - Configure an instance of GivenWhenThen to use in all our unit tests.

```kotlin
    // src/test/kotlin/com/example/kotlintdd/UnitTest.kt
    package com.example.kotlintdd
      
    import io.github.ludorival.kotlintdd.GWTContext // it is an alias of GivenWhenThen.Context
    import io.github.ludorival.kotlintdd.GivenWhenThen
      
    object UnitTest : GivenWhenThen<Action> {
        override val action: Action = object: Action {
            override fun createTodoList() = TodoList()
            override fun createItem(name: String) = Item(name)
          	override fun addItem(todoList: TodoList, item: Item) = todoList.add(item)
        }
    }
    // defines the entrypoint on file-level to be automatically recognized by your IDE
    fun <R> given(block: GWTContext<Action, Unit>.() -> R) = UnitTest.given(block)
    fun <R> `when`(block: GWTContext<Action, Unit>.() -> R) = UnitTest.`when`(block)
```

### 4 - Write your custom DSL

> A Domain Specific Language  is a computer language specialized to a particular application domain. 

This step is facultative but it helps to describe your action in a natural language.

Here we will create a file which will host this DSL.

```kotlin
    // src/test/kotlin/com/example/kotlintdd/DSL.kt
    package com.example.kotlintdd
    
    import io.github.ludorival.kotlintdd.GWTContext
    
    val GWTContext<*, *>.`a todo list` get() = action.createTodoList()
    
    fun GWTContext<*, *>.`an item`(name: String) = action.createItem(name)
    
    val GWTContext<*, *>.`I add the last item into my todo list` get() = 
        action.addItem(first<TodoList>(), last<Item>())
    
    val GWTContext<*, *>.`I expect this item is present in my todo list` get() =
        assertTrue(first<TodoList>().contains(last<Item>()))
```

Here we are extending the GivenWhenThen.Context by adding our own custom DSL.

Notice that you have access to the action instance allow us to call our respective action.

Check at line 11 & 14 the use of `first<TodoList>()` and `last<Item>()` functions. Those functions come with the Context
and store all previous context of each Step:

* the `first<TodoList>` allows to fetch the first `TodoList` instance returned by a step
* the `last<Item>()` allows to get the last `Item` instance returned by a step.

Note that the `TodoList`, `Item` can be removed at line 11 since it is inferred, given `action.addItem(first(), last())`
.

You can see all available functions in the [documentation](https://github.com/ludorival/kotlin-tdd#features).

### 5 - Write your unit test

Now we have configured our TDD and our custom DSL, let's put it all together in a test

```kotlin
    // src/test/kotlin/com/example/kotlintdd/TodoListTest
    package com.example.kotlintdd
    
    import org.junit.jupiter.api.Test
    
    class TodoListTest {
      
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
    }
```

### 6 - Run the test

Of course, the test is failing due to compilation error. We did not write any production code. Don't worry this is the
part of TDD process.

###  7 - Write production code

Now it is time to make the test green.

* Create the Item class

```kotlin
    // src/main/kotlin/com/example/kotlintdd/Item
    package com.example.kotlintdd
    
    data class Item(val name: String) 
```

* Create the TodoList class

```kotlin
    // src/main/kotlin/com/example/kotlintdd/TodoList
    package com.example.kotlintdd
    
    class TodoList {
    	val list = mutableListOf()
      
      	fun add(item: Item) {
          list.add(item)
          return this
        }
      
      	fun contains(item: Item) = list.contains(item)
    }
```

### 8 - Run the test again

Bravo, your test is Green !

### 9 - Next steps : Acceptance Test

We can continue to add more tests by combining the Given When Then pattern and our custom DSL. The DSL can be enhanced
for more use cases.

The advantage of Kotlin-TDD is that you can reuse the same process for writing Acceptance Test as well.

Let's assume that you have a **Spring application** where the TodoList and Item are saved in a database.

The creation and the update should be done through the database for those entities.

We expect to have three endpoints in our Rest API

    POST /v1/todo/list // Create a new Todo list -> return the TodoList with an id
    POST /v1/todo/item // Create a new Item -> return the Item with an id
    PUT /v1/todo/list/{listId}/add/{itemId} // add the item defined by {itemId} in the list {listId} 

We can write a different implementation of our Action interface

```kotlin
    // src/test/kotlin/com/example/kotlintdd/acceptance/RestActions.kt
    package com.example.kotlintdd.acceptance
    
    import com.example.kotlintdd.Action
    import com.example.kotlintdd.Item
    import com.example.kotlintdd.TodoList
    import org.springframework.http.HttpEntity
    import org.springframework.http.HttpMethod
    import org.springframework.http.ResponseEntity
    import org.springframework.web.client.RestTemplate
    
    class RestAction : Action {
    
        private val url = "http://localhost:8080/spring-rest/v1"
        private val restTemplate = RestTemplate()
        override fun createTodoList(): TodoList {
            val request = HttpEntity(TodoList())
            val response: ResponseEntity<TodoList> = restTemplate
                .exchange("$url/todo", HttpMethod.POST, request, TodoList::class.java)
            return response.body!!
        }
    
        override fun createItem(name: String): Item {
            val request = HttpEntity(Item(name))
            val response: ResponseEntity<Item> = restTemplate
                .exchange("$url/item", HttpMethod.POST, request, Item::class.java)
            return response.body!!
        }
    
        override fun addItem(todoList: TodoList, item: Item): TodoList {
            val response: ResponseEntity<TodoList> = restTemplate
                .exchange(
                    "$url/todo/${todoList.id}/add/${item.id}",
                    HttpMethod.PUT,
                    HttpEntity(null, null),
                    TodoList::class.java
                )
            return response.body!!
        }
    
    }
```

And you need to setup this new action for a different instance of GivenWhenThen

```kotlin
    // src/test/kotlin/com/example/kotlintdd/acceptance/AcceptanceTest.kt
    package com.example.kotlintdd.acceptance
    
    import com.example.kotlintdd.Action
    import io.github.ludorival.kotlintdd.GWTContext
    import io.github.ludorival.kotlintdd.GivenWhenThen
    
    object AcceptanceTest: GivenWhenThen<Action> {
        override val action: Action = RestAction()
    
    }
    fun <R> given(block: GWTContext<Action, Unit>.() -> R) = AcceptanceTest.given(block)
```

Then I can literally copy my unit test as an acceptance test

```kotlin
    // src/test/kotlin/com/example/kotlintdd/acceptance/TodoListAT.kt
    package com.example.kotlintdd.acceptance
    
    import org.junit.jupiter.api.Test
    import org.springframework.boot.test.context.SpringBootTest
    
    @SpringBootTest
    class TodoListAT {
    
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
    }
```

Of course we can factorize into a common function but you are beginning to see the magic !

Conclusion
----------

We saw a concrete example how to use Kotlin-TDD to implement TDD technique. We see that by writing a custom DSL, we
improve the readability of our test and it becomes easy to understand a test even after several months. Then we saw that
we can reuse the same process for Acceptance test without changing the way we build our test.

Now it is your turn to adopt it and please share your feedback directly in
the [Github repo](https://github.com/ludorival/kotlin-tdd).

Thanks for reading !
