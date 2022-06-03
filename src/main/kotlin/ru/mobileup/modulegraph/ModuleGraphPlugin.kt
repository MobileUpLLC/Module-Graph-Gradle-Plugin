package ru.mobileup.modulegraph

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import java.io.File


@Suppress("LeakingThis")
abstract class ModuleGraphExtension {
    abstract val featuresDir: DirectoryProperty
    abstract val resultFile: RegularFileProperty

    init {

        featuresDir.convention(featuresDir.dir(DEFAULT_FEATURES_PATH))
        resultFile.convention { File(DEFAULT_RESULT_PATH) }
    }

    companion object {
        private const val DEFAULT_RESULT_PATH = "gradle/dependency-graph/"
        private const val DEFAULT_FEATURES_PATH = "src/main/"
    }
}

class ModuleGraphPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val task = project.tasks.create("parseModules", ParseModuleTask::class.java)
        val extension =
            project.extensions.create("moduleGraphExtension", ModuleGraphExtension::class.java)
        task.inputDirectory.set(extension.featuresDir)
        task.outputFile.set(extension.resultFile)
    }
}
