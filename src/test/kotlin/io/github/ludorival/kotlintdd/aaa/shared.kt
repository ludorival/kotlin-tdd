package io.github.ludorival.kotlintdd.aaa

import io.github.ludorival.kotlintdd.AAAContext
import io.github.ludorival.kotlintdd.Action
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

fun AAAContext<Action, *>.someUseCase() = assume {
    1
} and {
    2
} and {
    3
}

fun AAAContext<Action, *>.someNestedUseCase() = assume {
    5
} and {
    someUseCase()
} and {
    6
}
