package io.github.ludorival.kotlintdd.aaa

import io.github.ludorival.kotlintdd.AAAContext
import io.github.ludorival.kotlintdd.Action
import io.github.ludorival.kotlintdd.aaa.assume as Assume

fun someUseCase() = Assume {
    1
} and {
    2
} and {
    3
}

fun someNestedUseCase() = Assume {
    5
} and {
    someUseCase()
} and {
    6
}

fun AAAContext<Action, *>.someUseCaseWithExtension() = assume {
    1
} and {
    2
} and {
    3
}

fun AAAContext<Action, *>.someNestedUseCaseWithExtension() = assume {
    5
} and {
    someUseCaseWithExtension()
} and {
    6
}
