package com.colorpl.di

import android.content.Context
import com.colorpl.BuildConfig
import com.colorpl.R
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    //local property로 따로 빼기
    val baseUrl = BuildConfig.BASE_URL
    val naverUrl = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/"
    val tmapUrl = "https://apis.openapi.sk.com/"
    val gptUrl = "https://api.openai.com/"

    @Singleton
    @Provides
    @NormalRetrofit
    fun provideRetrofit(@NormalOkHttp okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    @NoHeaderRetrofit
    fun provideNoHeaderRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .baseUrl(baseUrl)
            .build()
    }

    @Singleton
    @Provides
    @NormalOkHttp
    fun provideOkHttpClient(interceptor: AccessTokenInterceptor) = OkHttpClient.Builder().run {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        addInterceptor(interceptor)
        connectTimeout(120, TimeUnit.SECONDS)
        readTimeout(120, TimeUnit.SECONDS)
        writeTimeout(120, TimeUnit.SECONDS)
        build()
    }


    @Singleton
    @Provides
    @TmapRetrofit
    fun provideTmapRetrofit(@TmapOkHttp okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .baseUrl(tmapUrl)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    @TmapOkHttp
    fun provideTmapOkHttpClient() = OkHttpClient.Builder().run {
        addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("appKey", BuildConfig.TMAP_APP_KEY)
                .method(original.method, original.body)
                .build()
            chain.proceed(request = request)
        }
        connectTimeout(120, TimeUnit.SECONDS)
        readTimeout(120, TimeUnit.SECONDS)
        writeTimeout(120, TimeUnit.SECONDS)
        build()
    }

    @Singleton
    @Provides
    @GptRetrofit
    fun provideGptRetrofit(@GptOkHttp okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .baseUrl(gptUrl)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    @GptOkHttp
    fun provideGptOkHttpClient() = OkHttpClient.Builder().run {
        addInterceptor { chain ->
            val original = chain.request()
            val bearer = "Bearer ${BuildConfig.OPEN_API_KEY}"
            val request = original.newBuilder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", bearer)
                .method(original.method, original.body)
                .build()
            chain.proceed(request = request)
        }
        connectTimeout(120, TimeUnit.SECONDS)
        readTimeout(120, TimeUnit.SECONDS)
        writeTimeout(120, TimeUnit.SECONDS)
        build()
    }

    @Singleton
    @Provides
    @NaverRetrofit
    fun provideGeoCodingRetrofit(@NaverOkHttp okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .baseUrl(naverUrl)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    @NaverOkHttp
    fun provideGeoCodingOkHttpClient() = OkHttpClient.Builder().run {
        addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("X-NCP-APIGW-API-KEY-ID", BuildConfig.NAVER_MAP_CLIENT_ID)
                .header("X-NCP-APIGW-API-KEY", BuildConfig.NAVER_MAP_CLIENT_SECRET)
                .method(original.method, original.body)
                .build()
            chain.proceed(request = request)
        }
        connectTimeout(120, TimeUnit.SECONDS)
        readTimeout(120, TimeUnit.SECONDS)
        writeTimeout(120, TimeUnit.SECONDS)
        build()
    }

    @Singleton
    @Provides
    fun provideGetSignInWithGoogleOption(
        @ApplicationContext context: Context
    ): GetSignInWithGoogleOption {
        return GetSignInWithGoogleOption.Builder(
            context.getString(R.string.default_web_client_id)
        )
            .build()

    }

}