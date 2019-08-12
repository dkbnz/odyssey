name := "SENG302 TEAM 100 Everyware"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.12.8"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean).settings(
  watchSources ++= (baseDirectory.value / "public/ui" ** "*").get
)

libraryDependencies += guice
libraryDependencies += jdbc
libraryDependencies += javaWs
libraryDependencies += "com.h2database" % "h2" % "1.4.197"
libraryDependencies += "org.glassfish.jaxb" % "jaxb-core" % "2.3.0.1"
libraryDependencies += "org.glassfish.jaxb" % "jaxb-runtime" % "2.3.2"

// For Cucumber
libraryDependencies += "io.cucumber" % "cucumber-core" % "4.2.0" % Test
libraryDependencies += "io.cucumber" % "cucumber-jvm" % "4.2.0" % Test
libraryDependencies += "io.cucumber" % "cucumber-junit" % "4.2.0" % Test
libraryDependencies += "io.cucumber" % "cucumber-java" % "4.2.0"
libraryDependencies += "org.xerial" % "sqlite-jdbc" % "3.25.2"

// For database
// mySQL
libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.15"

libraryDependencies += "org.awaitility" % "awaitility" % "2.0.0" % Test
libraryDependencies += "org.assertj" % "assertj-core" % "3.6.2" % Test
libraryDependencies += "org.mockito" % "mockito-core" % "2.1.0" % Test
testOptions in Test += Tests.Argument(TestFrameworks.JUnit, "-a", "-v")

javacOptions ++= Seq("-Xlint:unchecked", "-Xlint:deprecation", "-Werror")

javaOptions in Test += "-Dconfig.file=conf/application.test.conf"

// Jacoco threshold values. If out report has less than set, test will fail.
jacocoReportSettings := JacocoReportSettings()
  .withThresholds(
    JacocoThresholds(
      instruction = 0,
      method = 0,
      branch = 0,
      complexity = 0,
      line = 0,
      clazz = 0)
  )
