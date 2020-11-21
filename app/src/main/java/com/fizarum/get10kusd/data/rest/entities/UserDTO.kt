package com.fizarum.get10kusd.data.rest.entities

import com.google.gson.annotations.SerializedName

data class UserDTO(
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("name")
    val firstName: String,

    @field:SerializedName("lastName")
    val lastName: String,

    @field:SerializedName("picture")
    val photoUrl: String,

    @field:SerializedName("dailyWage")
    val dailyWage: Int,
)