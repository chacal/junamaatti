import sbt.{ProjectInfo, DefaultProject, ParentProject}

class Project(info: ProjectInfo) extends ParentProject(info) with IdeaPlugin {

  override def shouldCheckOutputDirectories = false

  lazy val androidclient = project("android-client", "android-client", new AndroidClientProject(_))
  lazy val androidtests = project("android-tests", "android-tests", new AndroidTestsProject(_), androidclient)

  class AndroidClientProject(info: ProjectInfo) extends AndroidProject(info) with CommonConfig {
  }

  class AndroidTestsProject(info: ProjectInfo) extends AndroidTestProject(info) with CommonConfig {
    val specs = "org.scala-tools.testing" % "specs" % "1.6.2"
    val easymock = "org.easymock" % "easymock" % "2.0"
    val easymockExtensions = "org.easymock" % "easymockclassextension" % "2.2.2"
  }
}

trait CommonConfig extends IdeaPlugin {
  def androidPlatformName = "android-7"
  val commonsIO = "commons-io" % "commons-io" % "1.4" withSources()
  override def useMavenConfigurations = true
}
