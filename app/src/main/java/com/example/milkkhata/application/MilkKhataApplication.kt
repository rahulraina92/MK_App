package com.example.milkkhata.application

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.milkkhata.room.AppDatabase
import com.example.milkkhata.room.MyRecordDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@HiltAndroidApp
class MilkKhataApplication : Application()

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMyRecordDao(db: AppDatabase): MyRecordDao = db.myRecordDao()

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "my_db").build()
}