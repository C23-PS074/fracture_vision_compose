package com.bangkit.fracturevision.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import com.bangkit.fracturevision.model.User

class UserPreference private constructor(private val dataStore: DataStore<Preferences>){
    fun getUser(): Flow<User> {
        return dataStore.data.map { preferences ->
            User(
                preferences[UID_KEY] ?:0,
                preferences[USERNAME_KEY] ?: "",
                preferences[STATE_KEY] ?: false
            )
        }
    }

    suspend fun login(user: User) {
        dataStore.edit { preferences ->
            preferences[USERNAME_KEY] = user.username
            preferences[STATE_KEY] = user.isLogin
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = false
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null
        private val UID_KEY = intPreferencesKey("uid")
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val STATE_KEY = booleanPreferencesKey("state")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}