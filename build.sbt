import com.trueaccord.scalapb.{ScalaPbPlugin ⇒ PB}
import de.heikoseeberger.sbtheader.license.MIT
import sbt.Keys._
import sbt._

val sOptions = Seq(
  "-encoding",
  "UTF-8",
  "-deprecation",
  "-unchecked",
  "-feature",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture"
)

val jOptions = Seq(
  "-source",
  "1.8",
  "-target",
  "1.8",
  "-Xlint:unchecked",
  "-Xlint:deprecation",
  "-Xlint:-options"
)

lazy val protobufSettings = PB.protobufSettings ++ Seq(
  version in PB.protobufConfig := "3.5.0",
  // Protoc from jar
  PB.runProtoc in PB.protobufConfig := (args =>
    com.github.os72.protocjar.Protoc.runProtoc("-v350" +: args.toArray))
)

resolvers += Resolver.bintrayRepo("cakesolutions", "maven")

lazy val kafkaWire = (project in file("."))
  .enablePlugins(AutomateHeaderPlugin)
  .settings(protobufSettings: _*)
  .settings(
    name := "kafka-wire",
    version := "0.1.0",
    scalaVersion := "2.11.8",
    scalacOptions ++= sOptions,
    javacOptions ++= jOptions,
    headers := Map(
      "scala" -> MIT("2016", "Cake Solutions Limited"),
      "conf" -> MIT("2016", "Cake Solutions Limited", "#")
    ),
    libraryDependencies ++= Seq(
    "com.lihaoyi" %% "autowire" % "0.2.6",
      "net.cakesolutions" %% "scala-kafka-client-akka" % "1.0.0",
      "net.cakesolutions" %% "scala-kafka-client-testkit" % "1.0.0" % Test,
      "com.typesafe.akka" %% "akka-testkit" % "2.5.8" % Test,
      "com.trueaccord.scalapb" %% "scalapb-runtime" % "0.6.6",
      "com.trueaccord.scalapb" %% "scalapb-runtime-grpc" % "0.6.6",
      "com.google.protobuf" % "protobuf-java" % (version in PB.protobufConfig).value % PB.protobufConfig.name,
      "ch.qos.logback" % "logback-classic" % "1.1.6",
      "org.scalatest" %% "scalatest" % "3.0.0" % Test
    ),
    scalafmtConfig in ThisBuild := Some(file(".scalafmt"))
  )
  .settings(reformatOnCompileSettings)


