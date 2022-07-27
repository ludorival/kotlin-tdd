package io.github.ludorival.kotlintdd.gwt

import io.github.ludorival.kotlintdd.gwt.given as Given

fun someUseCase() = Given {
    1
} and {
    2
} and {
    3
}

fun someNestedUseCase() = Given {
    5
} and {
    someUseCase()
} and {
    6
}


