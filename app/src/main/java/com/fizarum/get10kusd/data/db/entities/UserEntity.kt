package com.fizarum.get10kusd.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey val userId: String,

    val dailyWage: Int
)