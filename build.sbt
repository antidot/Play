name := "AntidotSearchPlay"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache
)     

play.Project.playJavaSettings

libraryDependencies += "net.antidot" % "afs-api" % "1.1"
