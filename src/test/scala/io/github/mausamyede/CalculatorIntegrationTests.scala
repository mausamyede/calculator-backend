package io.github.mausamyede

import akka.http.scaladsl.testkit.{ScalatestRouteTest, WSProbe}
import org.scalatest.FunSuite

class CalculatorIntegrationTests extends FunSuite with ScalatestRouteTest {

  val wsClient: WSProbe = WSProbe()
  private val calculator = new Calculator

  test("/evaluate should evaluate given expression over opened channel") {
    WS("/evaluate?name=abc", wsClient.flow) ~> calculator.route ~>
      check {
        wsClient.sendMessage("2+2")
        wsClient.expectMessage("2+2 = 4.0")
      }
  }
}
