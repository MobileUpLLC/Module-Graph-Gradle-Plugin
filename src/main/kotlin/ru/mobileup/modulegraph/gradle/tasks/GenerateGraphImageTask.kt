package ru.mobileup.modulegraph.gradle.tasks

import guru.nidi.graphviz.engine.Format
import guru.nidi.graphviz.engine.Graphviz
import guru.nidi.graphviz.model.MutableGraph
import guru.nidi.graphviz.parse.Parser
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.IOException

abstract class GenerateGraphImageTask : DefaultTask() {

    @InputFile
    val inputDotFile: RegularFileProperty = project.objects.fileProperty()

    @OutputFile
    val outputImageFile: RegularFileProperty = project.objects.fileProperty()

    @TaskAction
    fun run() {
        val graph = importGraphByDot(inputDotFile.get().asFile)
        exportGraphToPNG(graph, outputImageFile.get().asFile)
    }

    private fun exportGraphToPNG(graph: MutableGraph, outputFile: File) {
        Graphviz.fromGraph(graph).render(Format.PNG).toFile(outputFile)
    }

    private fun importGraphByDot(file: File): MutableGraph {
        return try {
            Parser().read(file)
        } catch (exception: IOException) {
            throw IOException("Can't parse graph from file: ${file.path}", exception)
        }
    }
}