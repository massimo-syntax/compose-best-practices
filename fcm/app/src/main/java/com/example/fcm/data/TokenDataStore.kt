package com.example.fcm.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "fcm_prefs")

class TokenDataStore(context: Context) {

    private val applicationContext = context.applicationContext

    val tokenFlow: Flow<String?> = applicationContext.dataStore.data.map { preferences ->
        preferences[KEY_FCM_TOKEN]
    }

    suspend fun saveToken(token: String) {
        applicationContext.dataStore.edit { preferences ->
            preferences[KEY_FCM_TOKEN] = token
        }
    }

    companion object {
        private val KEY_FCM_TOKEN = stringPreferencesKey("fcm_token")
    }
}
