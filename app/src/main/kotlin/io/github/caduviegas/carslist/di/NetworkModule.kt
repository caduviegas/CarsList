package io.github.caduviegas.carslist.di

import io.github.caduviegas.carslist.data.api.CarApiService
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single { OkHttpClient.Builder().build() }
    single {
        Retrofit.Builder()
            .baseUrl("https://wswork.com.br/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single<CarApiService> { get<Retrofit>().create(CarApiService::class.java) }
}