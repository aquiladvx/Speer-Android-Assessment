package dev.aquiladvx.speerandroidassessment.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.aquiladvx.speerandroidassessment.BuildConfig
import dev.aquiladvx.speerandroidassessment.common.Constants.GITHUB_API_BASE_URL
import dev.aquiladvx.speerandroidassessment.data.network.GithubServiceApi
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideOkhttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val okHttp = OkHttpClient().newBuilder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val newRequest: Request =
                    chain.request().newBuilder().addHeader("Content-Type", "application/json")
                        .addHeader(
                            "Authorization",
                            "Bearer ${BuildConfig.GITHUB_TOKEN}"
                        ).build()
                chain.proceed(newRequest)
            }
            .build()

        return okHttp
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(GITHUB_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    fun provideGithubApi(retrofit: Retrofit): GithubServiceApi {
        return retrofit.create(GithubServiceApi::class.java)
    }
}