package ru.mobileup.modulegraph.graph

import guru.nidi.graphviz.model.LinkSource
import java.util.*

class GraphCycleDetector {

    fun detect(nodes: Collection<LinkSource>): List<LinkedList<LinkSource>> {
        val cycles = mutableListOf<LinkedList<LinkSource>>()
        val tsc = TarjanSimpleCycles(nodes.toList())

        tsc.findSimpleCycles { cycles.add(LinkedList(it)) }

        return cycles
    }
}