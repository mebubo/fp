scalaVersion := "2.12.6"

lazy val doobieVersion = "0.5.3"

libraryDependencies ++= Seq(
  "org.tpolecat" %% "doobie-core" % doobieVersion,
  "org.xerial" % "sqlite-jdbc" % "3.23.1"
)
