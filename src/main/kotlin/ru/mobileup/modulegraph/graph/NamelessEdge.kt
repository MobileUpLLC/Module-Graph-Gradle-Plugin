package ru.mobileup.modulegraph.graph

import org.jgrapht.graph.DefaultEdge

class NamelessEdge : DefaultEdge() {
    override fun toString(): String {
        return ""
    }
}