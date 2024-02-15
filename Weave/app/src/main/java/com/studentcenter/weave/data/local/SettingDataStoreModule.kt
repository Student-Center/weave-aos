package com.studentcenter.weave.data.local

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.studentcenter.weave.data.local.SettingDataStoreModule.PreferencesKeys.LOGIN_STATE
import com.studentcenter.weave.data.local.dto.SettingPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class SettingDataStoreModule(private val context: Context){
    private val Context.dataStore by preferencesDataStore(name = "settings")

    private object PreferencesKeys {
        val LOGIN_STATE = booleanPreferencesKey("login_state")
    }

    suspend fun getSettings(): Flow<SettingPreferences> = context.dataStore.data
        .catch { exception ->
            if(exception is IOException){
                Log.e("Error reading preferences.", exception.toString())
                emit(emptyPreferences())
            } else {
                Log.e("Error throw Exception", "line30")
                throw exception
            }
        }.map { preferences ->
            mapSettingPreferences(preferences)
        }

    suspend fun clearData(){
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun updatePreferencesLoginState(loginState: Boolean){
        context.dataStore.edit { preferences ->
            preferences[LOGIN_STATE] = loginState
        }
    }

    private fun mapSettingPreferences(preferences: Preferences): SettingPreferences {
        val loginState = preferences[LOGIN_STATE] ?: false
        return SettingPreferences(loginState)
    }
}