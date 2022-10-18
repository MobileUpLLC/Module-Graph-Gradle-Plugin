package ru.mobileup.modulegraph.gradle.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

abstract class GraphCycleDetectTask : DefaultTask() {

    @InputFile
    val inputDotFile: RegularFileProperty = project.objects.fileProperty()

    @get: Input
    @set: Option(
        option = "ignore",
        description = "Ignoring cycle and don't throw an exception if it is found"
    )
    var ignoreCycle: Boolean = false

    @TaskAction
    fun run() {
        val graph = inputDotFile.get().asFile //.importGraphByDot()
//        checkCycles(graph)
    }

    @Throws(IllegalStateException::class)
//    private fun checkCycles(graph: Graph<String, NamelessEdge>) {
//        val cycleDetector: CycleDetector<String, NamelessEdge> = CycleDetector(graph)
//        if (cycleDetector.detectCycles()) {
//            val cycles = cycleDetector.findCycles()
//            val errorMessage = getErrorMessage(cycles)
//            if (!ignoreCycle) throw IllegalStateException(errorMessage)
//            else println(errorMessage)
//        }
//    }

    private fun getErrorMessage(cycles: Set<String>): String {
        var errorMessage = "There are 1 or more cycles in the Dependency Graph \n"
        cycles.forEach {
            errorMessage += "Cycle is detected at: $it\n"
        }
        return errorMessage
    }
}