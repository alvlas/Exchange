package com.example.exchange

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.exchange.utils.*


class MainActivity : AppCompatActivity() {

    private lateinit var llMainCurrencyPanel: LinearLayout
    private lateinit var llMainCurrencyPanelFrom: LinearLayout
    private lateinit var llMainCurrencyPanelTo: LinearLayout
    private lateinit var etMainInput: EditText
    private lateinit var tvMainOutput: TextView
    private lateinit var bTest: Button
    private lateinit var vm: MainActivityViewModel
    private val TAG = "MainActivity"
//    var rates: HashMap<String, Double> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        llMainCurrencyPanel = findViewById<LinearLayout>(R.id.ll_main_currency_panel)
        llMainCurrencyPanelFrom = findViewById<LinearLayout>(R.id.ll_main_currency_panel_from)
        llMainCurrencyPanelTo = findViewById<LinearLayout>(R.id.ll_main_currency_panel_to)
        etMainInput = findViewById<EditText>(R.id.et_main_input)
        tvMainOutput = findViewById<TextView>(R.id.tv_main_output)
        bTest = findViewById<Button>(R.id.b_test)
        vm = MainActivityViewModel()
        var rates: HashMap<String, Double> = vm.responseOfConnection()

//        val rates = getResponse()

        val currencyArray: Array<String> = arrayOf("EUR", "USD", "RUB", "GBP", "CHF", "CNY")
        showCurrencyPanel(currencyArray, llMainCurrencyPanelFrom, "from")
        showCurrencyPanel(currencyArray, llMainCurrencyPanelTo, "to")

        etMainInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    vm.setChosenValue(s.toString())
                    showResult()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

    }

    fun showCurrencyPanel(array: Array<String>, llCurrencyPanel: ViewGroup, direction: String) {
        val inflater = layoutInflater
        for (currency in array) {
            val view = inflater.inflate(R.layout.currency_item, llCurrencyPanel, false)
            val tvItem = view.findViewById<TextView>(R.id.currency_item)
            tvItem.text = currency

            if (direction == "from") {
                view.setOnClickListener(View.OnClickListener {
                    vm.setChosenCurrencyFrom(tvItem.text.toString())
                    showResult()
                })
            } else {
                view.setOnClickListener(View.OnClickListener {
                    vm.setChosenCurrencyTo(tvItem.text.toString())
                    showResult()
                })
            }

            llCurrencyPanel.addView(view)
        }
    }

    /*
    // RXJava implementation here
    fun getResponse(): HashMap<String, Double> {
        var currencyRatePairs = HashMap<String, Double>()
        val link = "https://api.exchangeratesapi.io/latest"
        val o = createRequest(link)
                .map { Gson().fromJson(it, Feed::class.java) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        val request = o.subscribe({
            for (item in it.rates) {
                currencyRatePairs.put(item.key, item.value)
                Log.i(TAG, "responseOfConnection: ${item.key} ${item.value}")
                Log.i(TAG, "responseOfConnection: ${currencyRatePairs.containsKey(item.key)}")
            }
            showCurrencyPanel(it.rates, llMainCurrencyPanelFrom)
            showCurrencyPanel(it.rates, llMainCurrencyPanelTo)
        }, {
            Log.i(TAG, "responseOfConnection: no")
        })


        // Add the base "EUR" for further calculation
        currencyRatePairs.put("EUR", 1.0)


        for (i in currencyRatePairs) {
            Log.i(TAG, "getResponse: ${i.key} : ${i.value} ***")
        }
        return currencyRatePairs
    }

     */

//    fun calculation(currencyNameFirst: String?, currencyNameSecond: String?, inputNum: Double): Double {
//        val currencyRateFirst: Double = rates.get(currencyNameFirst)
//        val currencyRateSecond: Double = rates.get(currencyNameSecond)
//        return inputNum / currencyRateFirst * currencyRateSecond
//    }

    fun showResult() {
        val resultOfCalculation: String =vm.calculation()
        if (resultOfCalculation == "null") {
            Toast.makeText(this, "no data", Toast.LENGTH_SHORT).show()
        } else {
            tvMainOutput.text = resultOfCalculation
        }
    }
}