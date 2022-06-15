package ru.mobileup.modulegraph.gradle.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class GenerateGraphImageTask : DefaultTask() {

    private fun getDotCommand(dotFile: File, outputFile: File) =
        listOf("dot", "-Tsvg", dotFile.path, "-o", outputFile.path)

    @InputFile
    val dotFile: RegularFileProperty = project.objects.fileProperty()

    @OutputFile
    val outputFile: RegularFileProperty = project.objects.fileProperty()

    @TaskAction
    fun run() {
        val dotFile = dotFile.get().asFile
        val outputFile = outputFile.get().asFile
        val dotCommand = getDotCommand(dotFile, outputFile)

        ProcessBuilder(dotCommand)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()
    }
}