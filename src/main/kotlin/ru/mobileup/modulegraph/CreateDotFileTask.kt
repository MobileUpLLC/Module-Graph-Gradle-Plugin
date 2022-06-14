package ru.mobileup.modulegraph

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardOpenOption

abstract class CreateDotFileTask : DefaultTask() {

    private val dotFileStartString = "digraph {\n"
    private val dotFileEndString = "}\n"
    private fun getDependencyString(module: Module, dependencyModule: DependencyModule) =
        "$module -> $dependencyModule\n"

    private fun getModuleString(module: Module) = "${module.id}\n"

    @InputFile
    val moduleDependenciesJsonFile: Property<String> = project.objects.property(String::class.java)

    @OutputFile
    val outputDotFile: Property<String> = project.objects.property(String::class.java)

    @TaskAction
    fun run() {
        val modulesFile = moduleDependenciesJsonFile.get().getFileFromProjectRelativePath(project)
        val outputDotFile = outputDotFile.get().getFileFromProjectRelativePath(project)
        val modules = prepareInput(modulesFile)

        writeDotFile(outputDotFile, modules)
    }

    private fun writeDotFile(
        outputDotFile: File,
        modules: Set<Module>
    ) {
        prepareDotFileToStart(outputDotFile)

        modules.forEach { module ->
            addModuleToDot(outputDotFile, module)
        }

        modules.forEach { module ->
            addModuleDependenciesToDot(outputDotFile, module)
        }

        prepareDotFileToEnd(outputDotFile)
    }

    private fun prepareDotFileToStart(dotFile: File) {
        val path = dotFile.toPath()
        path.createPathIfNotExist()
        Files.writeString(path, dotFileStartString)
    }

    private fun prepareDotFileToEnd(dotFile: File) {
        Files.writeString(dotFile.toPath(), dotFileEndString, StandardOpenOption.APPEND)
    }

    private fun addModuleToDot(dotFile: File, module: Module) {
        val string = getModuleString(module)
        Files.writeString(dotFile.toPath(), string, StandardOpenOption.APPEND)
    }

    private fun addModuleDependenciesToDot(dotFile: File, module: Module) {
        module.dependency.forEach { dependencyModule ->
            addModuleDependencyToDot(dotFile, module, dependencyModule)
        }
    }

    private fun addModuleDependencyToDot(
        dotFile: File,
        module: Module,
        dependencyModule: DependencyModule
    ) {
        val string = getDependencyString(module, dependencyModule)
        Files.writeString(
            dotFile.toPath(),
            string,
            StandardOpenOption.APPEND
        )
    }

    private fun prepareInput(file: File): Set<Module> {
        val json = Files.readString(file.toPath())
        return Json.decodeFromString(json)
    }
}