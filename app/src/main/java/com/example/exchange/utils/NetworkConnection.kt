package com.example.exchange.utils

import android.util.Log
import com.example.exchange.Currencies
import com.example.exchange.CurrencyRates
import com.example.exchange.CurrencyRatesAPI
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.collections.HashMap

/**
 * This function returns an prepared Observable
 */

class NetworkConnection {
    private val TAG = "NetworkUtils"
    private val rates: HashMap<String, Double> = HashMap()

    fun getResponseRetrofit(): HashMap<String, Double> {
        val currencies = enumValues<Currencies>()
        val token = "2d2732b32c912cb202ef2feb76ec197e"

        val retrofit = Retrofit.Builder()
                .baseUrl("http://api.exchangeratesapi.io/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val currencyRatesAPI = retrofit.create(CurrencyRatesAPI::class.java)
//        val currencyRatesResponse = currencyRatesAPI.getRates(currencies)
        val currencyRatesResponse = currencyRatesAPI.getAllRates()
        Log.i(TAG, "getResponseRetrofit: ${currencyRatesResponse.request().url}")

        currencyRatesResponse.enqueue(object : Callback<CurrencyRates> {
            override fun onResponse(call: Call<CurrencyRates>, response: Response<CurrencyRates>) {
                if (response.isSuccessful) {
                    Log.i(TAG, "onResponse: Ok!")
                    val ratesMap = response.body()?.rates!!
                    for (pair in ratesMap.entries) {
                        Log.i(TAG, "onResponse: ${pair.key} : ${pair.value}")
                        rates[pair.key] = pair.value
//                        showCurrency(pair.key, pair.value)
                    }
//                    rates["EUR"] = 1.0
                } else {
                    Log.i(TAG, "onResponse: response.isSuccessful = false // ${response.code()}")
                }
                try {

                } catch (ex: NullPointerException) {
                    Log.i(TAG, "onResponse: Caught an ERROR")
                    Log.i(TAG, "onResponse: ${ex.message}")
                }
            }

            override fun onFailure(call: Call<CurrencyRates>, t: Throwable) {
                Log.i(TAG, "onResponse: response fell")
                Log.i(TAG, "onResponse: ${t.message}")
            }
        })
        return rates
    }

    fun getResponseRXJava(): HashMap<String, Double> {
        val link = "https://api.exchangeratesapi.io/latest"
        val o = createRequest(link)
                .map { Gson().fromJson(it, Feed::class.java) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        val request = o.subscribe({
            for (item in it.rates) {
                rates.put(item.key, item.value)
                Log.i(TAG, "responseOfConnection: ${item.key} ${item.value}")
                Log.i(TAG, "responseOfConnection: ${rates.containsKey(item.key)}")
            }
        }, {
            Log.i(TAG, "responseOfConnection: no")
        })

        // Add the base "EUR" for further calculation
        rates.put("EUR", 1.0)


        for (i in rates) {
            Log.i(TAG, "getResponse: ${i.key} : ${i.value} ***")
        }
        return rates
    }
}

class Feed(
        val rates: Map<String, Double>
)

/*
{
"rates":
    {
    "CAD":1.5225,
    "HKD":9.3297,
    "ISK":152.1,
    "PHP":58.338,"DKK":7.4361,"HUF":363.7,"CZK":26.142,
            "AUD":1.5422,"RON":4.8762,"SEK":10.14,"IDR":17230.11,"INR":88.259,"BRL":6.8387,"RUB":88.6885,
            "HRK":7.5825,"JPY":128.58,"THB":36.401,"CHF":1.105,"SGD":1.6016,"PLN":4.5322,"BGN":1.9558,
            "TRY":8.8151,"CNY":7.784,"NOK":10.2555,"NZD":1.6565,"ZAR":18.1353,"USD":1.2028,"MXN":24.8772,
            "ILS":3.97,"GBP":0.86433,"KRW":1354.37,"MYR":4.8804
    },

"base":"EUR",
"date":"2021-03-02"
}
*/

