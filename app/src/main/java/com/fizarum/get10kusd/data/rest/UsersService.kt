package com.fizarum.get10kusd.data.rest

import com.fizarum.get10kusd.data.rest.entities.UserListDTO
import io.reactivex.Single
import retrofit2.http.GET

interface UsersService {

    @GET("5c4650753100004c0005f340")
    fun getUserList(): Single<UserListDTO>
}