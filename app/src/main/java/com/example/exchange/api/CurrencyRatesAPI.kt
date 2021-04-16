package com.example.exchange.api

import com.example.exchange.data.Currencies
import com.example.exchange.data.CurrencyRates
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyRatesAPI {

    @GET("latest?access_key=9e33db4650aa48d32dbb2a2b8f6be7d1")
    fun getAllRatesRetrofit(): Call<CurrencyRates>

    @GET("latest?access_key=9e33db4650aa48d32dbb2a2b8f6be7d1")
    fun getAllRates(): Single<CurrencyRates>

    @GET("latest?access_key=9e33db4650aa48d32dbb2a2b8f6be7d1")
    fun getRates(@Query("symbols") currencyArray: Array<Currencies>): Call<CurrencyRates>

}
/**
 * https://manage.exchangeratesapi.io/dashboard
 * http://api.exchangeratesapi.io/v1/latest?access_key=778de779b485b2815141bc2d5a53b135&symbols=USD,AUD,CAD,PLN,MXN&format=1
 */
