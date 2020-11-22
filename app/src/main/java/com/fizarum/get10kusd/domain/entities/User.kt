package com.fizarum.get10kusd.domain.entities

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val id: String,
    val name: String,
    val lastName: String,
    val avatarUrl: String,
    val dailyWage: Int
) : Parcelable {

    @IgnoredOnParcel
    val shortName = name.getOrElse(0) { '_' }

    @IgnoredOnParcel
    val shortFullName = "${shortName}. $lastName"
}