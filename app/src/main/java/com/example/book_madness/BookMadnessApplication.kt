package com.example.book_madness

import android.app.Application
import com.example.book_madness.data.AppContainer
import com.example.book_madness.data.AppDataContainer

// We can have only one Application class instance per our application by default —
// this ensures that we also have a single instance of the AppContainer as well
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
