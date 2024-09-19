package kr.co.are.androidble.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@ActivityRetainedScoped
class DataStoreManager @Inject constructor(@ApplicationContext private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(PreferencesKeys.KEY_PREF_DATA_STORE)

    suspend fun <T> storeValue(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { prefs ->
            prefs[key] = value
        }

    }

    suspend fun <T> readValue(key: Preferences.Key<T>, defaultValue: T):Flow<T> {

        Timber.d("##### readValue2 - prefs[key]!!")
        return context.dataStore.data
            .catch { e ->
                e.printStackTrace()
                if (e is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw e
                }
            }
            .map { prefs ->
                prefs[key]?: defaultValue
            }

    }
}