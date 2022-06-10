package ru.mobileup.modulegraph

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardOpenOption

abstract class CreateDotFileTask : DefaultTask() {

    @InputFile
    val inputFile: RegularFileProperty = project.objects.fileProperty()

    @OutputFile
    val outputFile: RegularFileProperty = project.objects.fileProperty()

    @TaskAction
    fun run() {
        val file = inputFile.get().asFile
        val outputF = outputFile.get().asFile
        val modules = prepareInput(file)

        prepareDotFileToStart(outputF)

        modules.forEach { module ->
            addModuleToDot(outputF, module)
        }

        modules.forEach { module ->
            addModuleDependenciesToDot(outputF, module)
        }

        prepareDotFileToEnd(outputF)
    }

    private fun prepareDotFileToStart(dotFile: File) {
        val path = dotFile.toPath()
        path.createPathIfNotExist()
        Files.writeString(path, "digraph {\n")
    }

    private fun prepareDotFileToEnd(dotFile: File) {
        Files.writeString(dotFile.toPath(), "}\n", StandardOpenOption.APPEND)
    }

    private fun addModuleToDot(dotFile: File, module: Module) {
        Files.writeString(dotFile.toPath(), "${module.id}\n", StandardOpenOption.APPEND)
    }

    private fun addModuleDependenciesToDot(dotFile: File, module: Module) {
        module.dependency.forEach { dependencyModule ->
            addModuleDependencyToDot(dotFile, module.id, dependencyModule.id)
        }
    }

    private fun addModuleDependencyToDot(dotFile: File, module: String, dependencyModule: String) {
        Files.writeString(
            dotFile.toPath(),
            "$module -> $dependencyModule\n",
            StandardOpenOption.APPEND
        )
    }

    private fun prepareInput(file: File): Set<Module> {
        val json = Files.readString(file.toPath())
        return Json.decodeFromString(json)
    }
}