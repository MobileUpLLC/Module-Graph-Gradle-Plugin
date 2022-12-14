package ru.mobileup.modulegraph.gradle.tasks

import guru.nidi.graphviz.model.LinkSource
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import ru.mobileup.modulegraph.graph.GraphCycleDetector
import ru.mobileup.modulegraph.importGraphByDot
import java.util.*

abstract class GraphCycleDetectTask : DefaultTask() {

    @InputFile
    val inputDotFile: RegularFileProperty = project.objects.fileProperty()

    @Input
    val minCyclesProperty: Property<Int> = project.objects.property(Int::class.java)

    @get:Input
    @set:Option(
        option = "minCycles",
        description = "Do not throw an exception if the number of cycles found is less than or equal to minCycle."
    )
    var minCyclesArg: String = ""

    private val detector = GraphCycleDetector()
    private val minCycles: Int
    get() = minCyclesArg.toIntOrNull() ?: minCyclesProperty.get() ?: 0

    @TaskAction
    fun run() {
        val graph = inputDotFile.get().asFile.importGraphByDot()
        val cycles = detector.detect(graph.rootNodes())
        val message = getMessage(cycles)
        if (minCycles < cycles.size) throw IllegalStateException(message)
        else println(message)
    }


    private fun getMessage(cycles: List<LinkedList<LinkSource>>): String {
        var errorMessage = "There are ${cycles.size} cycles in the Dependency Graph, minCycles = $minCycles \n"

        cycles.forEach { path ->
            errorMessage += "Cycle path: "
            errorMessage += path.getPathString()
            errorMessage += "\n"
        }
        return errorMessage
    }

    private fun LinkedList<LinkSource>.getPathString(): String {
        val separator = " <- "
        var result = ""
        forEach { result += separator + it.name().toString() }
        return result.replaceFirst(separator, "")
    }
}