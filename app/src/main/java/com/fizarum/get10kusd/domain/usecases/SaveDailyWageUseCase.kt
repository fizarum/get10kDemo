package com.fizarum.get10kusd.domain.usecases

import com.fizarum.get10kusd.domain.entities.User
import com.fizarum.get10kusd.domain.repositories.UserRepository
import io.reactivex.Completable

class SaveDailyWageUseCase(private val repo: UserRepository) : CompletableUseCase<User>() {
    override fun buildUseCaseCompletable(params: User): Completable {
        return repo.saveUser(params)
    }
}