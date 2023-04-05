package com.example.omdb.di

import com.example.omdb.repository.remote.MovieServices
import org.koin.dsl.module
import retrofit2.Retrofit

object APIProvider {

    fun getModules() = module(override = true) {
        fun provideUseApi(retrofit: Retrofit): MovieServices {
            return retrofit.create(MovieServices::class.java)
        }

        single { provideUseApi(get()) }
    }
}