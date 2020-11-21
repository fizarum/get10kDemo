package com.fizarum.get10kusd.domain.usecases

abstract class SimpleUseCase<T, in Params> : UseCase() {

    internal abstract fun buildUseCaseSimple(params: Params): T

    fun execute(params: Params): T {
        return buildUseCaseSimple(params)
    }
}