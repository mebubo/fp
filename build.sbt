scalaVersion := "2.12.6"

lazy val doobieVersion = "0.5.3"

scalacOptions ++= Seq("-Ypartial-unification")

libraryDependencies ++= Seq(
  "org.tpolecat" %% "doobie-core" % doobieVersion,
  "org.xerial" % "sqlite-jdbc" % "3.23.1",
  "org.scala-lang.modules" %% "scala-xml" % "1.1.0"
)
