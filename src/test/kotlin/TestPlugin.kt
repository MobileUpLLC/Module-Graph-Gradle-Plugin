import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.internal.impldep.org.testng.Assert.assertNotNull
import org.gradle.testfixtures.ProjectBuilder
import org.junit.After
import org.junit.Before
import org.junit.Test
import ru.mobileup.modulegraph.gradle.ModuleGraphExtension
import ru.mobileup.modulegraph.gradle.ModuleGraphPlugin
import ru.mobileup.modulegraph.gradle.tasks.CreateDotFileTask
import ru.mobileup.modulegraph.gradle.tasks.GenerateGraphImageTask
import ru.mobileup.modulegraph.gradle.tasks.ParseModuleDependenciesTask
import java.io.File

class TestPlugin {

    var project: Project? = null

    @Before
    fun configureProject() {
        val pluginId = "ru.mobileup.modulegraph"
        val extensionId = ModuleGraphPlugin.EXTENSION_NAME
        val featureDir =
            File("/Users/Takexito/StudioProjects/Module-Graph-Gradle-Plugin/src/test/kotlin/features")
        val resultFile =
            File("/Users/Takexito/StudioProjects/Module-Graph-Gradle-Plugin/graph/result.json")
        val resultDotFile =
            File("/Users/Takexito/StudioProjects/Module-Graph-Gradle-Plugin/graph/result.dot")
        val resultImageFile =
            File("/Users/Takexito/StudioProjects/Module-Graph-Gradle-Plugin/graph/result.svg")
        val applicationId = "features"
        val project = ProjectBuilder.builder().build()

        project.plugins.apply(pluginId)
        project.extensions.configure<ModuleGraphExtension>(extensionId) {
            it.featuresDir.set(featureDir)
            it.modulesJsonFile.set(resultFile)
            it.applicationId.set(applicationId)
            it.resultDotFile.set(resultDotFile)
            it.resultImageFile.set(resultImageFile)
        }
        this.project = project
    }

    @After
    fun resetProject() {
        project = null
    }

    private fun <T : Task> getTask(name: String): T {
        val task = project!!.tasks.findByName(name)
        assertNotNull(task)
        return (task!! as T)
    }

    @Test
    fun checkModule() {
        val taskName = ModuleGraphPlugin.PARSE_MODULE_DEPENDENCIES_TASK_NAME
        val task = getTask<ParseModuleDependenciesTask>(taskName)
        task.run()
    }

    @Test
    fun checkDotFile() {
        val taskName = ModuleGraphPlugin.GENERATE_DOT_FILE_TASK_NAME
        val task = getTask<CreateDotFileTask>(taskName)
        task.run()
    }

    @Test
    fun checkImageFile() {
        val taskName = ModuleGraphPlugin.GENERATE_IMAGE_FILE_TASK_NAME
        val task = getTask<GenerateGraphImageTask>(taskName)
        task.run()
    }
}