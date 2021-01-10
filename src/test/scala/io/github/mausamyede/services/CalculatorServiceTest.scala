package io.github.mausamyede.services

import io.github.mausamyede.repositories.CalculatorRepo
import org.mockito.MockitoSugar.{mock, verify, when}
import org.scalatest.{FunSuite, Matchers}

class CalculatorServiceTest extends FunSuite with Matchers {
  private val mockRepo: CalculatorRepo = mock[CalculatorRepo]

  test("should evaluate and delegate result to repository") {
    when(mockRepo.readLatest10Results).thenReturn(List("2+2 = 4.0", "3*3 = 9.0"))
    val service = new CalculatorService(mockRepo)

    val result = service.evaluateExpression("2+2")

    verify(mockRepo).write("2+2 = 4.0")
    result shouldBe "2+2 = 4.0\n3*3 = 9.0"
  }

  test("should return error if unable to write to file") {
    when(mockRepo.readLatest10Results).thenReturn(List("2+2 = 4.0", "3*3 = 9.0"))
    when(mockRepo.write("2+2 = 4.0")).thenThrow(new RuntimeException)
    val service = new CalculatorService(mockRepo)

    val result = service.evaluateExpression("2+2")

    result shouldBe "Something went wrong"
  }

  test("should return error if unable to parse expression") {
    when(mockRepo.readLatest10Results).thenReturn(List("2+2 = 4.0", "3*3 = 9.0"))
    val service = new CalculatorService(mockRepo)

    val result = service.evaluateExpression("abcd")

    result shouldBe "Something went wrong"
  }

  test("evaluate should return result for expression containing subtraction") {
    val service = new CalculatorService(mockRepo)

    service.evaluate("2-4") shouldBe -2.0
  }

  test("evaluate should return result for expression multiplication") {
    val service = new CalculatorService(mockRepo)

    service.evaluate("2*4") shouldBe 8.0
  }

  test("evaluate should return result for expression division") {
    val service = new CalculatorService(mockRepo)

    service.evaluate("10/5") shouldBe 2.0
  }

  test("evaluate should return result for expression mix of operations") {
    val service = new CalculatorService(mockRepo)

    service.evaluate("10/3-(6-5)+4*5") shouldBe 22.333333333333332
  }
}
