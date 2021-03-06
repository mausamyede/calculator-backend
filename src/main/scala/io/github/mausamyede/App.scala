package io.github.mausamyede

import akka.actor.ActorSystem
import akka.http.scaladsl.Http

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

object App extends JsonSupport {
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("calculatorApp")
    val calculator = new Calculator

    val route = calculator.route

    val host = "0.0.0.0"
    val port = sys.env.getOrElse("PORT", "8080").toInt
    Await.result(Http().newServerAt(host, port).bind(route), 10.seconds)
    println(s"Calculator server online at http://$host:$port/")
  }
}
