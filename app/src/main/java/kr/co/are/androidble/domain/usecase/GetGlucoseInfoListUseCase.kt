package kr.co.are.androidble.domain.usecase

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kr.co.are.androidble.domain.entity.GlucoseInfoEntity
import kr.co.are.androidble.domain.model.ResultDomain
import kr.co.are.androidble.domain.repository.AppDatabaseRepository
import javax.inject.Inject

class GetGlucoseInfoListUseCase @Inject constructor(
    private val appRepository: AppDatabaseRepository
) {
    suspend operator fun invoke(): Flow<ResultDomain<List<GlucoseInfoEntity>>> {
        return flow {
            runCatching {
                appRepository.getGlucoseInfoList()
                    .catch { exception ->
                        // 에러를 방출
                        emit(ResultDomain.Error(exception, false))
                    }
                    .collect { resultData -> // collectLatest 대신 collect 사용
                        when (resultData) {
                            is ResultDomain.Success -> {
                                emit(ResultDomain.Success(resultData.data))
                            }
                            is ResultDomain.Error -> {
                                emit(
                                    ResultDomain.Error(
                                        resultData.exception,
                                        resultData.isNetwork
                                    )
                                )
                            }
                            ResultDomain.Loading -> {
                                emit(ResultDomain.Loading)
                            }
                        }
                    }
            }.onFailure {
                emit(ResultDomain.Error(it, false))
            }
        }.flowOn(Dispatchers.IO) // IO 스레드에서 실행
    }

}
