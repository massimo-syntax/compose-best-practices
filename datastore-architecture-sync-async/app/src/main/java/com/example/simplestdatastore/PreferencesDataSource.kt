package com.example.simplestdatastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.io.IOException


class PreferencesDataSource(
    private val dataStore: DataStore<Preferences>
){
    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                // debug type, no flavors
                throw exception
            }
        }.map { preferences ->
            val theme =
                StringToSelectedTheme.getThemeEnum(preferences[KEY_THEME])
            val username = preferences[KEY_USER_NAME] ?: USERNAME_GUEST
            UserPreferences(
                theme = theme,
                username = username
            )
        }

    fun getUserPrefsOnceSync(): UserPreferences{
        return runBlocking {
            val prefs = dataStore.data.first()
            val theme =
                StringToSelectedTheme.getThemeEnum(prefs[KEY_THEME])
            val username = prefs[KEY_USER_NAME] ?: USERNAME_GUEST
            UserPreferences(
                theme = theme,
                username = username
            )
        }
    }

    suspend fun saveTheme(theme: SelectedTheme) {
        dataStore.edit { preferences ->
            preferences[KEY_THEME] = theme.string
        }
    }

    suspend fun saveName(name: String) {
        dataStore.edit { preferences ->
            preferences[KEY_USER_NAME] = name
        }
    }

    suspend fun clearAllData() {
        dataStore.edit { it.clear() }
    }


    companion object{
        private val KEY_THEME = stringPreferencesKey("key_app_background_color")
        private val KEY_USER_NAME = stringPreferencesKey("key_app_user_name")

    }

}
