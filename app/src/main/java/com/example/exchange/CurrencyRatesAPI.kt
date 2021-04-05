package com.example.exchange

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface CurrencyRatesAPI {

    @GET("latest?access_key=9e33db4650aa48d32dbb2a2b8f6be7d1")
    fun getRates(@Query("symbols") currencyArray: Array<Currencies>): Call<CurrencyRates>

    @GET("latest?access_key=9e33db4650aa48d32dbb2a2b8f6be7d1")
    fun getAllRates(): Call<CurrencyRates>

}
/**
 * https://api.exchangeratesapi.io/latest?symbols=USD,GBP
 */
