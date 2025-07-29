package io.github.caduviegas.carslist.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class User(
    @PrimaryKey
    @ColumnInfo(name = "cpf") val cpf: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "phone") val phone: String? = null,
    @ColumnInfo(name = "birthday") val birthday: LocalDate? = null
)