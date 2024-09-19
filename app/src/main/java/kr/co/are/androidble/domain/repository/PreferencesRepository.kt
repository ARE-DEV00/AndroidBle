package kr.co.are.androidble.domain.repository


interface PreferencesRepository {
    suspend fun setServiceUuid(uuid: String)
    suspend fun getServiceUuid(): String
}