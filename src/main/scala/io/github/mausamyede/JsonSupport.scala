package io.github.mausamyede

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
//  implicit val evaluationResultFormat: RootJsonFormat[EvaluationResult] = jsonFormat1(EvaluationResult)
}
