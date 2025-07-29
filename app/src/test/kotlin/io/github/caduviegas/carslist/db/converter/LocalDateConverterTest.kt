package io.github.caduviegas.carslist.db.converter

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.LocalDate

class LocalDateConverterTest {

    private val converter = LocalDateConverter()

    @Test
    fun `fromLocalDate should convert LocalDate to epoch millis`() {
        val localDate = LocalDate.of(2024, 6, 15)
        val expectedMillis = 1718409600000L
        val result = converter.fromLocalDate(localDate)
        assertThat(result).isEqualTo(expectedMillis)
    }

    @Test
    fun `fromLocalDate should return null when input is null`() {
        val result = converter.fromLocalDate(null)
        assertThat(result).isNull()
    }

    @Test
    fun `toLocalDate should convert epoch millis to LocalDate`() {
        val millis = 1718409600000L
        val expectedDate = LocalDate.of(2024, 6, 15)
        val result = converter.toLocalDate(millis)
        assertThat(result).isEqualTo(expectedDate)
    }

    @Test
    fun `toLocalDate should return null when input is null`() {
        val result = converter.toLocalDate(null)
        assertThat(result).isNull()
    }
}