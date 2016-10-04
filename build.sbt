import play.ebean.sbt.PlayEbean

name := """microservice-notifications"""

version := "0.0.1"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs
)

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.38"
