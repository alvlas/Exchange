package com.example.exchange.utils

import io.reactivex.rxjava3.core.Observable
import java.lang.RuntimeException
import java.net.HttpURLConnection
import java.net.URL

fun createRequest(link: String): Observable<String> {
    return Observable.create<String> {
        val urlConnection = URL(link).openConnection() as HttpURLConnection
        try {
            urlConnection.connect()
            if (urlConnection.responseCode != HttpURLConnection.HTTP_OK) {
                it.onError(RuntimeException(urlConnection.responseMessage))
            } else {
                val str = urlConnection.inputStream.bufferedReader().readText()
                it.onNext(str)
            }
        } finally {
            urlConnection.disconnect()
        }
    }
}