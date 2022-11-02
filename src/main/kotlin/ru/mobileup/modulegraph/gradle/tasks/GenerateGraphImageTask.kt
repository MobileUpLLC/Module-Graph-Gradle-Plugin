package ru.mobileup.modulegraph.gradle.tasks

import guru.nidi.graphviz.engine.Format
import guru.nidi.graphviz.engine.Graphviz
import guru.nidi.graphviz.model.MutableGraph
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import ru.mobileup.modulegraph.importGraphByDot
import java.io.File

abstract class GenerateGraphImageTask : DefaultTask() {

    @InputFile
    val inputDotFile: RegularFileProperty = project.objects.fileProperty()

    @OutputFile
    val outputImageFile: RegularFileProperty = project.objects.fileProperty()

    @TaskAction
    fun run() {
        val graph = inputDotFile.get().asFile.importGraphByDot()
        exportGraphToPNG(graph, outputImageFile.get().asFile)
    }

    private fun exportGraphToPNG(graph: MutableGraph, outputFile: File) {
        Graphviz.fromGraph(graph).render(Format.PNG).toFile(outputFile)
    }
}