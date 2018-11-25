scalaVersion := "2.12.7"

val doobieVersion = "0.6.0"
val http4sVersion = "0.20.0-M3"

scalacOptions ++= Seq("-Ypartial-unification")

libraryDependencies ++= Seq(
  "org.tpolecat" %% "doobie-core" % doobieVersion,
  "org.xerial" % "sqlite-jdbc" % "3.23.1",
  "org.scala-lang.modules" %% "scala-xml" % "1.1.0",
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion
)
