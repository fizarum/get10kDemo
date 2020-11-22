package com.fizarum.get10kusd.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fizarum.get10kusd.data.db.entities.UserEntity
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface UserDao {
    @Query("select * from user")
    fun getAll(): Single<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: UserEntity): Completable
}