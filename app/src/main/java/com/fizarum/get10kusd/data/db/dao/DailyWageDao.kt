package com.fizarum.get10kusd.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fizarum.get10kusd.data.db.entities.UserEntity
import io.reactivex.Completable
import io.reactivex.Single

//todo rename to UserDao!
@Dao
interface DailyWageDao {
    @Query("select * from user")
    fun getAll(): Single<List<UserEntity>>

    @Query("select * from user where userId = :userId limit 1")
    fun getById(userId: String): Single<UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: UserEntity): Completable
}