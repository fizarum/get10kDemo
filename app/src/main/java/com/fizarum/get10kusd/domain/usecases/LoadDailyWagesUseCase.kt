package com.fizarum.get10kusd.domain.usecases

import com.fizarum.get10kusd.domain.entities.User
import com.fizarum.get10kusd.domain.repositories.UserRepository
import io.reactivex.Single

class LoadDailyWagesUseCase(private val repo: UserRepository): SingleUseCase<List<User>, Nothing?>() {

    override fun buildUseCaseSingle(params: Nothing?): Single<List<User>> {
        return repo.loadAllUsers()
    }
}