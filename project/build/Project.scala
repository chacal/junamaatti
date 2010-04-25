import sbt.{ProjectInfo, DefaultProject}

class Project(info: ProjectInfo) extends AndroidProject(info) with IdeaPlugin {
  def androidPlatformName = "android-7"

  override def useMavenConfigurations = true
  override def shouldCheckOutputDirectories = false

  override def updateAction = task { None }

// Enable this if specialization causes harm (some known issues in 2.8.0.RC1).
// override def compileOptions = super.compileOptions ++ Seq("-no-specialization").map(CompileOption(_))
}
