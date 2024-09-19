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

class AddGlucoseInfoUseCase @Inject constructor(
    private val appRepository: AppDatabaseRepository
) {
    suspend operator fun invoke(jsonData: String): Flow<ResultDomain<Boolean>> {
        return flow {
            runCatching {
                val moshi = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()

                // Moshi 어댑터 생성
                val adapter = moshi.adapter(GlucoseInfoEntity::class.java)

                // JSON 문자열을 GlucoseInfoEntity로 변환
                adapter.fromJson(jsonData)
            }.onSuccess { glucoseInfoEntity ->
                glucoseInfoEntity?.let {
                    // 앱 리포지토리에 데이터를 추가하고 그 결과를 순차적으로 처리
                    appRepository.addGlucoseInfo(glucoseInfoEntity)
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
                }
            }.onFailure {
                emit(ResultDomain.Error(it, false))
            }
        }.flowOn(Dispatchers.IO) // IO 스레드에서 실행
    }

}
