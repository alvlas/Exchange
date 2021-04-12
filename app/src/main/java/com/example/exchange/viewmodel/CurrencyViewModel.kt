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

    private fun responseOfConnection() {
        currenciesRatesLiveData.value = NetworkConnection().getResponse()
    }

    fun calculation(): String {
//        val rates = currenciesRatesLiveData.value
        if (!currenciesRatesLiveData.value.isNullOrEmpty()) {
            val valueOfChosenCurrencyFrom = currenciesRatesLiveData.value!![chosenCurrencyLiveDataFrom.value]
            val valueOfChosenCurrencyTo = currenciesRatesLiveData.value!![chosenCurrencyLiveDataTo.value]
            val chosenValue = chosenCurrencyLiveDataValue.value!!.toDouble()

            return "${round(
                chosenValue / valueOfChosenCurrencyFrom!! * valueOfChosenCurrencyTo!!
                    * 100) / 100}"

        } else {
            Log.i(TAG, "calculation: no values to calculate")
            return "null"
        }
    }
}
