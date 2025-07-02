package com.shyam.calculator

import android.content.Intent
import android.icu.text.DecimalFormat
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.shyam.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding

    var number: String? = null
    var firstNumber: Double = 0.0
    var lastNumber: Double = 0.0
    var status: String? = null
    var operator: Boolean = false

    val myFormatter = DecimalFormat("######.######")

    var history: String = ""
    var currentResult: String = ""
    var buttonEqualsControl: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.textViewResult.text = "0"
        mainBinding.textViewHistory.text = ""

        // Number Buttons
        mainBinding.btnZero.setOnClickListener { onNumberClicked("0") }
        mainBinding.btnOne.setOnClickListener { onNumberClicked("1") }
        mainBinding.btnTwo.setOnClickListener { onNumberClicked("2") }
        mainBinding.btnThree.setOnClickListener { onNumberClicked("3") }
        mainBinding.btnFour.setOnClickListener { onNumberClicked("4") }
        mainBinding.btnFive.setOnClickListener { onNumberClicked("5") }
        mainBinding.btnSix.setOnClickListener { onNumberClicked("6") }
        mainBinding.btnSeven.setOnClickListener { onNumberClicked("7") }
        mainBinding.btnEight.setOnClickListener { onNumberClicked("8") }
        mainBinding.btnNine.setOnClickListener { onNumberClicked("9") }

        // Dot
        mainBinding.btnDot.setOnClickListener {
            if (buttonEqualsControl) {
                number = "0."
                buttonEqualsControl = false
            } else if (number == null) {
                number = "0."
            } else if (!number!!.contains(".")) {
                number += "."
            }
            mainBinding.textViewResult.text = number
        }

        // AC
        mainBinding.btnAC.setOnClickListener {
            onButtonACClicked()
        }

        // DEL
        mainBinding.btnDEL.setOnClickListener {
            number = number?.dropLast(1)
            if (number.isNullOrEmpty()) {
                number = null
                mainBinding.textViewResult.text = "0"
            } else {
                mainBinding.textViewResult.text = number
            }
        }

        // Operators
        mainBinding.btnPlus.setOnClickListener { onOperatorClicked("addition", "+") }
        mainBinding.btnMinus.setOnClickListener { onOperatorClicked("subtraction", "-") }
        mainBinding.btnMulti.setOnClickListener { onOperatorClicked("multiplication", "*") }
        mainBinding.btnDivide.setOnClickListener { onOperatorClicked("divide", "/") }

        // Equal
        mainBinding.btnEqual.setOnClickListener {
            if (number != null && status != null) {
                when (status) {
                    "multiplication" -> multiply()
                    "divide" -> divide()
                    "subtraction" -> minus()
                    "addition" -> plus()
                }
                mainBinding.textViewHistory.text =
                    "${myFormatter.format(firstNumber)} ${statusSymbol(status!!)} ${number} ="
                number = myFormatter.format(firstNumber)
                mainBinding.textViewResult.text = number
                operator = false
                status = null
                buttonEqualsControl = true
            }
        }
    }

    private fun onNumberClicked(clickNumber: String) {
        if (buttonEqualsControl) {
            number = clickNumber
            buttonEqualsControl = false
            firstNumber = 0.0
            lastNumber = 0.0
            status = null
            mainBinding.textViewHistory.text = ""
        } else if (number == null || number == "0") {
            number = clickNumber
        } else {
            number += clickNumber
        }

        mainBinding.textViewResult.text = number
        operator = true
    }

    private fun onOperatorClicked(operation: String, symbol: String) {
        if (number != null) {
            firstNumber = number!!.toDouble()
            status = operation
            mainBinding.textViewHistory.text = "${myFormatter.format(firstNumber)} $symbol"
            number = null
            operator = false
            mainBinding.textViewResult.text = "0"
            buttonEqualsControl = false
        }

        mainBinding.toolbar.setOnMenuItemClickListener{ item->
            when(item.itemId){
                R.id.settings_items->{
                    val intent = Intent(this@MainActivity,changingConfigurations::class.java)
                    startActivity(intent)
                    return@setOnMenuItemClickListener true
                }
                else->return@setOnMenuItemClickListener false
            }
        }
    }

    private fun onButtonACClicked() {
        number = null
        firstNumber = 0.0
        lastNumber = 0.0
        status = null
        operator = false
        buttonEqualsControl = false
        mainBinding.textViewResult.text = "0"
        mainBinding.textViewHistory.text = ""
    }

    private fun plus() {
        lastNumber = number?.toDoubleOrNull() ?: return
        firstNumber += lastNumber
    }

    private fun minus() {
        lastNumber = number?.toDoubleOrNull() ?: return
        firstNumber -= lastNumber
    }

    private fun multiply() {
        lastNumber = number?.toDoubleOrNull() ?: return
        firstNumber *= lastNumber
    }

    private fun divide() {
        lastNumber = number?.toDoubleOrNull() ?: return
        if (lastNumber == 0.0) {
            Toast.makeText(applicationContext, "The divisor cannot be zero", Toast.LENGTH_LONG)
                .show()
        } else {
            firstNumber /= lastNumber
        }
    }

    private fun statusSymbol(status: String): String {
        return when (status) {
            "addition" -> "+"
            "subtraction" -> "-"
            "multiplication" -> "*"
            "divide" -> "/"
            else -> ""
        }
    }
}
