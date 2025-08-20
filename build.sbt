val scala3Version = "3.7.2"

lazy val root = project
  .in(file("."))
  .settings(
    name := "weather-points",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % "1.0.0" % Test,
      "com.softwaremill.sttp.client3" %% "core" % "3.9.1",
      "com.softwaremill.sttp.client3" %% "circe" % "3.9.1",
      "io.circe" %% "circe-core" % "0.14.6",
      "io.circe" %% "circe-generic" % "0.14.6",
      "io.circe" %% "circe-parser" % "0.14.6",
      "com.github.pureconfig" %% "pureconfig-core" % "0.17.4",
      "com.softwaremill.sttp.client3" %% "slf4j-backend" % "3.9.1"
    )
  )
