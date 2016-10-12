import play.ebean.sbt.PlayEbean

name := """microservice-notifications"""

version := "0.0.3"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "mysql" % "mysql-connector-java" % "5.1.38",
  "org.igniterealtime.smack" % "smack-tcp" % "4.1.8",
  "org.igniterealtime.smack" % "smack-resolver-javax" % "4.1.8",
  "org.igniterealtime.smack" % "smack-java7" % "4.1.8",
  "org.igniterealtime.smack" % "smack-im" % "4.1.8",
  "org.igniterealtime.smack" % "smack-extensions" % "4.1.8",
  "com.squareup.retrofit2" % "retrofit" % "2.1.0"
)
