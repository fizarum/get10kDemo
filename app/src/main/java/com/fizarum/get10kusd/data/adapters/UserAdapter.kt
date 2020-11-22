package com.fizarum.get10kusd.data.adapters

import com.fizarum.get10kusd.data.db.entities.UserEntity
import com.fizarum.get10kusd.data.rest.entities.UserDTO
import com.fizarum.get10kusd.domain.entities.User

object UserAdapter {

    fun fromDTO(userDTO: UserDTO): User {
        return with(userDTO) {
            User(id, name = firstName, lastName, avatarUrl = photoUrl, dailyWage)
        }
    }

    fun toDBEntity(user: User): UserEntity {
        return UserEntity(user.id, user.dailyWage)
    }

    // because real user data should be fetched from rest source
    // and we have to store only user id & dailyWage
    // transformation works in that way. If there is no performance issue
    // user object can be saved/loaded entirely to/from db
    fun fromDBEntity(entity: UserEntity): User {
        return User(
            id = entity.userId,
            name = "",
            lastName = "",
            avatarUrl = "",
            dailyWage = entity.dailyWage
        )
    }
}