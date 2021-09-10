package com.example.covid19.domain.usecase

import com.example.covid19.data.CovidAPI
import com.example.covid19.domain.repository.Covid
import com.example.covid19.presenter.model.DataFromTo
import com.example.covid19.presenter.model.Global
import com.example.covid19.presenter.model.UzbCovid19
import javax.inject.Inject

class CovidUseCase @Inject constructor(private val covidAPI: CovidAPI) : Covid {
    override suspend fun getWorldStatistics(): Global {
        return covidAPI.getCountries().Global
    }

    override suspend fun getUzbData(data: DataFromTo): List<UzbCovid19> {
        return covidAPI.getUzbData(data.from, data.to)
    }

    override suspend fun getUzbDataSingle(): UzbCovid19 {
        return covidAPI.getUzbSingleData()[covidAPI.getUzbSingleData().lastIndex]
    }
}