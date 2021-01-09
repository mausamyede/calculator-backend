package io.github.mausamyede.actors

import akka.actor.{Actor, ActorRef}
import io.github.mausamyede.messages.CalculatorMessages.{Evaluate, UserJoined, UserLeft}
import io.github.mausamyede.repositories.CalculatorRepo
import io.github.mausamyede.services.CalculatorService

import java.nio.file.{Files, Path}
import scala.collection.mutable

class CalculatorActor extends Actor {
  val users: mutable.Map[String, ActorRef] = mutable.Map.empty[String, ActorRef]

  val storeFilePath: Path = Files.createTempFile("calc-", ".txt")
  storeFilePath.toFile.deleteOnExit()
  val calculatorRepo = new CalculatorRepo(storeFilePath)
  val calculatorService = new CalculatorService(calculatorRepo)

  override def receive: Receive = {
    case UserJoined(name, actorRef) =>
      users.put(name, actorRef)
      println(s"$name joined the calculator.")

    case UserLeft(name) =>
      users.remove(name)
      println(s"$name left the calculator.")

    case Evaluate(exp) =>
      val result = calculatorService.evaluateExpression(exp)
      broadcast(result)
  }

  def broadcast(msg: String): Unit = {
    users.values.foreach(_ ! msg)
  }
}
