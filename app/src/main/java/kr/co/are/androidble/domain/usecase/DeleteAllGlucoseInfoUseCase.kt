package kr.co.are.androidble.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kr.co.are.androidble.domain.model.ResultDomain
import kr.co.are.androidble.domain.repository.AppDatabaseRepository
import javax.inject.Inject

class DeleteAllGlucoseInfoUseCase @Inject constructor(
    private val appRepository: AppDatabaseRepository
) {
    suspend operator fun invoke(): Flow<ResultDomain<Boolean>> {
        return flow {
            appRepository.deleteAllGlucoseInfo()
                .catch { exception ->
                    // 에러를 방출
                    emit(ResultDomain.Error(exception, false))
                }
                .collect { resultData -> // collectLatest 대신 collect 사용
                    when (resultData) {
                        is ResultDomain.Success -> {
                            emit(ResultDomain.Success(true))
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
        }.flowOn(Dispatchers.IO)
    }

}
