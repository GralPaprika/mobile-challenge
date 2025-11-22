package com.example.mobilechallenge.di

import com.example.mobilechallenge.BuildConfig
import com.example.mobilechallenge.data.remote.ApiService
import com.example.mobilechallenge.data.remote.LoremApiService
import com.example.mobilechallenge.data.repository.HomeRepositoryImpl
import com.example.mobilechallenge.domain.repository.HomeRepository
import com.example.mobilechallenge.domain.usecase.GetAlbumsUseCase
import com.example.mobilechallenge.domain.usecase.GetAlbumsUseCaseImpl
import com.example.mobilechallenge.domain.usecase.GetPhotosUseCase
import com.example.mobilechallenge.domain.usecase.GetPhotosUseCaseImpl
import com.example.mobilechallenge.domain.usecase.GetPhotoDescriptionUseCase
import com.example.mobilechallenge.domain.usecase.GetPhotoDescriptionUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class JsonPlaceholderRetrofit

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class LoremRetrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
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
    @JsonPlaceholderRetrofit
    fun provideJsonPlaceholderRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @LoremRetrofit
    fun provideLoremRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.LOREM_API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(@JsonPlaceholderRetrofit retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideLoremApiService(@LoremRetrofit retrofit: Retrofit): LoremApiService {
        return retrofit.create(LoremApiService::class.java)
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
    fun provideGetPhotoDescriptionUseCase(loremApiService: LoremApiService): GetPhotoDescriptionUseCase {
        return GetPhotoDescriptionUseCaseImpl(loremApiService)
    }
}
