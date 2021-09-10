package com.example.covid19.domain.repository

import com.example.covid19.presenter.model.CovidTotal
import com.example.covid19.presenter.model.DataFromTo
import com.example.covid19.presenter.model.Global
import com.example.covid19.presenter.model.UzbCovid19

interface Covid {
    suspend fun getWorldStatistics() : Global
    suspend fun getUzbData(data: DataFromTo) : List<UzbCovid19>
    suspend fun getUzbDataSingle() : UzbCovid19
}