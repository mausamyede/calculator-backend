package io.github.mausamyede.messages

import akka.actor.ActorRef

sealed trait CalculatorMessages

object CalculatorMessages {
  case class Evaluate(expression: String) extends CalculatorMessages
  case class UserJoined(name: String, userActor: ActorRef)
      extends CalculatorMessages
  case class UserLeft(name: String) extends CalculatorMessages
}
