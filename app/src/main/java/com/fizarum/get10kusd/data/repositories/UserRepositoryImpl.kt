package com.fizarum.get10kusd.data.repositories

import com.fizarum.get10kusd.data.adapters.UserAdapter
import com.fizarum.get10kusd.data.db.AppDatabase
import com.fizarum.get10kusd.data.rest.RestClient
import com.fizarum.get10kusd.data.rest.UsersService
import com.fizarum.get10kusd.domain.entities.User
import com.fizarum.get10kusd.domain.repositories.UserRepository
import io.reactivex.Completable
import io.reactivex.Single

class UserRepositoryImpl(restClient: RestClient, private val db: AppDatabase) : UserRepository {

    private val userService = restClient.retrofit.create(UsersService::class.java)

    override fun getUsers(): Single<List<User>> {
        return userService.getUserList().map { response ->
            response.list.map { userDTO ->
                UserAdapter.fromDTO(userDTO)
            }
        }
    }

    override fun saveUser(user: User): Completable {
        return with(UserAdapter.toDBEntity(user)) {
            db.userDao().insert(this)
        }
    }

    override fun loadAllUsers(): Single<List<User>> {
        return db.userDao().getAll().map { dbList ->
            dbList.map { entity ->
                UserAdapter.fromDBEntity(entity)
            }
        }
    }
}