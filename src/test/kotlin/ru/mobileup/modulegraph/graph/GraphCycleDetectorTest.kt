package ru.mobileup.modulegraph.graph

import guru.nidi.graphviz.parse.Parser
import org.junit.Test

internal class GraphCycleDetectorTest {

    @Test
    fun detect0Cycles() {
        val graph = Parser().read(dot0Cycles)
        val detector = GraphCycleDetector()
        val list = graph.rootNodes()

        val cycles = detector.detect(list)

        assert(cycles.isEmpty())
    }

    @Test
    fun detect1Cycles() {
        val graph = Parser().read(dot1Cycle)
        val detector = GraphCycleDetector()
        val list = graph.rootNodes()

        val cycles = detector.detect(list)

        assert(cycles.size == 1)
    }

    @Test
    fun detect2Cycles() {
        val graph = Parser().read(dot2Cycles)
        val detector = GraphCycleDetector()
        val list = graph.rootNodes()

        val cycles = detector.detect(list)

        assert(cycles.size == 2)
    }

    @Test
    fun detect3Cycles() {
        val graph = Parser().read(dot3Cycles)
        val detector = GraphCycleDetector()
        val list = graph.rootNodes()

        val cycles = detector.detect(list)

        assert(cycles.size == 3)
    }

    @Test
    fun detect5Cycles() {
        val graph = Parser().read(dot5Cycles)
        val detector = GraphCycleDetector()
        val list = graph.rootNodes()

        val cycles = detector.detect(list)

        assert(cycles.size == 5)
    }
}


val init = """
            cycle
            feature1
            feature2
            feature3
            isolated
            isolated2
""".trimIndent()

val dot0Cycles = """
    digraph {
        $init
        cycle -> feature1
        feature1 -> feature2
    }

""".trimIndent()

val dot1Cycle = """
    digraph {
        $init
        cycle -> feature1
        feature1 -> cycle
    }

""".trimIndent()

val dot2Cycles = """
    digraph {
        $init
        cycle -> feature1
        feature1 -> cycle
        feature2 -> feature3
        feature3 -> feature2
    }
""".trimIndent()

val dot3Cycles = """
    digraph {
        $init
        cycle -> feature1
        feature1 -> cycle
        isolated -> isolated2
        isolated2 -> isolated
        cycle -> isolated
        isolated2 -> feature1
    }
""".trimIndent()

val dot5Cycles = """
    digraph {
        $init
        cycle -> feature1
        feature1 -> cycle
        isolated -> isolated2
        isolated2 -> isolated
        cycle -> isolated
        isolated2 -> feature1
        feature1 -> feature2
        feature2 -> feature3
        feature3 -> cycle
    }
""".trimIndent()