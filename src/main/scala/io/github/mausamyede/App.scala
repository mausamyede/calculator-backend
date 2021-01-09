package io.github.mausamyede

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

object App extends JsonSupport {
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("calculatorApp")
    val calculator = new Calculator

    val route =
      path("evaluate") {
        get {
          parameters("name") { name =>
            handleWebSocketMessages(calculator.flow(name))
          }
        }
      }

    Await.result(Http().newServerAt("0.0.0.0", 8080).bind(route), 10.seconds)
    println(s"Calculator server online at http://0.0.0.0:8080/")
  }
}