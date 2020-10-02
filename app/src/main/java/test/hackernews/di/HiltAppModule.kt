package test.hackernews.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import test.hackernews.data.api.HackerNewsApi
import test.hackernews.data.db.HackerNewsDb
import javax.inject.Singleton

/**
 * Provides the objects needed to construct repository class
 */
@Module
@InstallIn(ApplicationComponent::class)
object HiltAppModule {
    private const val BASE_URL = "https://hn.algolia.com/api/v1/"

    @Singleton
    @Provides
    fun provideRestAdapter(): Retrofit {
        val oktHttpClient = OkHttpClient.Builder()
        val client = oktHttpClient.build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideApi(restAdapter: Retrofit): HackerNewsApi {
        return restAdapter.create(HackerNewsApi::class.java)
    }

    @Singleton
    @Provides
    fun provideDb(@ApplicationContext ctx: Context): HackerNewsDb {
        return HackerNewsDb.create(ctx, false)
    }

}