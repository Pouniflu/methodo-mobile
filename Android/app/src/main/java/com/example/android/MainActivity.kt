package com.example.android

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly

class MainActivity : AppCompatActivity() {

    companion object {
        private val INPUT_BUTTONS = listOf(
                listOf("C", "CE"),
                listOf("1", "2", "3", "/"),
                listOf("4", "5", "6", "*"),
                listOf("7", "8", "9", "-"),
                listOf("0", ".", "=", "+")
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addCells(findViewById(R.id.calculator_input_container_line1), 0)
        addCells(findViewById(R.id.calculator_input_container_line2), 1)
        addCells(findViewById(R.id.calculator_input_container_line3), 2)
        addCells(findViewById(R.id.calculator_input_container_line4), 3)
        addCells(findViewById(R.id.calculator_input_container_line5), 4)
    }

    private fun addCells(linearLayout: LinearLayout, position: Int) {
        for (x in INPUT_BUTTONS[position].indices) {
            linearLayout.addView(
                    TextView(
                            ContextThemeWrapper(this, R.style.CalculatorInputButton)
                    ).apply {
                        text = INPUT_BUTTONS[position][x]
                        setOnClickListener { onCellClicked(this.text.toString()) }
                    },
                    LinearLayout.LayoutParams(
                            0,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            1f
                    )
            )
        }
    }

    private var input: Float? = null
    private var previousInput: Float? = null
    private var previousCalculation: Float? = null
    private var symbol: String? = null

    private fun onCellClicked(value: String) {
        fun updateDisplayContainer(value: Any) {
            findViewById<TextView>(R.id.calculator_display_container).text = value.toString()
        }

        fun onSymbolClicked(newSymbol: String) {
            if (symbol == null) {
                this.symbol = newSymbol
                previousInput = input
                input = null
            } else {
                when (symbol) {
                    "+" -> previousCalculation = (previousInput!! + input!!)
                    "-" -> previousCalculation = (previousInput!! - input!!)
                    "*" -> previousCalculation = (previousInput!! * input!!)
                    "/" -> previousCalculation = (previousInput!! / input!!)
                    else -> "Holà"
                }
                this.symbol = newSymbol
                previousInput = previousCalculation
                input = null
            }
        }

        fun onEqualsClicked() {
            if (input == null || previousInput == null || symbol == null) {
                return
            }

            updateDisplayContainer(when (symbol) {
                "+" -> previousInput!! + input!!
                "-" -> previousInput!! - input!!
                "*" -> previousInput!! * input!!
                "/" -> if(input == 0f) {
                            "IMPOSSIBLE"
                        } else {
                            previousInput!! / input!!
                        }
                else -> "ERROR"
            })

            input = null
            previousInput = null
            symbol = null
        }

        fun onResetClicked() {
            input = null
            previousInput = null
            symbol = null

            updateDisplayContainer("")
        }

        fun onDeleteClicked() {
            updateDisplayContainer("")
        }

        when {
            value.isNum() -> {
                input = value.toFloat()
                updateDisplayContainer(value)
            }

            value == "." -> onDecimalClicked()
            value == "C" -> onDeleteClicked()
            value == "CE" -> onResetClicked()
            value == "=" -> onEqualsClicked()
            listOf("/", "*", "-", "+").contains(value) -> onSymbolClicked(value)
        }
    }
}

fun String.isNum(): Boolean {
    return isDigitsOnly()
}