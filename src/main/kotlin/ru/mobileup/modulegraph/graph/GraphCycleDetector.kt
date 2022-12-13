package ru.mobileup.modulegraph.graph

import guru.nidi.graphviz.model.LinkSource
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.collections.HashMap

class GraphCycleDetector {

    fun detect(nodes: Collection<LinkSource>): List<LinkedList<LinkSource>> {
        val visited = mutableMapOf<LinkSource, NodeState>()
        val paths = ArrayList<LinkedList<LinkSource>>()
        val cycleStack = ArrayDeque<LinkSource>()

        nodes.forEach { node ->
            dfsSearch(node, visited, paths, cycleStack)
            cycleStack.clear()
            visited.forEach { entry ->
                if (entry.value == NodeState.NOW_VISITING) visited[entry.key] = NodeState.VISITED
            }
        }
        return paths.distinct()
    }


    private fun dfsSearch(
        currentNode: LinkSource,
        visitedMap: MutableMap<LinkSource, NodeState>,
        cyclePaths: ArrayList<LinkedList<LinkSource>>,
        cycleStack: ArrayDeque<LinkSource>
    ) {
        cycleStack.add(currentNode)

        when (visitedMap[currentNode]) {
            NodeState.NOT_VISITED, null -> {
                visitedMap[currentNode] = NodeState.NOW_VISITING
            }
            NodeState.VISITED -> {
                return
            }
            NodeState.NOW_VISITING -> {
                val path = getPath(cycleStack)
                if (path.isEmpty()) return
                cyclePaths.add(path)
                return
            }
        }

        val stack = ArrayDeque<LinkSource>()
        stack.addAll(currentNode.links().map { it.to().asLinkSource() })


        while (stack.isNotEmpty()) {
            val cycleStackCopy = ArrayDeque(cycleStack)
            val visitedMapCopy = HashMap(visitedMap)

            val newNode = stack.removeLastOrNull() ?: return
            dfsSearch(newNode, visitedMapCopy, cyclePaths, cycleStackCopy)
        }
    }

    private fun getPath(cycleStack: ArrayDeque<LinkSource>): LinkedList<LinkSource> {
        val path = LinkedList<LinkSource>()
        val stackLast = cycleStack.removeLastOrNull() ?: return path
        path.add(stackLast)

        do {
            val stackNode = cycleStack.removeLastOrNull() ?: stackLast
            path.add(stackNode)
        } while (stackNode != stackLast)

        return path
    }

    private enum class NodeState {
        NOT_VISITED, VISITED, NOW_VISITING
    }
}