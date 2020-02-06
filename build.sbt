name := "book-race"
version := "0.0.1-SNAPSHOT"

scalaVersion := "2.12.7"

val http4sVersion = "0.20.17"
val mockitoVersion = "1.11.2"
val pureConfigVersion = "0.12.2"

scalacOptions ++= Seq(
  "-encoding", "UTF-8", // source files are in UTF-8
  "-deprecation", // warn about use of deprecated APIs
  "-unchecked", // warn about unchecked type parameters
  "-feature", // warn about misused language features
  "-language:higherKinds", // allow higher kinded types without `import scala.language.higherKinds`
  "-Xlint", // enable handy linter warnings
  "-Xfatal-warnings", // turn compiler warnings into errors
  "-Ypartial-unification" // allow the compiler to unify type constructors of different arities
)

libraryDependencies += "org.typelevel" %% "cats-core" % "1.4.0"
libraryDependencies += "org.typelevel" %% "cats-effect" % "2.1.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.0" % Test

libraryDependencies ++= Seq(
  "org.mockito" %% "mockito-scala-scalatest" % mockitoVersion % Test,
  "org.mockito" %% "mockito-scala-cats" % mockitoVersion % Test
)

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,
  "org.http4s" %% "http4s-circe" % http4sVersion,
  "io.circe" %% "circe-generic" % "0.11.2"
)
libraryDependencies += "com.github.pureconfig" %% "pureconfig" % pureConfigVersion
libraryDependencies += "com.github.pureconfig" %% "pureconfig-cats-effect" % pureConfigVersion
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.3")
