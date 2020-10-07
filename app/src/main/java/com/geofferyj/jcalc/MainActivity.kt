package com.geofferyj.jcalc

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.WindowManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {
    val calc: Calculator = Calculator()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val parenStack: Stack<String> = Stack()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        btn_0.setOnClickListener {
            enterNumber("0")
        }

        btn_1.setOnClickListener {
            enterNumber("1")
        }

        btn_2.setOnClickListener {
            enterNumber("2")
        }

        btn_3.setOnClickListener {
            enterNumber("3")
        }

        btn_4.setOnClickListener {
            enterNumber("4")
        }

        btn_5.setOnClickListener {
            enterNumber("5")
        }

        btn_6.setOnClickListener {
            enterNumber("6")
        }

        btn_7.setOnClickListener {
            enterNumber("7")
        }

        btn_8.setOnClickListener {
            enterNumber("8")
        }

        btn_9.setOnClickListener {
            enterNumber("9")
        }
        btn_dot.setOnClickListener {
            val div = getString(R.string.div)
            val percent = getString(R.string.percent)
            val opPos = input.text.lastIndexOfAny(
                listOf("-", "+", "x", div, percent),
                input.text.lastIndex
            )

            if (input.text.isEmpty() || input.text.lastOrNull()!! in "-+x$div(") {
                input.append("0.")
            } else if (input.text.lastOrNull()!! in ")$percent") {
                input.append("x0.")
            } else if (opPos > input.text.lastIndexOf(".") && input.text.lastOrNull()!! in "0123456789") {
                input.append(".")
            } else if (opPos == -1 && input.text.lastIndexOf(".") == -1 ) {
                input.append(".")
            }

        }

        btn_percent.setOnClickListener {
            val percent = getString(R.string.percent)
            val div = getString(R.string.div)
            if (input.text.isNotEmpty() && input.text.lastOrNull()!! in "0123456789)") input.append(
                percent
            )
        }

        btn_plus.setOnClickListener {
            enterOper("+")
        }
        btn_minus.setOnClickListener {
            enterOper("-")
        }
        btn_div.setOnClickListener {
            val div: String = getString(R.string.div)
            enterOper(div)
        }
        btn_mult.setOnClickListener {
            enterOper("x")
        }


        btn_paren.setOnClickListener {

            if (input.text.isEmpty()) {
                input.append("(")
                parenStack.push("(")
            } else if (input.text.lastOrNull()!! in "-+x\u00f7") {
                input.append("(")
                parenStack.push("(")
            } else if (input.text.lastOrNull()!! in "1234567890)" && parenStack.isNotEmpty()) {
                input.append(")")
                parenStack.pop()
            } else if (input.text.lastOrNull()!! in "0123456789)" && parenStack.isEmpty()) {
                input.append("x(")
                parenStack.push("(")
            } else {
                input.append("(")
                parenStack.push("(")
            }

            Toast.makeText(this, parenStack.toString(), Toast.LENGTH_LONG).show()
        }

        btn_clear.setOnClickListener {
            if (parenStack.isNotEmpty() && input.text.lastOrNull()!! == '(') parenStack.pop()
            if (input.text.isNotEmpty()) input.text = input.text.dropLast(1)
        }

        btn_clear.setOnLongClickListener {
            input.text = ""
            parenStack.clear()
            true
        }

        btn_equals.setOnClickListener {
            input.text = calc.evaluate(input.text.toString())
        }
    }

    private fun enterOper(oper: String) {
        val div = getString(R.string.div)
        val percent = getString(R.string.percent)

        if (input.text.isNotEmpty()) {
            when {
                input.text.lastOrNull() == '(' -> {
                    if (oper in "+-") {
                        input.append(oper)
                    } else {
                        input.append("")
                    }
                }
                input.text.lastOrNull()!! in "+-x$div" -> {
                    input.text = input.text.dropLast(1)
                    if (oper in "+-" && input.text.lastOrNull()!! == '(') {
                        input.append(oper)
                    } else if (input.text.lastOrNull()!! in "0123456789)$percent") {
                        input.append(oper)
                    }
                }
                input.text.lastOrNull()!! in "0123456789)$percent" -> {
                    input.append(oper)
                }
            }

        }
    }

    private fun enterNumber(number: String) {
        val percent = getString(R.string.percent)
        if (input.text.isNotEmpty() && input.text.lastOrNull()!! in ")$percent") {
            input.append("x$number")
        } else {
            input.append(number)
        }
    }
}