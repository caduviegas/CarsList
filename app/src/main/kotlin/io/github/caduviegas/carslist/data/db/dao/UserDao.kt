package io.github.caduviegas.carslist.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.caduviegas.carslist.data.db.entity.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT EXISTS(SELECT 1 FROM user LIMIT 1)")
    suspend fun hasLoggedUser(): Boolean

    @Query("SELECT * FROM user LIMIT 1")
    suspend fun getLoggedUser(): User

    @Query("DELETE FROM user")
    suspend fun deleteAllUsers()
}