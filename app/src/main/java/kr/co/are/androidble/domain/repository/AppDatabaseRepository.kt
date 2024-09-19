package kr.co.are.androidble.domain.repository

import kotlinx.coroutines.flow.Flow
import kr.co.are.androidble.domain.entity.GlucoseInfoEntity
import kr.co.are.androidble.domain.model.ResultDomain

interface AppDatabaseRepository {
    suspend fun getGlucoseInfoList(): Flow<ResultDomain<List<GlucoseInfoEntity>>>
    suspend fun addGlucoseInfo(glucoseInfoEntity: GlucoseInfoEntity): Flow<ResultDomain<Boolean>>
    suspend fun deleteAllGlucoseInfo(): Flow<ResultDomain<Boolean>>
}