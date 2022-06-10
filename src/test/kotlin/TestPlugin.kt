import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.internal.impldep.org.testng.Assert.assertNotNull
import org.gradle.testfixtures.ProjectBuilder
import org.junit.After
import org.junit.Before
import org.junit.Test
import ru.mobileup.modulegraph.CreateDotFileTask
import ru.mobileup.modulegraph.GenerateGraphImageTask
import ru.mobileup.modulegraph.ModuleGraphExtension
import ru.mobileup.modulegraph.ParseModuleTask
import java.io.File

class TestPlugin {

    var project: Project? = null

    @Before
    fun configureProject(){
        val pluginId = "ru.mobileup.module-graph"
        val extensionId = "moduleGraphExtension"
        val featureDir = File("/Users/Takexito/StudioProjects/Module-Graph-Gradle-Plugin/src/test/kotlin/features")
        val resultFile = File("/Users/Takexito/StudioProjects/Module-Graph-Gradle-Plugin/graph/result.json")
        val resultDotFile = File("/Users/Takexito/StudioProjects/Module-Graph-Gradle-Plugin/graph/result.dot")
        val resultImageFile = File("/Users/Takexito/StudioProjects/Module-Graph-Gradle-Plugin/graph/result.svg")
        val applicationId = "features"
        val project = ProjectBuilder.builder().build()

        project.plugins.apply(pluginId)
        project.extensions.configure<ModuleGraphExtension>(extensionId) {
            it.featuresDir.set(featureDir)
            it.resultFile.set(resultFile)
            it.applicationId.set(applicationId)
            it.resultDotFile.set(resultDotFile)
            it.resultImageFile.set(resultImageFile)
        }
        this.project = project
    }

    @After
    fun resetProject(){
        project = null
    }

    private fun <T: Task> getTask(name: String): T{
        val task = project!!.tasks.findByName(name)
        assertNotNull(task)
        return (task!! as T)
    }

    @Test
    fun checkModule() {
        val taskName = "parseModules"
        val task = getTask<ParseModuleTask>(taskName)
        task.run()
    }

    @Test
    fun checkDotFile(){
        val taskName = "generateDotFile"
        val task = getTask<CreateDotFileTask>(taskName)
        task.run()
    }

    @Test
    fun checkImageFile(){
        val taskName = "generateImageFile"
        val task = getTask<GenerateGraphImageTask>(taskName)
        task.run()
    }
}