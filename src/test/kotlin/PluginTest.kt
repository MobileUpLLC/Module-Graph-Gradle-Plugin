import org.gradle.testkit.runner.GradleRunner
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import java.nio.file.Paths

/**
 * ###Warning:
 * TemporaryFolder Rule don't delete all temp file at TestEnvironment directory
 */
class PluginTest {
    private val relativePathToTestEnvironment = "src/test/kotlin/test_env"
    private val relativePathToFeaturesDirectory: String = "../features"
    private val relativePathToOutputDirectory: String = "../outputs"
    private val testEnvironmentFolder = Paths.get(relativePathToTestEnvironment).toAbsolutePath().toFile()

    @get:Rule
    var testProjectDir = TemporaryFolder(testEnvironmentFolder)
    private lateinit var buildFile: File
    private lateinit var settingsFile: File
    private lateinit var gradleRunner: GradleRunner

    @Before
    fun setup() {
        setupBuildGradleFile()
        setupSettingsGradleFile()

        gradleRunner = GradleRunner.create()
            .withPluginClasspath()
            .withProjectDir(testProjectDir.root)
            .withTestKitDir(testProjectDir.newFolder())
    }

    private fun setupBuildGradleFile(){
        buildFile = testProjectDir.newFile("build.gradle")

        buildFile.appendText("""
        plugins {
            id("ru.mobileup.module-graph")
        }
    """.trimIndent())

        buildFile.appendText("""
            moduleGraph {
                featuresPackage = "test_env.features"
                featuresDirectory = project.file("$relativePathToFeaturesDirectory")
                outputDirectory = project.file("$relativePathToOutputDirectory")
            }
        """.trimIndent())
    }

    private fun setupSettingsGradleFile(){
        settingsFile = testProjectDir.newFile("settings.gradle")

        settingsFile.appendText("""
            rootProject.name = "module-graph"
        """.trimIndent())
    }

    @Test
    fun `run generateModuleGraph task without errors`() {
        val result = gradleRunner
            .withArguments("generateModuleGraph")
            .build()
    }
}