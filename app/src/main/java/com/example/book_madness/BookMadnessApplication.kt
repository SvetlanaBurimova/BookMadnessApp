package com.example.book_madness

import android.app.Application
import com.example.book_madness.data.AppContainer
import com.example.book_madness.data.AppDataContainer

class BookMadnessApplication : Application() {
    lateinit var  container: AppContainer

    // Dependency is active al long as app is active
    // Initialization when app is created
    override fun onCreate() {
        super.onCreate()
        // Will be used the same instance of dependency (BooksRepository) across all classes
        container = AppDataContainer(this)
    }
}
