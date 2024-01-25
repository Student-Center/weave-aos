package com.weave.weave.data.local

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.weave.weave.data.local.UserDataStoreModule.PreferencesKeys.ACCESS_TOKEN
import com.weave.weave.data.local.UserDataStoreModule.PreferencesKeys.REFRESH_TOKEN
import com.weave.weave.data.local.dto.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserDataStoreModule(private val context: Context){
    private val Context.dataStore by preferencesDataStore(name = "user_prefs")

    private object PreferencesKeys {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }

    suspend fun getLoginToken(): Flow<UserPreferences> = context.dataStore.data
        .catch { exception ->
            if(exception is IOException){
                Log.e("Error reading preferences.", exception.toString())
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            mapUserPreferences(preferences)
        }

    suspend fun updatePreferencesRefreshToken(refreshToken: String){
        context.dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN] = refreshToken
        }
    }

    suspend fun updatePreferencesAccessToken(accessToken: String){
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken
        }
    }

    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        val accessToken = preferences[ACCESS_TOKEN] ?: "NULL"
        val refreshToken = preferences[REFRESH_TOKEN] ?: "NULL"
        return UserPreferences(accessToken, refreshToken)
    }
}