package io.github.mausamyede.services

import io.github.mausamyede.repositories.CalculatorRepo

import scala.collection.mutable

class CalculatorService(repo: CalculatorRepo) {

  def evaluateExpression(expression: String): String = {
    val result = evaluate(expression)
    repo.write(s"$expression = $result")
    repo.readFirst10Results.mkString("\n")
  }

  private[services] def evaluate(expression: String): Double = {
    val tokens = extractTokensFrom(expression)
    val numbers = mutable.Stack[Double]()
    val operators = mutable.Stack[String]()
    tokens.foreach(t => {
      if (t.matches("[0-9]+")) {
        numbers.push(t.toInt)
      } else if (t == "(") {
        operators.push(t)
      } else if (t == ")") {
        while (operators.top != "(") {
          numbers.push(operate(operators.pop(), numbers.pop(), numbers.pop()))
        }
        operators.pop()
      } else if (tokenIsOperator(t)) {
        while (operators.nonEmpty && hasPrecedence(t, operators.top)) {
          numbers.push(operate(operators.pop(), numbers.pop(), numbers.pop()))
        }
        operators.push(t)
      }
    })
    while (operators.nonEmpty) {
      numbers.push(operate(operators.pop(), numbers.pop(), numbers.pop()))
    }
    numbers.pop()
  }

  private def extractTokensFrom(expression: String): Array[String] = {
    expression.replaceAll("\\s", "").split("(?<=[-+*/()])|(?=[-+*/()])")
  }

  private def operate(operator: String, op1: Double, op2: Double): Double = {
    operator match {
      case "+" => op2 + op1
      case "-" => op2 - op1
      case "*" => op2 * op1
      case "/" => op2 / op1
    }
  }

  private def hasPrecedence(op1: String, op2: String): Boolean = {
    if (op2 == "(" || op2 == ")") return false
    if ((op1 == "*" || op1 == "/") && (op2 == "+" || op2 == "-")) false
    else true
  }

  private def tokenIsOperator(token: String): Boolean =
    token == "+" || token == "-" || token == "*" || token == "/"
}
