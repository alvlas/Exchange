package com.example.exchange

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.exchange.utils.NetworkConnection
import java.util.*
import kotlin.math.round

class MainActivityViewModel: ViewModel() {
    private val TAG = "MainActivityViewModel"
    private var rates: HashMap<String, Double> = HashMap()
    private var chosenCurrencyFrom = "USD"
    private var chosenCurrencyTo = "RUB"
    private var chosenValue = 0.0

    fun getChosenCurrencyFrom(): String {
        return chosenCurrencyFrom
    }
    fun setChosenCurrencyFrom(chosenCurrencyFrom: String) {
        this.chosenCurrencyFrom = chosenCurrencyFrom
    }

    fun getChosenCurrencyTo(): String {
        return chosenCurrencyTo
    }
    fun setChosenCurrencyTo(chosenCurrencyTo: String) {
        this.chosenCurrencyTo = chosenCurrencyTo
    }

    fun getChosenValue(): Double {
        return chosenValue
    }
    fun setChosenValue(inputValue: String) {
        chosenValue = inputValue.toDouble()
    }

    fun responseOfConnection(): HashMap<String, Double> {
//        rates = NetworkConnection().getResponseRXJava()
        rates = NetworkConnection().getResponseRetrofit()
        return rates
    }

    fun calculation(): String {
        if (!rates.isNullOrEmpty()) {
            val valueOfChosenCurrencyFrom = rates[chosenCurrencyFrom]
            val valueOfChosenCurrencyTo = rates[chosenCurrencyTo]
            Log.i(TAG, "calculation: $chosenValue")
            Log.i(TAG, "calculation: ${rates[chosenCurrencyFrom]}")
            Log.i(TAG, "calculation: ${rates[chosenCurrencyTo]}")
            Log.i(TAG, "calculation: ${round(chosenValue / valueOfChosenCurrencyFrom!! * valueOfChosenCurrencyTo!!
                    * 100) / 100}")
            return "${round(chosenValue / valueOfChosenCurrencyFrom!! * valueOfChosenCurrencyTo!!
                    * 100) / 100}"
        } else {
            Log.i(TAG, "calculation: no values to calculate")
            return "null"
        }

    }

}