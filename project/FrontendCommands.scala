/**
  * Frontend build commands.
  * Change these if you are using some other package manager. i.e: Yarn
  */
object FrontendCommands {
  val dependencyInstall: String = "npm install"
  val test: String = "echo Skipping frontend tests"
  val serve: String = "npm run start"
  val build: String = "npm run build"
}