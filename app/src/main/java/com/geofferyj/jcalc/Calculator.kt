package com.geofferyj.jcalc

import java.util.*

class Calculator {
    private fun isNumeric(strNum: String): Boolean {
        try {
            val d = strNum.toDouble()
        } catch (nfe: NumberFormatException) {
            return false
        }
        return true
    }

    private fun isOperator(c: String): Boolean {
        return when (c) {
            "+", "-", "x", "\u00f7", "%" -> true
            else -> false
        }
    }

    private fun operPrec(str: String): Int {
        when (str) {
            "+", "-" -> return 1
            "x", "\u00f7", "%" -> return 2
        }
        return -1
    }

    private fun tokenize(exp: String): Array<String> {
        return exp.split("((?<=[+\\-x/()%])|(?=[+\\-x/()%]))".toRegex()).toTypedArray()
    }

    private fun toPostfix(exp: String): Array<String> {

        // initializing empty String for result
        var result = ""

        // initializing empty stack
        val stack: Stack<String> = Stack()
        val expression = tokenize(exp)

        for (token in expression) {
            if (isNumeric(token)) {
                result += "$token "
            } else if (token == "(") {
                stack.push(token)
            } else if (token == ")") {
                while (stack.isNotEmpty()) {
                    result += if (stack.peek() != "(") {
                        stack.pop().toString() + " "
                    } else {
                        stack.pop()
                        break
                    }
                }
            } else if (isOperator(token)) {
                while (true) {
                    result += if (stack.isEmpty()) {
                        stack.push(token)
                        break
                    } else if (operPrec(token) <= operPrec(stack.peek())) {
                        stack.pop().toString() + " "
                    } else {
                        stack.push(token)
                        break
                    }
                }
            }
        }
        while (stack.isNotEmpty()) {
            result += stack.pop().toString() + " "
        }
        return result.split(" ".toRegex()).toTypedArray()
    }

    private fun isBinary(oper: String): Boolean{
        return when (oper) {
            "+", "-", "x", "\u00f7" -> true
            else -> false
        }
    }
    fun evaluate(exp: String): String? {
        val postfixExp = toPostfix(exp)
        val stack: Stack<String> = Stack()
        var value1: Double
        var value2: Double
        var result = 0.0
        for (token in postfixExp) {
            if (isNumeric(token)) {
                stack.push(token)
            } else if (isOperator(token)) {

                if (isBinary(token)){
                    value2 = stack.pop().toDouble()
                    if (stack.isNotEmpty()) {
                        value1 = stack.pop().toDouble()
                        when (token) {
                            "+" -> {
                                result = value1 + value2
                                stack.push(result.toString())
                            }
                            "-" -> {
                                result = value1 - value2
                                stack.push(result.toString())
                            }
                            "\u00f7" -> {
                                result = value1 / value2
                                stack.push(result.toString())
                            }
                            "x" -> {
                                result = value1 * value2
                                stack.push(result.toString())
                            }
                            "%" -> {
                                result = value1 * value2
                                stack.push(result.toString())
                            }
                        }
                    } else {
                        when (token) {
                            "+" -> stack.push(value2.toString())
                            "-" -> stack.push((-value2).toString())
                        }
                    }
                } else{
                    if (stack.isNotEmpty()) {
                        value1 = stack.pop().toDouble()
                        when (token) {
                            "%" -> {
                                result = value1 * 0.01
                                stack.push(result.toString())
                            }

                        }
                    }
                }

            }
        }
        return stack.pop()
    }


}