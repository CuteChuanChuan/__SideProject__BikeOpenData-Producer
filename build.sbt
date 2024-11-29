ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.15"

lazy val root = (project in file("."))
  .settings(
    name := "BikeDataProducer"
  )

val kafkaVersion               = "3.9.0"
val circeVersion               = "0.14.10"
val testcontainersScalaVersion = "0.41.4"
val slf4jVersion               = "2.0.16"
val nettyVersion               = "4.1.105.Final"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion) ++ Seq(
  "com.typesafe" % "config" % "1.4.3",
  "org.slf4j" % "slf4j-api" % slf4jVersion,
  "ch.qos.logback" % "logback-classic" % "1.5.12",
  "io.github.chronoscala" %% "chronoscala" % "2.0.13"
) ++ Seq(
  "org.scalatest" %% "scalatest" % "3.2.19" % Test,
  "org.mockito" %% "mockito-scala" % "1.17.37" % Test,
  "com.dimafeng" %% "testcontainers-scala-scalatest" % testcontainersScalaVersion % Test
)

libraryDependencies ++= Seq(
  "io.netty" % "netty-buffer",
  "io.netty" % "netty-codec",
  "io.netty" % "netty-common",
  "io.netty" % "netty-handler",
  "io.netty" % "netty-resolver",
  "io.netty" % "netty-transport",
  "io.netty" % "netty-transport-classes-epoll",
  "io.netty" % "netty-transport-native-epoll",
  "io.netty" % "netty-transport-native-unix-common"
).map(_ % nettyVersion)

libraryDependencies ++= Seq(
  "org.apache.kafka" %% "kafka",
  "org.apache.kafka" % "kafka-clients"
).map(_ % kafkaVersion exclude ("org.apache.logging.log4j", "log4j-slf4j-impl"))

assembly / assemblyMergeStrategy := {
  case PathList("META-INF", "io.netty.versions.properties") => MergeStrategy.first
  case PathList("module-info.class")                        => MergeStrategy.discard
  case x if x.endsWith("/module-info.class")                => MergeStrategy.discard
  case x                                                    =>
    val oldStrategy = (ThisBuild / assemblyMergeStrategy).value
    oldStrategy(x)
}
