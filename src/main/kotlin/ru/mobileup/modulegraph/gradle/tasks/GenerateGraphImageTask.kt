package ru.mobileup.modulegraph.gradle.tasks

import guru.nidi.graphviz.engine.Graphviz
import guru.nidi.graphviz.engine.GraphvizJdkEngine
import guru.nidi.graphviz.engine.GraphvizV8Engine
import guru.nidi.graphviz.model.MutableGraph
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import ru.mobileup.modulegraph.getImageFileFormat
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
        exportGraphToImage(graph, outputImageFile.get().asFile)
    }

    private fun exportGraphToImage(graph: MutableGraph, outputFile: File) {
        val format = outputFile.getImageFileFormat()
        Graphviz.useEngine(GraphvizV8Engine(), GraphvizJdkEngine())
        Graphviz.fromGraph(graph).render(format).toFile(outputFile)
    }
}