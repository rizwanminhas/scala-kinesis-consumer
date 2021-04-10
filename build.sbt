name := """scala-kinesis-producer"""

organization := "com.rminhas"

version := "1.0-SNAPSHOT"

scalaVersion := "2.13.5"

libraryDependencies ++= Seq(
  "software.amazon.kinesis" % "amazon-kinesis-client" % "2.3.4",
  "com.amazonaws" % "amazon-kinesis-client" % "1.14.1",
  "org.slf4j" % "slf4j-api" % "1.7.29",
  "org.slf4j" % "slf4j-simple" % "1.7.29")
