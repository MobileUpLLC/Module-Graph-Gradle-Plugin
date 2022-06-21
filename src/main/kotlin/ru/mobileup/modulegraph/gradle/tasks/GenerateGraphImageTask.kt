package ru.mobileup.modulegraph.gradle.tasks

import com.mxgraph.layout.mxCircleLayout
import com.mxgraph.layout.mxIGraphLayout
import com.mxgraph.util.mxCellRenderer
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.jgrapht.Graph
import org.jgrapht.ext.JGraphXAdapter
import org.jgrapht.graph.DirectedPseudograph
import org.jgrapht.nio.dot.DOTImporter
import ru.mobileup.modulegraph.graph.NamelessEdge
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

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

    private fun exportGraphToPNG(graph: Graph<String, NamelessEdge>, outputFile: File) {
        val graphAdapter: JGraphXAdapter<String, NamelessEdge> = JGraphXAdapter(graph)
        val layout: mxIGraphLayout = mxCircleLayout(graphAdapter)
        layout.execute(graphAdapter.defaultParent)

        val image: BufferedImage = mxCellRenderer.createBufferedImage(
            graphAdapter,
            null,
            2.0,
            Color.WHITE,
            true,
            null
        )

        ImageIO.write(image, "PNG", outputFile)
    }

    private fun importGraphByDot(file: File): Graph<String, NamelessEdge> {
        val graph: Graph<String, NamelessEdge> = DirectedPseudograph(NamelessEdge::class.java)
        val importer = DOTImporter<String, NamelessEdge>()

        importer.setVertexFactory { id -> id }
        importer.importGraph(graph, file)

        return graph
    }
}