package com.example.covid19.di

import android.content.Context
import android.content.SharedPreferences
import com.example.covid19.data.CovidAPI
import com.example.covid19.presenter.utils.MyConstants.Companion.BASE_URL
import com.example.covid19.presenter.utils.MyConstants.Companion.SHARED_PREF
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun getContext(@ApplicationContext context: Context) : Context{
        return context.applicationContext
    }

    @Singleton
    @Provides
    fun getSharedPref(context: Context) : SharedPreferences{
        return context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun createRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun getRequest(retrofit: Retrofit) : CovidAPI{
        return retrofit.create(CovidAPI::class.java)
    }
}