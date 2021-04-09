package com.example.exchange.view

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
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.exchange.R
import com.example.exchange.viewmodel.CurrencyViewModel


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var llMainCurrencyPanel: LinearLayout
    private lateinit var llMainCurrencyPanelFrom: LinearLayout
    private lateinit var llMainCurrencyPanelTo: LinearLayout
    private lateinit var etMainInput: EditText
    private lateinit var tvMainResult: TextView
    private lateinit var bTest: Button
    private lateinit var currencyViewModel: CurrencyViewModel
    private lateinit var observer: Observer<String>
//    var rates: HashMap<String, Double> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        llMainCurrencyPanel = findViewById<LinearLayout>(R.id.ll_main_currency_panel)
        llMainCurrencyPanelFrom = findViewById<LinearLayout>(R.id.ll_main_currency_panel_from)
        llMainCurrencyPanelTo = findViewById<LinearLayout>(R.id.ll_main_currency_panel_to)
        etMainInput = findViewById<EditText>(R.id.et_main_input)
        tvMainResult = findViewById<TextView>(R.id.tv_main_output)
        bTest = findViewById<Button>(R.id.b_test)

        val vm: CurrencyViewModel by viewModels()
        currencyViewModel = vm
        observer = Observer {
            showResult()

//            if (!it.isNullOrEmpty() && it != "0" && it != "0.0") {
//
//            }
//            else if (!it.isDigitsOnly()) {
//                Toast.makeText(this, "Only digits are allowed here", Toast.LENGTH_SHORT)
//                    .show()
//            }
        }

        val currencyArray: Array<String> = arrayOf("EUR", "USD", "RUB", "GBP", "CHF", "CNY")
        showCurrencyPanel(currencyArray, llMainCurrencyPanelFrom, "from")
        showCurrencyPanel(currencyArray, llMainCurrencyPanelTo, "to")

        etMainInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    currencyViewModel.chosenCurrencyLiveDataValue.value = s.toString()
//                    showResult()
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
        if (savedInstanceState != null) {
            currencyViewModel.chosenCurrencyLiveDataFrom.value =
                savedInstanceState.getString("chosenCurrencyLiveDataFrom")
            currencyViewModel.chosenCurrencyLiveDataTo.value =
                savedInstanceState.getString("chosenCurrencyLiveDataTo")
            currencyViewModel.chosenCurrencyLiveDataValue.value =
                savedInstanceState.getString("chosenCurrencyLiveDataValue")
            etMainInput.setText(savedInstanceState.getString("etMainInput"))
//            showResult()
        }
    }

    fun showCurrencyPanel(array: Array<String>, llCurrencyPanel: ViewGroup, direction: String) {
        val inflater = layoutInflater
        for (currency in array) {
            val view = inflater.inflate(R.layout.currency_item, llCurrencyPanel, false)
            val tvItem = view.findViewById<TextView>(R.id.currency_item)
            tvItem.text = currency

            if (direction == "from") {
                view.setOnClickListener(View.OnClickListener {
                    currencyViewModel.chosenCurrencyLiveDataFrom.value = tvItem.text.toString()
//                    showResult()
                })
            } else {
                view.setOnClickListener(View.OnClickListener {
                    currencyViewModel.chosenCurrencyLiveDataTo.value = tvItem.text.toString()
//                    showResult()
                })
            }
            llCurrencyPanel.addView(view)
        }
    }

    fun showResult() {
        val resultOfCalculation: String = currencyViewModel.calculation()
        if (resultOfCalculation == "null") {
            Toast.makeText(this, "no data", Toast.LENGTH_SHORT).show()
        } else {
            tvMainResult.text = resultOfCalculation
        }
    }

    override fun onStart() {
        super.onStart()
        currencyViewModel.chosenCurrencyLiveDataFrom.observe(this, observer)
        currencyViewModel.chosenCurrencyLiveDataTo.observe(this, observer)
        currencyViewModel.chosenCurrencyLiveDataValue.observe(this, observer)
//        currencyViewModel.currenciesRatesLiveData.observe(this, observer)
//        Add the rates Observer after WorkManager auto updating
    }

    override fun onStop() {
        super.onStop()
        currencyViewModel.chosenCurrencyLiveDataFrom.removeObserver(observer)
        currencyViewModel.chosenCurrencyLiveDataTo.removeObserver(observer)
        currencyViewModel.chosenCurrencyLiveDataValue.removeObserver(observer)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("chosenCurrencyLiveDataFrom",
            currencyViewModel.chosenCurrencyLiveDataFrom.value)
        outState.putString("chosenCurrencyLiveDataTo",
            currencyViewModel.chosenCurrencyLiveDataTo.value)
        outState.putString("chosenCurrencyLiveDataValue",
            currencyViewModel.chosenCurrencyLiveDataValue.value)
        outState.putString("etMainInput", etMainInput.text.toString())
    }
}