val scala3Version = "3.6.4"

lazy val root = project
  .in(file("."))
  .settings(
    name := "dbgremlinjobpermission",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,
    semanticdbEnabled := true,

    scalacOptions += {"-Wunused:imports"},

    libraryDependencies += "org.scalameta" %% "munit" % "1.0.0" % Test,
    // https://mvnrepository.com/artifact/com.databricks/databricks-sdk-java
    libraryDependencies += "com.databricks" % "databricks-sdk-java" % "0.45.0",
    // https://mvnrepository.com/artifact/org.rogach/scallop
    libraryDependencies += "org.rogach" %% "scallop" % "5.2.0",
  )

assemblyMergeStrategy in assembly := {
 case PathList("META-INF", xs @ _*) => MergeStrategy.discard
 case x => MergeStrategy.first
}
