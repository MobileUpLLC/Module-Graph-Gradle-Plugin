/*
 * (C) Copyright 2013-2021, by Nikolay Ognyanov and Contributors.
 *
 * JGraphT : a free Java graph-theory library
 *
 * See the CONTRIBUTORS.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0, or the
 * GNU Lesser General Public License v2.1 or later
 * which is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1-standalone.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR LGPL-2.1-or-later
 */
package ru.mobileup.modulegraph.graph

import guru.nidi.graphviz.model.LinkSource
import java.lang.IllegalArgumentException
import java.util.ArrayDeque
import java.util.ArrayList
import java.util.HashSet
import java.util.HashMap
import java.util.function.Consumer

/**
 * Find all simple cycles of a directed graph using the Tarjan's algorithm.
 *
 *
 *
 * See:<br></br>
 * R. Tarjan, Enumeration of the elementary circuits of a directed graph, SIAM J. Comput., 2 (1973),
 * pp. 211-216.
 *
 * @author Nikolay Ognyanov
 */
class TarjanSimpleCycles {

    var graph: List<LinkSource>? = null
    private var cycleConsumer: Consumer<List<LinkSource>>? = null
    private var marked: MutableSet<LinkSource>? = null
    private var markedStack: ArrayDeque<LinkSource>? = null
    private var pointStack: ArrayDeque<LinkSource>? = null
    private var vToI: MutableMap<LinkSource, Int>? = null
    private var removed: MutableMap<LinkSource, MutableSet<LinkSource>>? = null

    /**
     * Create a simple cycle finder with an unspecified graph.
     */
    constructor() {}

    /**
     * Create a simple cycle finder for the specified graph.
     *
     * @param graph - the DirectedGraph in which to find cycles.
     * @throws IllegalArgumentException if the graph argument is `
     * null`.
     */
    constructor(graph: List<LinkSource>?) {
        this.graph = graph
    }

    fun findSimpleCycles(consumer: Consumer<List<LinkSource>>) {
        requireNotNull(graph) { "Null graph." }
        initState(consumer)
        for (start in graph!!) {
            backtrack(start, start)
            while (!markedStack!!.isEmpty()) {
                marked!!.remove(markedStack!!.pop())
            }
        }
        clearState()
    }

    private fun backtrack(start: LinkSource, vertex: LinkSource): Boolean {
        var foundCycle = false
        pointStack!!.push(vertex)
        marked!!.add(vertex)
        markedStack!!.push(vertex)
        for (currentEdge in vertex.links()) {
            val currentVertex = currentEdge.to().asLinkSource()
            if (getRemoved(vertex).contains(currentVertex)) {
                continue
            }
            val comparison = toI(currentVertex)!!.compareTo(toI(start)!!)
            if (comparison < 0) {
                getRemoved(vertex).add(currentVertex)
            } else if (comparison == 0) {
                foundCycle = true
                val cycle: MutableList<LinkSource> = ArrayList()
                val it = pointStack!!.descendingIterator()
                var v: LinkSource
                while (it.hasNext()) {
                    v = it.next()
                    if (start == v) {
                        break
                    }
                }
                cycle.add(start)
                while (it.hasNext()) {
                    cycle.add(it.next())
                }
                cycleConsumer!!.accept(cycle)
            } else if (!marked!!.contains(currentVertex)) {
                val gotCycle = backtrack(start, currentVertex)
                foundCycle = foundCycle || gotCycle
            }
        }
        if (foundCycle) {
            while (markedStack!!.peek() != vertex) {
                marked!!.remove(markedStack!!.pop())
            }
            marked!!.remove(markedStack!!.pop())
        }
        pointStack!!.pop()
        return foundCycle
    }

    private fun initState(consumer: Consumer<List<LinkSource>>) {
        cycleConsumer = consumer
        marked = HashSet()
        markedStack = ArrayDeque()
        pointStack = ArrayDeque()
        vToI = HashMap()
        removed = HashMap()
        var index = 0
        for (v in graph!!) {
            (vToI as HashMap<LinkSource, Int>)[v] = index++
        }
    }

    private fun clearState() {
        cycleConsumer = null
        marked = null
        markedStack = null
        pointStack = null
        vToI = null
    }

    private fun toI(v: LinkSource): Int? {
        return vToI!![v]
    }

    private fun getRemoved(v: LinkSource): MutableSet<LinkSource> {
        // Removed sets typically not all
        // needed, so instantiate lazily.
        return removed!!.computeIfAbsent(v) { k: LinkSource? -> HashSet() }
    }
}