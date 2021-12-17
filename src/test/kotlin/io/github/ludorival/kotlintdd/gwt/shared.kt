package io.github.ludorival.kotlintdd.gwt

import io.github.ludorival.kotlintdd.Action
import io.github.ludorival.kotlintdd.GWTContext
import io.github.ludorival.kotlintdd.gwt.given as Given

fun someUseCaseWithoutExtension() = Given {
    1
} and {
    2
} and {
    3
}

fun someNestedUseCaseWithoutExtension() = Given {
    5
} and {
    someUseCaseWithoutExtension()
} and {
    6
}

fun GWTContext<Action, *>.someUseCase() = given {
    1
} and {
    2
} and {
    3
}

fun GWTContext<Action, *>.someNestedUseCase() = given {
    5
} and {
    someUseCase()
} and {
    6
}
