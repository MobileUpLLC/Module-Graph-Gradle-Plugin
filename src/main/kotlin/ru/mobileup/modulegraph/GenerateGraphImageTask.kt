package ru.mobileup.modulegraph

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class GenerateGraphImageTask : DefaultTask() {

    @InputFile
    val inputFile: Property<String> = project.objects.property(String::class.java)

    @OutputFile
    val outputFile: Property<String> = project.objects.property(String::class.java)

    @TaskAction
    fun run() {
        val file = File(project.projectDir.path + "/" + inputFile.get())
        val outputF = File(project.projectDir.path + "/" + outputFile.get())
        println("InputFile : ${file.absolutePath}")
        println("OutputFile : ${outputF.absolutePath}")

        val dotCommand = listOf("dot", "-Tsvg", file.path, "-o", outputF.path)
        ProcessBuilder(dotCommand)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()
    }
}