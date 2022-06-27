package com.globodai.ovestiaire.data.db

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.*
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.globodai.ovestiaire.data.models.User
import com.globodai.ovestiaire.data.models.dtos.LoginResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "my_data_store")

class UserPreferences(
    context: Context
) {

    private val appContext = context.applicationContext

    suspend fun saveAuthData(loginResponse: LoginResponse) {
        appContext.dataStore.edit { preferences ->
            preferences[KEY_AUTH] = loginResponse.token!!
            preferences[KEY_EXPIRE_DATE] = loginResponse.expireDate.time

            //if (loginResponse.utilisateurInfo.imgUrl!=null) preferences[KEY_MEMBER_IMG_URL] = loginResponse.utilisateurInfo.imgUrl!!
            //preferences[KEY_MEMBER_IMG_URL] = loginResponse.userInfo["ImgUrl"].toString()
            preferences[KEY_MEMBER_NAME] = loginResponse.name.toString()
            preferences[KEY_MEMBER_EMAIL] = loginResponse.email.toString()
            preferences[KEY_MEMBER_PHONE] = loginResponse.telephone.toString()
        }
    }


    val authToken: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[KEY_AUTH]
        }

    val expireDate: Flow<Long?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[KEY_EXPIRE_DATE]
        }

    val utilisateurData: Flow<User>
        get() =
            appContext.dataStore.data.map { preferences ->
                User(
                    preferences[KEY_MEMBER_NAME]!!,
                    preferences[KEY_MEMBER_EMAIL]!!,
                    preferences[KEY_MEMBER_PHONE]!!
                )
            }

    suspend fun clearEverything() {
        appContext.dataStore.edit { preferences ->
            preferences.clear()
        }
    }


    companion object {
        private val KEY_AUTH = stringPreferencesKey("key_auth")
        private val KEY_EXPIRE_DATE = longPreferencesKey("key_expire_date")

        private val KEY_MEMBER_IMG_URL = stringPreferencesKey("key_member_img_url")
        private val KEY_MEMBER_NAME = stringPreferencesKey("key_member_name")

        //private val KEY_MEMBER_FIRST_NAME = stringPreferencesKey("key_member_first_name")
        private val KEY_MEMBER_EMAIL = stringPreferencesKey("key_member_email")
        private val KEY_MEMBER_PHONE = stringPreferencesKey("key_member_phone")
    }
}
