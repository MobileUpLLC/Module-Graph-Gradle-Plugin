package ru.mobileup.modulegraph.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import ru.mobileup.modulegraph.gradle.tasks.CreateDotFileTask
import ru.mobileup.modulegraph.gradle.tasks.GenerateGraphImageTask
import ru.mobileup.modulegraph.gradle.tasks.ParseModuleDependenciesTask

@Suppress("LeakingThis")
abstract class ModuleGraphExtension {
    abstract val featuresPackage: Property<String>
    abstract val featuresDirectory: DirectoryProperty
    abstract val outputDirectory: DirectoryProperty
    abstract val resultJsonFileName: Property<String>
    abstract val resultDotFileName: Property<String>
    abstract val resultImageFileName: Property<String>

    companion object {
        private const val DEFAULT_RESULT_JSON_FILE_NAME = "modules.json"
        private const val DEFAULT_RESULT_DOT_FILE_NAME = "modules.dot"
        private const val DEFAULT_RESULT_IMAGE_FILE_NAME = "modules.svg"
    }

    init {
        resultJsonFileName.convention(DEFAULT_RESULT_JSON_FILE_NAME)
        resultDotFileName.convention(DEFAULT_RESULT_DOT_FILE_NAME)
        resultImageFileName.convention(DEFAULT_RESULT_IMAGE_FILE_NAME)
    }
}

class ModuleGraphPlugin : Plugin<Project> {

    companion object {
        internal const val PARSE_MODULE_DEPENDENCIES_TASK_NAME = "parseModuleDependencies"
        internal const val GENERATE_DOT_FILE_TASK_NAME = "generateDotFile"
        internal const val GENERATE_IMAGE_FILE_TASK_NAME = "generateModuleGraph"
        internal const val EXTENSION_NAME = "moduleGraph"
    }

    override fun apply(project: Project) {
        val extension = project.extensions.create(
            EXTENSION_NAME,
            ModuleGraphExtension::class.java
        )

        val task1 = project.tasks.register(
            PARSE_MODULE_DEPENDENCIES_TASK_NAME,
            ParseModuleDependenciesTask::class.java
        )

        val task2 = project.tasks.register(
            GENERATE_DOT_FILE_TASK_NAME,
            CreateDotFileTask::class.java
        )

        val task3 = project.tasks.register(
            GENERATE_IMAGE_FILE_TASK_NAME,
            GenerateGraphImageTask::class.java
        )

        task1.configure { task ->
            task.featuresDirectory.set(extension.featuresDirectory)
            task.featuresPackage.set(extension.featuresPackage)
            task.outputJsonFile.set(extension.outputDirectory.file(extension.resultJsonFileName))
        }

        task2.configure { task ->
            task.outputDotFile.set(extension.outputDirectory.file(extension.resultDotFileName))
            task.moduleDependenciesJsonFile.set(task1.flatMap { it.outputJsonFile })
        }

        task3.configure { task ->
            task.outputFile.set(extension.outputDirectory.file(extension.resultDotFileName))
            task.dotFile.set(task2.flatMap { it.outputDotFile })
        }
    }
}
