package ru.mobileup.modulegraph.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import ru.mobileup.modulegraph.gradle.tasks.CreateDotFileTask
import ru.mobileup.modulegraph.gradle.tasks.GenerateGraphImageTask
import ru.mobileup.modulegraph.gradle.tasks.ParseModuleDependenciesTask


@Suppress("LeakingThis")
abstract class ModuleGraphExtension {
    abstract val featuresDir: Property<String>
    abstract val modulesJsonFile: Property<String>
    abstract val applicationId: Property<String>
    abstract val resultDotFile: Property<String>
    abstract val resultImageFile: Property<String>


    init {
        featuresDir.convention(DEFAULT_FEATURES_PATH)
        modulesJsonFile.convention(DEFAULT_RESULT_PATH)
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
        val extension =
            project.extensions.create(
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
            task.featuresDirectory.set(extension.featuresDir)
            task.outputJsonFile.set(extension.modulesJsonFile)
            task.applicationId.set(extension.applicationId)
        }

        task2.configure { task ->
            task.outputDotFile.set(extension.resultDotFile)
            task.moduleDependenciesJsonFile.set(task1.flatMap { it.outputJsonFile })
        }

        task3.configure { task ->
            task.outputFilePath.set(extension.resultImageFile)
            task.dotFilePath.set(task2.flatMap { it.outputDotFile })
        }
    }

    companion object {
        private const val PARSE_MODULE_DEPENDENCIES_TASK_NAME = "parseModuleDependencies"
        private const val GENERATE_DOT_FILE_TASK_NAME = "generateDotFile"
        private const val GENERATE_IMAGE_FILE_TASK_NAME = "generateModuleGraph"
        private const val EXTENSION_NAME = "moduleGraph"
    }
}