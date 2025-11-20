package com.example.mobilechallenge.di

import com.example.mobilechallenge.data.remote.ApiService
import com.example.mobilechallenge.data.repository.HomeRepositoryImpl
import com.example.mobilechallenge.domain.repository.HomeRepository
import com.example.mobilechallenge.domain.usecase.GetAlbumsUseCase
import com.example.mobilechallenge.domain.usecase.GetAlbumsUseCaseImpl
import com.example.mobilechallenge.domain.usecase.GetPhotosUseCase
import com.example.mobilechallenge.domain.usecase.GetPhotosUseCaseImpl
import com.example.mobilechallenge.domain.usecase.LoadHomeScreenUseCase
import com.example.mobilechallenge.domain.usecase.LoadHomeScreenUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideHomeRepository(apiService: ApiService): HomeRepository {
        return HomeRepositoryImpl(apiService)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideGetAlbumsUseCase(repository: HomeRepository): GetAlbumsUseCase {
        return GetAlbumsUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideGetPhotosUseCase(repository: HomeRepository): GetPhotosUseCase {
        return GetPhotosUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideLoadHomeScreenUseCase(
        getAlbumsUseCase: GetAlbumsUseCase,
        getPhotosUseCase: GetPhotosUseCase
    ): LoadHomeScreenUseCase {
        return LoadHomeScreenUseCaseImpl(getAlbumsUseCase, getPhotosUseCase)
    }
}
