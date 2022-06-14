package ru.mobileup.modulegraph

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property


@Suppress("LeakingThis")
abstract class ModuleGraphExtension {
    abstract val featuresDir: Property<String>
    abstract val resultFile: Property<String>
    abstract val applicationId: Property<String>
    abstract val resultDotFile: Property<String>
    abstract val resultImageFile: Property<String>


    init {
        featuresDir.convention(DEFAULT_FEATURES_PATH)
        resultFile.convention(DEFAULT_RESULT_PATH)
        resultDotFile.convention(DEFAULT_RESULT_DOT_PATH)
        resultImageFile.convention(DEFAULT_RESULT_IMAGE_PATH)
    }

    companion object {
        private const val DEFAULT_RESULT_PATH = "gradle/dependency-graph/modules.json"
        private const val DEFAULT_RESULT_DOT_PATH = "gradle/dependency-graph/modules.dot"
        private const val DEFAULT_RESULT_IMAGE_PATH = "gradle/dependency-graph/modules.svg"
        private const val DEFAULT_FEATURES_PATH = "src/main/"
    }
}

class ModuleGraphPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val task = project.tasks.create("parseModules", ParseModuleDependenciesTask::class.java)
        val task2 = project.tasks.create("generateDotFile", CreateDotFileTask::class.java)
        val task3 = project.tasks.create("generateImageFile", GenerateGraphImageTask::class.java)

        val extension =
            project.extensions.create("moduleGraphExtension", ModuleGraphExtension::class.java)
        task.featuresDirectory.set(extension.featuresDir)
        task.outputJsonFile.set(extension.resultFile)
        task.applicationId.set(extension.applicationId)

        task2.moduleDependenciesJsonFile.set(extension.resultFile)
        task2.outputDotFile.set(extension.resultDotFile)

        task3.dotFilePath.set(extension.resultDotFile)
        task3.outputFilePath.set(extension.resultImageFile)
    }
}
