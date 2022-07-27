package io.github.ludorival.kotlintdd.aaa

import io.github.ludorival.kotlintdd.aaa.assume as Assume

fun someUseCaseWithoutExtension() = Assume {
    1
} and {
    2
} and {
    3
}

fun someNestedUseCaseWithoutExtension() = Assume {
    5
} and {
    someUseCaseWithoutExtension()
} and {
    6
}

