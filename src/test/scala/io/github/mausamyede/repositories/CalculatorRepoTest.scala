package io.github.mausamyede.repositories

import org.scalatest.{BeforeAndAfterEach, FunSuite, Matchers}

import java.nio.file.{Files, Path, Paths}

class CalculatorRepoTest
    extends FunSuite
    with Matchers
    with BeforeAndAfterEach {

  val storeFilePath: Path = Paths.get("testStore.txt")

  override protected def beforeEach(): Unit = Files.createFile(storeFilePath)

  override protected def afterEach(): Unit = Files.delete(storeFilePath)

  test("write should append the evaluation result to store file") {
    val repo = new CalculatorRepo(storeFilePath)

    repo.write("2+2 = 4.0")
    repo.write("3+3 = 6.0")

    Files.readString(storeFilePath) shouldBe "2+2 = 4.0\n3+3 = 6.0\n"
  }

  test(
    "readLatest10Results should return last 10 lines from store file if exists"
  ) {
    val repo = new CalculatorRepo(storeFilePath)
    val str =
      "line1\nline2\nline3\nline4\nline5\nline6\nline7\nline8\nline9\nline10\n"
    Files.write(storeFilePath, str.getBytes)

    repo.readLatest10Results shouldBe List(
      "line10",
      "line9",
      "line8",
      "line7",
      "line6",
      "line5",
      "line4",
      "line3",
      "line2",
      "line1"
    )
  }

  test(
    "readLatest10Results should return all lines from store file if less than 10"
  ) {
    val repo = new CalculatorRepo(storeFilePath)
    val str =
      "line1\nline2\nline3\nline4\nline5\nline6\n"
    Files.write(storeFilePath, str.getBytes)

    repo.readLatest10Results shouldBe List(
      "line6",
      "line5",
      "line4",
      "line3",
      "line2",
      "line1"
    )
  }

  test(
    "readLatest10Results should return last 10 lines from store file if more than 10"
  ) {
    val repo = new CalculatorRepo(storeFilePath)
    val str =
      "line1\nline2\nline3\nline4\nline5\nline6\nline7\nline8\nline9\nline10\nline11\nline12\n"
    Files.write(storeFilePath, str.getBytes)

    repo.readLatest10Results shouldBe List(
      "line12",
      "line11",
      "line10",
      "line9",
      "line8",
      "line7",
      "line6",
      "line5",
      "line4",
      "line3"
    )
  }
}
