package com.example.book_madness.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.book_madness.model.FilterType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.filterPreferences: DataStore<Preferences> by preferencesDataStore(name = "selected_filter")

object DataStoreUtils {
    private val SELECTED_FILTER_KEY = stringPreferencesKey("selected_filter")

    suspend fun saveSelectedFilter(context: Context, filter: FilterType) {
        context.filterPreferences.edit { preferences ->
            preferences[SELECTED_FILTER_KEY] = filter.name
        }
    }

    val Context.selectedFilterFlow: Flow<FilterType?>
        get() = filterPreferences.data.map { preferences ->
            val filterName = preferences[SELECTED_FILTER_KEY]
            filterName?.let { FilterType.valueOf(it) }
        }
}
