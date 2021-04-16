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
        if (currenciesRatesLiveData.value.isNullOrEmpty()) {
            currenciesRatesLiveData.value = responseOfConnection()
        }
        if (chosenCurrencyLiveDataFrom.value.isNullOrEmpty()) {
            chosenCurrencyLiveDataFrom.value = "EUR"
        }
        if (chosenCurrencyLiveDataTo.value.isNullOrEmpty()) {
            chosenCurrencyLiveDataTo.value = "EUR"
        }
        if (chosenCurrencyLiveDataValue.value.isNullOrEmpty()) {
            chosenCurrencyLiveDataValue.value = "0.0"
        }
    }

    private fun responseOfConnection(): HashMap<String, Double> {
        return NetworkConnection().getResponse()
    }

    fun calculation(): String {
        return if (!currenciesRatesLiveData.value.isNullOrEmpty()) {
            val valueOfChosenCurrencyFrom = currenciesRatesLiveData.value!![chosenCurrencyLiveDataFrom.value]
            val valueOfChosenCurrencyTo = currenciesRatesLiveData.value!![chosenCurrencyLiveDataTo.value]
            val chosenValue = chosenCurrencyLiveDataValue.value!!.toDouble()

            "${round(
                    chosenValue / valueOfChosenCurrencyFrom!! * valueOfChosenCurrencyTo!!
                            * 100) / 100}"
        } else {
            Log.i(TAG, "calculation: no values to calculate")
            ""
        }
    }
}
