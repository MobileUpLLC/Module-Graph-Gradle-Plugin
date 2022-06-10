package ru.mobileup.modulegraph

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import java.io.File


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
        val task = project.tasks.create("parseModules", ParseModuleTask::class.java)
        val task2 = project.tasks.create("generateDotFile", CreateDotFileTask::class.java)
        val task3 = project.tasks.create("generateImageFile", GenerateGraphImageTask::class.java)

        val extension =
            project.extensions.create("moduleGraphExtension", ModuleGraphExtension::class.java)
        task.inputDirectory.set(extension.featuresDir)
        task.outputFile.set(extension.resultFile)
        task.applicationId.set(extension.applicationId)

        task2.inputFile.set(extension.resultFile)
        task2.outputFile.set(extension.resultDotFile)

        task3.inputFile.set(extension.resultDotFile)
        task3.outputFile.set(extension.resultImageFile)
    }
}
