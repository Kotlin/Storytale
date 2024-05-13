class GreetingPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    project.task("hi") {
      doLast {
        println("hi from plugin")
      }
    }
  }
}
