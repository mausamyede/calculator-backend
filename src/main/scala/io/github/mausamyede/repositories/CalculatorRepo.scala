package io.github.mausamyede.repositories

import java.nio.file.{Files, Path, StandardOpenOption}
import scala.io.Source
import scala.util.Using

class CalculatorRepo(storeFilePath: Path) {
  def write(evalResult: String): Unit = {
    Files.write(storeFilePath, s"$evalResult\n".getBytes, StandardOpenOption.APPEND)
  }

  def readLatest10Results: List[String] = {
    Using(Source.fromFile(storeFilePath.toFile)) {
      _.getLines().toList.reverse.take(10)
    }.get
  }
}
