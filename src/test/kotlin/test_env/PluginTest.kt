package test_env

import org.gradle.testkit.runner.GradleRunner
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import java.nio.file.Paths

class PluginTest {
    private val relativePathToTestEnvironment = "src/test/kotlin/test_env"
    private val pathToBuildGradleFile: String = "$relativePathToTestEnvironment/gradle/build.gradle"
    private val pathToSettingsGradleFile: String =
        "$relativePathToTestEnvironment/gradle/settings.gradle"
    private val testEnvironmentFolder =
        Paths.get(relativePathToTestEnvironment).toAbsolutePath().toFile()

    @get:Rule
    var testProjectDir = TemporaryFolder(testEnvironmentFolder)
    private var buildFile: File = File(pathToBuildGradleFile)
    private var settingsFile: File = File(pathToSettingsGradleFile)
    private lateinit var gradleRunner: GradleRunner

    @Before
    fun setup() {
        buildFile.copyTo(testProjectDir.newFile("build.gradle"), true)
        settingsFile.copyTo(testProjectDir.newFile("settings.gradle"), true)

        gradleRunner = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withPluginClasspath()
    }

    @Test
    fun `run generateModuleGraph task without errors`() {
        val result = gradleRunner
            .withArguments("generateModuleGraph")
            .build()

        println(result.output)
    }

    @Test
    fun `run detektCycle task without errors`() {
        val result = gradleRunner
            .withArguments("detectGraphCycles")
            .build()
        println(result.output)
    }
}