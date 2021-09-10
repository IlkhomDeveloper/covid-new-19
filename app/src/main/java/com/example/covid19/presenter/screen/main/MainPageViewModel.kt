package com.example.covid19.presenter.screen.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covid19.domain.usecase.CovidUseCase
import com.example.covid19.presenter.model.CovidTotal
import com.example.covid19.presenter.model.DataFromTo
import com.example.covid19.presenter.model.UzbCovid19
import com.example.covid19.presenter.utils.MyConstants.Companion.YYYY_MM_DD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainPageViewModel @Inject constructor(private val useCase: CovidUseCase) : ViewModel() {

    private var _worldTotal = MutableLiveData<CovidTotal>()
    val worldTotal: LiveData<CovidTotal> = _worldTotal

    private var _covidUzb = MutableLiveData<CovidTotal>()
    val covidUzb: LiveData<CovidTotal> = _covidUzb

    private var _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun getWorldTotal() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = useCase.getWorldStatistics()
                val death = data.TotalDeaths
                val all = data.TotalConfirmed
                val confirmed = if (data.TotalRecovered == 0) all-death else data.TotalRecovered

                Log.d("TTT"," data $data")
                val newData = CovidTotal(all, death, confirmed)
                _worldTotal.postValue(newData)

            } catch (e: Exception) {
                _message.postValue(e.message)
            } catch (e: HttpException) {
                _message.postValue(e.message())
            }
        }
    }

    fun getUzbTotal(data: DataFromTo) {
        if (data.from == "fromT00:00:00Z" || data.to == "toT00:00:00Z"){
            _message.postValue("You have to select both times! ")
            return
        }
        val date1 = SimpleDateFormat(YYYY_MM_DD, Locale("uz")).parse(data.from)
        val date2 = SimpleDateFormat(YYYY_MM_DD, Locale("uz")).parse(data.to)

        if (date1 < date2 && date2 <= Date()){
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val ls = useCase.getUzbData(data)
                    Log.d("TTT", "ls = ${ls.size}")
                    val all = ls[ls.lastIndex].Confirmed - ls[0].Confirmed
                    val death = ls[ls.lastIndex].Deaths - ls[0].Deaths
                    val recover = if (ls[ls.lastIndex].Recovered - ls[0].Recovered == 0) all - death else
                        ls[ls.lastIndex].Recovered - ls[0].Recovered
                    _covidUzb.postValue(CovidTotal(all, death, recover))
                } catch (e: Exception) {
                    _message.postValue(e.message)
                } catch (e: HttpException) {
                    _message.postValue(e.message())
                }
            }
        }
        else{
            _message.postValue("The time is incorrect!")
        }
    }

    fun getUzbSingle() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val uzbCovid19 = useCase.getUzbDataSingle()
                val all = uzbCovid19.Confirmed
                val death = uzbCovid19.Deaths
                val confirmed = if (uzbCovid19.Recovered == 0) all-death else uzbCovid19.Recovered
                _covidUzb.postValue(CovidTotal(all, death, confirmed))
            } catch (e: Exception) {
                _message.postValue(e.message)
            } catch (e: HttpException) {
                _message.postValue(e.message())
            }
        }
    }
}