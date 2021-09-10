package com.example.covid19.data

import com.example.covid19.presenter.model.MainData
import com.example.covid19.presenter.model.UzbCovid19
import retrofit2.http.*

interface CovidAPI {

    @GET("summary")
    suspend fun getCountries() : MainData

    @GET("country/uzbekistan")
    suspend fun getUzbData(@Query("from") data1:String, @Query("to") data2:String) : List<UzbCovid19>

    @GET("live/country/uzbekistan/status/confirmed")
    suspend fun getUzbSingleData() : List<UzbCovid19>
}