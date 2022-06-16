package ru.mobileup.modulegraph.gradle.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class GenerateGraphImageTask : DefaultTask() {

    private fun getDotCommand(inputDotFile: File, outputImageFile: File) =
        listOf("dot", "-Tsvg", inputDotFile.path, "-o", outputImageFile.path)

    @InputFile
    val inputDotFile: RegularFileProperty = project.objects.fileProperty()

    @OutputFile
    val outputImageFile: RegularFileProperty = project.objects.fileProperty()

    @TaskAction
    fun run() {
        val dotCommand = getDotCommand(inputDotFile.get().asFile, outputImageFile.get().asFile)

        ProcessBuilder(dotCommand)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()
    }
}