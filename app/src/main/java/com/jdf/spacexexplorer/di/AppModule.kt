package com.jdf.spacexexplorer.di

import android.content.Context
import androidx.room.Room
import com.jdf.spacexexplorer.data.local.AppDatabase
import com.jdf.spacexexplorer.data.local.LaunchDao
import com.jdf.spacexexplorer.data.local.RocketDao
import com.jdf.spacexexplorer.data.local.CapsuleDao
import com.jdf.spacexexplorer.data.local.CoreDao
import com.jdf.spacexexplorer.data.remote.ApiService
import com.jdf.spacexexplorer.data.repository.SpaceXRepositoryImpl
import com.jdf.spacexexplorer.domain.repository.SpaceXRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): com.squareup.moshi.Moshi {
        return com.squareup.moshi.Moshi.Builder()
            .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: com.squareup.moshi.Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.spacexdata.com/v4/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "spacex_explorer_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideLaunchDao(database: AppDatabase): LaunchDao {
        return database.launchDao()
    }

    @Provides
    @Singleton
    fun provideRocketDao(database: AppDatabase): RocketDao {
        return database.rocketDao()
    }

    @Provides
    @Singleton
    fun provideCapsuleDao(database: AppDatabase): CapsuleDao {
        return database.capsuleDao()
    }

    @Provides
    @Singleton
    fun provideCoreDao(database: AppDatabase): CoreDao {
        return database.coreDao()
    }

    @Provides
    @Singleton
    fun provideSpaceXRepository(
        apiService: ApiService,
        launchDao: LaunchDao,
        rocketDao: RocketDao,
        capsuleDao: CapsuleDao,
        coreDao: CoreDao
    ): SpaceXRepository {
        return SpaceXRepositoryImpl(apiService, launchDao, rocketDao, capsuleDao, coreDao)
    }
} 