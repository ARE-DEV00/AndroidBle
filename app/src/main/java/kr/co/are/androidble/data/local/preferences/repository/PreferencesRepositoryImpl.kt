package kr.co.are.androidble.data.local.preferences.repository

import kotlinx.coroutines.flow.first
import kr.co.are.androidble.data.local.preferences.DataStoreManager
import kr.co.are.androidble.data.local.preferences.PreferencesKeys
import kr.co.are.androidble.domain.repository.PreferencesRepository
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : PreferencesRepository {
    override suspend fun setServiceUuid(uuid: String) {
        dataStoreManager.storeValue(PreferencesKeys.PREF_SERVICE_UUID, uuid)
    }

    override suspend fun getServiceUuid(): String {
        return dataStoreManager.readValue(PreferencesKeys.PREF_SERVICE_UUID, "").first()
    }


}