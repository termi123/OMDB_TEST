package com.example.omdb

import android.app.Application
import com.example.omdb.di.APIProvider
import com.example.omdb.di.RepositoryProvider
import com.example.omdb.di.RetrofitProvider
import com.example.omdb.di.ViewModelProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(
                listOf(
                    RepositoryProvider.getModules(),
                    ViewModelProvider.getModules(),
                    RetrofitProvider.getModules(this@MyApplication),
                    APIProvider.getModules()
                )
            )
        }
    }
}