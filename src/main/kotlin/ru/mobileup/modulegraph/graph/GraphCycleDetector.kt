package ru.mobileup.modulegraph.graph

import guru.nidi.graphviz.model.LinkSource
import java.util.*
import kotlin.collections.ArrayDeque

class GraphCycleDetector {

    fun detect(nodes: Collection<LinkSource>): ArrayList<LinkedList<LinkSource>> {
        val visited = mutableMapOf<LinkSource, NodeState>()
        val stack = ArrayDeque<LinkSource>()
        val paths = ArrayList<LinkedList<LinkSource>>()
        val cycleStack = ArrayDeque<LinkSource>()

        nodes.forEach {
            dfsSearch(it, stack, visited, paths, cycleStack)
            cycleStack.clear()
        }
        return paths
    }

    private fun dfsSearch(
        node: LinkSource,
        stack: ArrayDeque<LinkSource>,
        visited: MutableMap<LinkSource, NodeState>,
        paths: ArrayList<LinkedList<LinkSource>>,
        cycleStack: ArrayDeque<LinkSource>
    ) {
        cycleStack.add(node)

        when (visited[node]) {
            NodeState.NOT_VISITED, null -> {
                visited[node] = NodeState.NOW_VISITING
            }
            NodeState.VISITED -> {
                return
            }
            NodeState.NOW_VISITING -> {
                val path = getPath(cycleStack)
                if (path.isEmpty()) return
                paths.add(path)
                stack.removeLastOrNull()
                return
            }
        }

        stack.addAll(node.links().map { it.to().asLinkSource() })

        val newNode = stack.removeLastOrNull() ?: return
        dfsSearch(newNode, stack, visited, paths, cycleStack)
        visited[node] = NodeState.VISITED
    }

    private fun getPath(cycleStack: ArrayDeque<LinkSource>): LinkedList<LinkSource> {
        val path = LinkedList<LinkSource>()
        val stackLast = cycleStack.removeLastOrNull() ?: return path
        path.add(stackLast)

        do {
            val stackNode = cycleStack.removeLastOrNull() ?: return path
            path.add(stackNode)
        } while (stackNode != stackLast)

        return path
    }

    private enum class NodeState {
        NOT_VISITED, VISITED, NOW_VISITING
    }
}