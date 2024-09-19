package kr.co.are.androidble.data.local.room.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kr.co.are.androidble.data.local.room.database.AppDatabase
import kr.co.are.androidble.data.local.room.entity.TableGlucoseInfoEntity
import kr.co.are.androidble.domain.entity.GlucoseInfoEntity
import kr.co.are.androidble.domain.model.ResultDomain
import kr.co.are.androidble.domain.repository.AppDatabaseRepository
import javax.inject.Inject

class AppDatabaseRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase,
) : AppDatabaseRepository {
    override suspend fun getGlucoseInfoList(): Flow<ResultDomain<List<GlucoseInfoEntity>>> {
        return flow {
            runCatching {
                emit(ResultDomain.Loading)
                val glucoseInfoList = appDatabase.glucoseInfoDao().selectAllGlucoseInfo()
                emit(ResultDomain.Success(glucoseInfoList.map {
                    GlucoseInfoEntity(
                        type = it.type,
                        serviceUuid = it.serviceUuid,
                        glucoseLevel = it.glucoseLevel,
                        unit = it.unit,
                        time = it.time,
                        createdTime = it.createdTime.toString(),
                        modifiedTime = it.modifiedTime.toString(),
                    )
                }))
            }.onFailure {
                emit(ResultDomain.Error(it, false))
            }
        }
    }

    override suspend fun addGlucoseInfo(glucoseInfoEntity: GlucoseInfoEntity): Flow<ResultDomain<Boolean>> {
        return flow {
            runCatching {
                emit(ResultDomain.Loading)
                appDatabase.glucoseInfoDao().insertGlucoseInfoInfo(
                    TableGlucoseInfoEntity(
                        type = glucoseInfoEntity.type,
                        serviceUuid = glucoseInfoEntity.serviceUuid,
                        glucoseLevel = glucoseInfoEntity.glucoseLevel,
                        unit = glucoseInfoEntity.unit,
                        time = glucoseInfoEntity.time,
                    )
                )
                emit(ResultDomain.Success(true))
            }.onFailure {
                emit(ResultDomain.Error(it, false))
            }
        }
    }

    override suspend fun deleteAllGlucoseInfo(): Flow<ResultDomain<Boolean>> {
        return flow {
            runCatching {
                emit(ResultDomain.Loading)
                appDatabase.glucoseInfoDao().deleteAllGlucoseInfoInfo()
                emit(ResultDomain.Success(true))
            }.onFailure {
                emit(ResultDomain.Error(it, false))
            }
        }
    }
}
