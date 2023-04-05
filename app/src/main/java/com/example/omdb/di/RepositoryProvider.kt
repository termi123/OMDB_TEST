package com.example.omdb.di

import com.example.omdb.repository.MovieRepository
import com.example.omdb.repository.MovieRepositoryImpl
import org.koin.dsl.module

object RepositoryProvider {

    fun getModules() = module(override = true) {
        single<MovieRepository> {
            MovieRepositoryImpl(get())
        }
    }
}