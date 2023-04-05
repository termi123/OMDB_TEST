package com.example.omdb.di

import com.example.omdb.ui.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ViewModelProvider {

    fun getModules() = module(override = true) {
        viewModel {
            SearchViewModel(get())
        }
    }
}