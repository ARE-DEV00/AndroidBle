package kr.co.are.androidble.data.local.preferences

import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {

    val KEY_PREF_DATA_STORE = "PREF_DATA_STORE"
    val PREF_SERVICE_UUID = stringPreferencesKey("PREF_SERVICE_UUID")

}