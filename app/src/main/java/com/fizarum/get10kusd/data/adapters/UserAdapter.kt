package com.fizarum.get10kusd.data.adapters

import com.fizarum.get10kusd.data.rest.entities.UserDTO
import com.fizarum.get10kusd.domain.entities.User

object UserAdapter {

    fun fromDTO(userDTO: UserDTO): User {
        return with(userDTO) {
            User(id, name = firstName, lastName, avatarUrl = photoUrl, dailyWage)
        }
    }
}