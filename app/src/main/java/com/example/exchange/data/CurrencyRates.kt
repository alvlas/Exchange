package com.example.exchange.data

import com.google.gson.annotations.SerializedName

data class CurrencyRates(
        @SerializedName("success")
        val success: Boolean,

        @SerializedName("base")
        val base: String,

        @SerializedName("data")
        val data: String,

        @SerializedName("rates")
        val rates: HashMap<String, Double>
)

/*
{
  "success":true,
  "timestamp":1617363186,
  "base":"EUR",
  "date":"2021-04-02",
  "rates":{
    "USD":1.177516,
    "AUD":1.546244,
    "CAD":1.479138,
    "PLN":4.59991,
    "MXN":23.912419
  }
}
 */
