package kr.co.are.androidble.data.local.preferences

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kr.co.are.androidble.data.local.preferences.repository.PreferencesRepositoryImpl
import kr.co.are.androidble.domain.repository.PreferencesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PrefsDataStoreModule {

    @Provides
    @Singleton
    fun providePreferencesDataStore(@ApplicationContext appContext: Context): PreferencesRepository =
        PreferencesRepositoryImpl(DataStoreManager(appContext))

}
