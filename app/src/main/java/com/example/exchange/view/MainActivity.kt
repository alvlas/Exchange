package com.example.exchange.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.exchange.R
import com.example.exchange.data.Currencies
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
        }

        val currencyTargetValues: List<String> = enumValues<Currencies>().map { it.name }
        showCurrencyPanel(currencyTargetValues, llMainCurrencyPanelFrom, "from")
        showCurrencyPanel(currencyTargetValues, llMainCurrencyPanelTo, "to")

        etMainInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    currencyViewModel.chosenCurrencyLiveDataValue.value = s.toString()
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        bTest.setOnClickListener(View.OnClickListener {
            // add some tmp action if necessary
        })
    }

    private fun showCurrencyPanel(values: List<String>, llCurrencyPanel: ViewGroup, direction: String) {
        val inflater = layoutInflater
        for (currency in values) {
            val view = inflater.inflate(R.layout.currency_item, llCurrencyPanel, false)
            val tvItem = view.findViewById<TextView>(R.id.currency_item)
            tvItem.text = currency

            if (direction == "from") {
                view.setOnClickListener(View.OnClickListener {
                    currencyViewModel.chosenCurrencyLiveDataFrom.value = tvItem.text.toString()
                })
            } else {
                view.setOnClickListener(View.OnClickListener {
                    currencyViewModel.chosenCurrencyLiveDataTo.value = tvItem.text.toString()
                })
            }
            llCurrencyPanel.addView(view)
        }
    }

    private fun showResult() {
        tvMainResult.text = currencyViewModel.calculation()
//        val resultOfCalculation: String = currencyViewModel.calculation()
//        if (resultOfCalculation == "null") {
//             handle it
//        } else {
//            tvMainResult.text = resultOfCalculation
//        }
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
}