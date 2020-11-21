package com.fizarum.get10kusd.domain.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val id: String,
    val name: String,
    val lastName: String,
    val avatarUrl: String,
    val dailyWage: Int
) : Parcelable {

    val shortName = name.getOrElse(0) { '_' }

    val shortFullName = "${shortName}. $lastName"
}