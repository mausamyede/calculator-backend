lazy val akkaHttpVersion = "10.2.1"
lazy val akkaVersion = "2.6.10"

lazy val root = (project in file("."))
  .settings(
    inThisBuild(
      List(
        organization := "io.github.mausamyede",
        scalaVersion := "2.13.3"
      )
    ),
    name := "calculator-backend",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
      "org.scalatest" %% "scalatest" % "3.0.8" % Test,
      "org.mockito" %% "mockito-scala" % "1.16.0" % Test
    )
  )
  .enablePlugins(JavaAppPackaging)
