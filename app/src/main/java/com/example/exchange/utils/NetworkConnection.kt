package com.example.exchange.utils

import android.util.Log
import com.example.exchange.data.Currencies
import com.example.exchange.data.CurrencyRates
import com.example.exchange.api.CurrencyRatesAPI
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.collections.HashMap

class NetworkConnection {
    private val TAG = "NetworkConnection"
    private var rates: HashMap<String, Double> = HashMap()

    fun getResponse(): HashMap<String, Double> {
        val currencies : List<String> = enumValues<Currencies>().map { it.name }
        val retrofit = Retrofit.Builder()
                .baseUrl("http://api.exchangeratesapi.io/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
        
        val currenciesRatesAPI = retrofit.create(CurrencyRatesAPI::class.java)
        currenciesRatesAPI.getAllRates()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DisposableSingleObserver<CurrencyRates>() {
                    override fun onSuccess(t: CurrencyRates?) {
                        for (pair in t!!.rates) {
                            if (currencies.contains(pair.key)) {
                                rates.put(pair.key, pair.value)
                            }
                        }
                    }
                    override fun onError(e: Throwable?) {
                        Log.i(TAG, "onError: Single hasn't come yet")
                    }
                })
        return rates
    }
}
