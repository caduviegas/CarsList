package io.github.caduviegas.carslist.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.caduviegas.carslist.db.entity.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Query("SELECT EXISTS(SELECT 1 FROM user LIMIT 1)")
    fun hasLoggedUser(): Boolean

    @Query("SELECT * FROM user LIMIT 1")
    fun getLoggedUser(): User

    @Query("DELETE FROM user WHERE cpf = :cpf")
    fun deleteUserByCpf(cpf: String)
}