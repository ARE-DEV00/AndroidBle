package kr.co.are.androidble.data.local.room

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kr.co.are.androidble.data.local.room.database.AppDatabase
import kr.co.are.androidble.data.local.room.repository.AppDatabaseRepositoryImpl
import kr.co.are.androidble.domain.repository.AppDatabaseRepository
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppDatabaseModule {

    @Provides
    @Singleton
    @Named("AppDatabase")
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDataBaseKey.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideAppDatabaseRepository(@Named("AppDatabase") database: AppDatabase): AppDatabaseRepository =
        AppDatabaseRepositoryImpl(database)


}