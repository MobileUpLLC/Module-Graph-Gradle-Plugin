package ru.mobileup.modulegraph.graph

import org.jgrapht.graph.DefaultEdge

/**
 * [DefaultEdge.toString] uses [DefaultEdge.source] and [DefaultEdge.target] to generate an edge label on graph
 * [NamelessEdge] override [DefaultEdge.toString] to return empty string
 */
class NamelessEdge : DefaultEdge() {
    override fun toString(): String {
        return ""
    }
}