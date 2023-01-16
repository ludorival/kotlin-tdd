package io.github.ludorival.kotlintdd.coroutine.gwt

import io.github.ludorival.kotlintdd.coroutine.gwt.given as Given

suspend fun someUseCase() = Given {
    1
} and {
    2
} and {
    3
}

suspend fun someNestedUseCase() = Given {
    5
} and {
    someUseCase()
} and {
    6
}


