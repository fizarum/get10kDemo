package com.fizarum.get10kusd.domain.entities

data class User(
    val id: String,
    val name: String,
    val lastName: String,
    val avatarUrl: String,
    val dailyWage: Int
) {

    val shortName = name.getOrElse(0) { '_' }

    val shortFullName = "${shortName}. $lastName"
}