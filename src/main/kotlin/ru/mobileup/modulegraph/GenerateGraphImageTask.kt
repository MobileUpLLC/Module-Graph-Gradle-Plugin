package ru.mobileup.modulegraph

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class GenerateGraphImageTask : DefaultTask() {

    @InputFile
    val inputFile: RegularFileProperty = project.objects.fileProperty()

    @OutputFile
    val outputFile: RegularFileProperty = project.objects.fileProperty()

    @TaskAction
    fun run() {
        val file = inputFile.get().asFile
        val outputF = outputFile.get().asFile

        val dotCommand = listOf("dot", "-Tsvg", file.path, "-o", outputF.path)
        ProcessBuilder(dotCommand)
            .directory(file.parentFile)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()
    }

    private fun createCommand(file: File): String{
        return "dot -Tsvg ${file.path}"
    }
}