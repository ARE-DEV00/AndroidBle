package kr.co.are.androidble.ui.module.bluetooth

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BluetoothModule {

    @Provides
    @Singleton
    fun provideBluetoothModule(
        application: Application
    ): BluetoothUtil {
        return BluetoothUtil(application)
    }
}