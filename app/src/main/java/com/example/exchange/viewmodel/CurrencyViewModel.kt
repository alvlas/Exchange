package com.example.exchange.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.exchange.utils.NetworkConnection
import kotlin.math.round

class CurrencyViewModel(application: Application): AndroidViewModel(application) {
    private val TAG = "CurrencyViewModel"
    val chosenCurrencyLiveDataFrom = MutableLiveData<String>()
    val chosenCurrencyLiveDataTo = MutableLiveData<String>()
    val chosenCurrencyLiveDataValue = MutableLiveData<String>()
    private var currenciesRatesLiveData = MutableLiveData<HashMap<String, Double>>()

    init {
        responseOfConnection()
        chosenCurrencyLiveDataFrom.value = "EUR"
        chosenCurrencyLiveDataTo.value = "EUR"
        chosenCurrencyLiveDataValue.value = "0.0"
    }

    fun responseOfConnection(): java.util.HashMap<String, Double> {
//        rates = NetworkConnection().getResponseRXJava()
        currenciesRatesLiveData.value = NetworkConnection().getResponseRetrofit()
        return currenciesRatesLiveData.value!!
    }

    fun calculation(): String {
//        val rates = currenciesRatesLiveData.value
        if (!currenciesRatesLiveData.value.isNullOrEmpty()) {
            val valueOfChosenCurrencyFrom = currenciesRatesLiveData.value!![chosenCurrencyLiveDataFrom.value]
            val valueOfChosenCurrencyTo = currenciesRatesLiveData.value!![chosenCurrencyLiveDataTo.value]
            val chosenValue = chosenCurrencyLiveDataValue.value!!.toDouble()

            Log.i(TAG, "calculation: ${chosenCurrencyLiveDataValue.value!!}")
            Log.i(TAG, "calculation: ${currenciesRatesLiveData.value!![chosenCurrencyLiveDataFrom.value]}")
            Log.i(TAG, "calculation: ${currenciesRatesLiveData.value!![chosenCurrencyLiveDataTo.value]}")
            Log.i(TAG, "calculation: ${round(
                chosenValue / valueOfChosenCurrencyFrom!! * valueOfChosenCurrencyTo!!
                    * 100) / 100}")

            return "${round(
                chosenValue / valueOfChosenCurrencyFrom!! * valueOfChosenCurrencyTo!!
                    * 100) / 100}"

        } else {
            Log.i(TAG, "calculation: no values to calculate")
            return "null"
        }
    }
}
