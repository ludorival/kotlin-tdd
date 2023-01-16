package io.github.ludorival.kotlintdd.coroutine.aaa

import io.github.ludorival.kotlintdd.coroutine.aaa.assume as Assume

suspend fun someUseCaseWithoutExtension() = Assume {
    1
} and {
    2
} and {
    3
}

suspend fun someNestedUseCaseWithoutExtension() = Assume {
    5
} and {
    someUseCaseWithoutExtension()
} and {
    6
}

