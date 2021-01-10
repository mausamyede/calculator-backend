package io.github.mausamyede

import akka.Done
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.model.ws.{BinaryMessage, Message, TextMessage}
import akka.http.scaladsl.server.Directives.{get, handleWebSocketMessages, parameters, path}
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import akka.stream.{CompletionStrategy, Materializer, OverflowStrategy}
import io.github.mausamyede.actors.CalculatorActor
import io.github.mausamyede.messages.CalculatorMessages.{Evaluate, UserJoined, UserLeft}
import org.reactivestreams.Publisher

class Calculator(implicit system: ActorSystem, mat: Materializer) {

  private val calcActor = system.actorOf(Props(classOf[CalculatorActor]))

  def route: Route = {
    path("evaluate") {
      get {
        parameters("name") { name =>
          handleWebSocketMessages(flow(name))
        }
      }
    }
  }

  private def flow(userName: String): Flow[Message, Message, Any] = {
    val (actorRef: ActorRef, publisher: Publisher[TextMessage.Strict]) =
      Source
        .actorRef[String](
          completionMatcher = completeWithDone,
          failureMatcher = PartialFunction.empty,
          bufferSize = 16,
          overflowStrategy = OverflowStrategy.fail
        )
        .map(msg => TextMessage.Strict(msg))
        .toMat(Sink.asPublisher(false))(Keep.both)
        .run()

    calcActor ! UserJoined(userName, actorRef)

    val sink: Sink[Message, Any] = Flow[Message]
      .map {
        case TextMessage.Strict(exp) => calcActor ! Evaluate(exp)
        case _ => "Invalid input"
      }
      .to(Sink.onComplete(_ => calcActor ! UserLeft(userName)))

    Flow.fromSinkAndSource(sink, Source.fromPublisher(publisher))
  }

  val completeWithDone: PartialFunction[Any, CompletionStrategy] = {
    case Done => CompletionStrategy.immediately
  }
}
