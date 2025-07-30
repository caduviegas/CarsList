package io.github.caduviegas.carslist.data.db.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import io.github.caduviegas.carslist.db.entity.User
import io.github.caduviegas.carslist.db.CarsDatabase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.time.LocalDate

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class UserDaoTest {

    private lateinit var db: CarsDatabase
    private lateinit var userDao: UserDao

    @Before
    fun setUp() {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, CarsDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        userDao = db.userDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun `should insert user and get logged user`() {
        val user = User(
            cpf = "12345678900",
            name = "João da Silva",
            email = "joao@email.com",
            phone = "11999999999",
            birthday = LocalDate.of(1990, 3, 15)
        )
        userDao.insertUser(user)
        val result = userDao.getLoggedUser()
        assertThat(result).isEqualTo(user)
    }

    @Test
    fun `should return true for hasLoggedUser when user exists`() {
        val user = User(
            cpf = "12345678900",
            name = "João da Silva",
            email = "joao@email.com"
        )
        userDao.insertUser(user)
        val result = userDao.hasLoggedUser()
        assertThat(result).isTrue()
    }

    @Test
    fun `should replace user on conflict when inserting`() {
        val user1 = User("123", "A", "a@email.com")
        val user2 = User("123", "B", "b@email.com")
        userDao.insertUser(user1)
        userDao.insertUser(user2)
        val result = userDao.getLoggedUser()
        assertThat(result).isEqualTo(user2)
    }

    @Test
    fun `should insert user with null fields`() {
        val user = User(
            cpf = "999",
            name = "No Phone",
            email = "no@email.com",
            phone = null,
            birthday = null
        )
        userDao.insertUser(user)
        val result = userDao.getLoggedUser()
        assertThat(result).isEqualTo(user)
        assertThat(result.phone).isNull()
        assertThat(result.birthday).isNull()
    }

    @Test
    fun `should return false for hasLoggedUser when no user exists`() {
        val result = userDao.hasLoggedUser()
        assertThat(result).isFalse()
    }

    @Test
    fun `should remove user by cpf`() {
        val user = User(
            cpf = "12345678900",
            name = "João da Silva",
            email = "joao@email.com"
        )
        userDao.insertUser(user)
        userDao.deleteAllUsers("12345678900")
        val hasUser = userDao.hasLoggedUser()
        assertThat(hasUser).isFalse()
    }
}