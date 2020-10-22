name := "SlickStart"

version := "0.1"

val commonSettings = Seq(
  scalaVersion := "2.13.3"
)


val models = Project(id = "models", base = file("models"))
  .settings(
    commonSettings: _*
  )

val repositories =
  Project(id = "repositories", base = file("repositories"))
    .dependsOn(models)
    .settings(
      libraryDependencies ++= Seq(
        "com.typesafe.slick" %% "slick" % "3.3.2",
        "com.typesafe" % "config" % "1.4.0",
        "com.typesafe.slick" %% "slick-hikaricp" % "3.3.2",
        "org.postgresql" % "postgresql" % "42.2.18",
        "com.h2database" % "h2" % "1.4.200",
        "ch.qos.logback" % "logback-classic" % "1.2.3",
        "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
      )
    )
    .settings(commonSettings :_*)

val api =
  Project(id = "api", base = file("api"))
    .dependsOn(repositories)
    .settings(commonSettings :_*)