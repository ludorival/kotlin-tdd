package com.ludorival.kotlintdd

/**
 * The interface providing the Test Driven Design pattern like GWT (Given When Then) or AAA (Assume Act Assert) in your test.
 * Implements this interface in an object file like
 * ```
 *     // UnitTest.kt
 *     package com.example.test
 *     object UnitTest: KotlinTDD<MockAction> {
 *        val action: MockAction
 *     }
 * ```
 *
 * > You need to provide a type action which can be anything.
 * > It allows to have access to this type inside a step block (`given`, `when`, `then` or `assume`, `act`, `assert`).
 * > Refer the readme for an example of use. Otherwise if you don't use it you can replace by `Unit` like `object UnitTest: KotlinTDD<Unit>`.
 *
 * Then in your unit test, you can use it like this
 * ```
 * package com.example.test
 *
 * import com.example.test.UnitTest.*
 *
 * class MyTest {
 *
 *    @Test
 *    fun `should equal to 3`() {
 *      given {
 *          1
 *      } and {
 *          2
 *      } `when` {
 *          first<Int>() + last<Int>()
 *      } then {
 *          assertTrue(result == 3)
 *      }
 *    }
 * }
 * ```
 */
interface KotlinTDD<A> {

    val action: A

    private fun <T> initializeState(block: State<A, Unit>.() -> T): State<A, T> {
        return State(action, Unit).and(block)
    }

    //#region Given When Then pattern
    fun <T> given(block: State<A, Unit>.() -> T) = initializeState(block)

    fun <T> `when`(block: State<A, Unit>.() -> T) = initializeState(block)
    //#endregion


    //#region Assume Act Assert pattern
    fun <T> assume(block: State<A, Unit>.() -> T) = initializeState(block)

    fun <T> act(block: State<A, Unit>.() -> T) = initializeState(block)
    //#endregion
}
