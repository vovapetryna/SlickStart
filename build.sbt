name := "SlickStart"

version := "0.1"

scalaVersion := "2.13.3"

libraryDependencies ++= Seq(
  "com.typesafe.slick"         %% "slick"           % "3.3.2",
  "com.typesafe"                % "config"          % "1.4.0",
  "com.typesafe.slick"         %% "slick-hikaricp"  % "3.3.2",
  "org.postgresql"              % "postgresql"      % "42.2.18",
  "com.h2database"              % "h2"              % "1.4.200",
  "ch.qos.logback"              % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging"   % "3.9.2"
)
