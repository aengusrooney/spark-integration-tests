import scala.util.Properties

import sbt._
import sbt.Keys._


object BuildSettings {
  val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := "org.apache.spark",
    version := "0.1-SNAPSHOT",
    scalaVersion := "2.10.4",
    resolvers ++= Seq(
      Resolver.sonatypeRepo("snapshots"),
      Resolver.sonatypeRepo("releases"),
      Resolver.typesafeRepo("releases")
    ),
    parallelExecution in Test := false
  )
}


object SparkIntegrationTestsBuild extends Build {

  import BuildSettings._

  val SPARK_HOME = Properties.envOrNone("SPARK_HOME").getOrElse {
    throw new Exception("SPARK_HOME must be defined")
  }

  lazy val sparkCore = ProjectRef(
    file(SPARK_HOME),
    "core"
  )

  lazy val root = Project(
    "spark-integration-tests",
    file("."),
    settings = buildSettings ++ Seq(
      scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature"),
      libraryDependencies ++= Seq(
        "org.scalatest" % "scalatest_2.10" % "2.2.1" % "test"
      )
    )
  ).dependsOn(sparkCore)
}