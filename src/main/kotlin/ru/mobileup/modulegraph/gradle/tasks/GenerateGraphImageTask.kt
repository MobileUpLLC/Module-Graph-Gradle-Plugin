package ru.mobileup.modulegraph.gradle.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import ru.mobileup.modulegraph.getFileFromProjectRelativePath
import java.io.File

abstract class GenerateGraphImageTask : DefaultTask() {

    private fun getDotCommand(dotFile: File, outputFile: File) =
        listOf("dot", "-Tsvg", dotFile.path, "-o", outputFile.path)

    @InputFile
    val dotFilePath: Property<String> = project.objects.property(String::class.java)

    @OutputFile
    val outputFilePath: Property<String> = project.objects.property(String::class.java)

    @TaskAction
    fun run() {
        val dotFile = dotFilePath.get().getFileFromProjectRelativePath(project)
        val outputFile = outputFilePath.get().getFileFromProjectRelativePath(project)
        val dotCommand = getDotCommand(dotFile, outputFile)

        ProcessBuilder(dotCommand)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()
    }
}