import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.github.mhodovaniuk",
      scalaVersion := "2.12.4",
      version := "0.0.1-SNAPSHOT"
    )),
    name := "hibernate-log-util",
    libraryDependencies ++= Seq(
      "com.github.scopt" %% "scopt" % "3.7.0",
      "com.jsuereth" %% "scala-arm" % "2.0"
    )
  )
