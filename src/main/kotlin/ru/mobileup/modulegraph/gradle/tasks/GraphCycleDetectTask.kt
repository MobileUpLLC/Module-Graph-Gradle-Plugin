package ru.mobileup.modulegraph.gradle.tasks

import guru.nidi.graphviz.model.LinkSource
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import ru.mobileup.modulegraph.graph.GraphCycleChecker
import ru.mobileup.modulegraph.importGraphByDot
import java.util.*

abstract class GraphCycleDetectTask : DefaultTask() {

    @InputFile
    val inputDotFile: RegularFileProperty = project.objects.fileProperty()

    @get: Input
    @set: Option(
        option = "ignore",
        description = "Ignoring cycle and don't throw an exception if it is found"
    )
    var ignoreCycle: Boolean = false
    private val checker = GraphCycleChecker()


    @TaskAction
    fun run() {
        val graph = inputDotFile.get().asFile.importGraphByDot()
        val cycles = checker.check(graph.rootNodes())
        val message = getMessage(cycles)
        if (!ignoreCycle && cycles.size > 0) throw IllegalStateException(message)
        else println(message)
    }


    private fun getMessage(cycles: ArrayList<LinkedList<LinkSource>>): String {
        var errorMessage = "There are ${cycles.size} cycles in the Dependency Graph \n"

        cycles.forEach { path ->
            errorMessage += "Cycle path: "
            path.forEach { errorMessage += " <- ${it.name()}" }
            errorMessage += "\n"
        }
        return errorMessage
    }
}