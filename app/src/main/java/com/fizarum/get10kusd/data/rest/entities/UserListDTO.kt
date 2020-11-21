package com.fizarum.get10kusd.data.rest.entities

import com.google.gson.annotations.SerializedName

data class UserListDTO(
    @field:SerializedName("userList")
    val list: List<UserDTO>
)