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

    @Test
    fun detectCycles7() {
        val graph = Parser().read(dotDifferentCycles)
        val detector = GraphCycleDetector()
        val list = graph.rootNodes()

        val cycles = detector.detect(list)

        assert(cycles.size == 7)
    }

    @Test
    fun detectOtherCycles7() {
        val graph = Parser().read(dotDifferentCycles2)
        val detector = GraphCycleDetector()
        val list = graph.rootNodes()

        val cycles = detector.detect(list)

        assert(cycles.size == 7)
    }

    @Test
    fun detectDifferentDotCycles() {
        val graph = Parser().read(dotDifferentCycles)
        val detector = GraphCycleDetector()
        val list = graph.rootNodes()
        val graph2 = Parser().read(dotDifferentCycles2)
        val list2 = graph2.rootNodes()

        val cycles = detector.detect(list)
        val cycles2 = detector.detect(list2)

        assert(cycles.size == cycles2.size)
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

val dotDifferentCycles = """
digraph {
followings
discover
author_flow
profile
photocard_flow
root
unsplash
favorites_flow
authorization
photos
collections
feed_flow
followings -> profile
discover -> photocard_flow
author_flow -> profile
author_flow -> photocard_flow
profile -> followings
profile -> author_flow
profile -> collections
photocard_flow -> author_flow
photocard_flow -> collections
root -> unsplash
root -> authorization
unsplash -> discover
unsplash -> profile
unsplash -> favorites_flow
unsplash -> authorization
unsplash -> feed_flow
favorites_flow -> photocard_flow
favorites_flow -> photos
photos -> profile
collections -> profile
collections -> photocard_flow
feed_flow -> photocard_flow
feed_flow -> photos
}
""".trimIndent()

val dotDifferentCycles2 = """
digraph {
authorization
author_flow
collections
discover
favorites_flow
feed_flow
followings
photocard_flow
photos
profile
root
unsplash
author_flow -> photocard_flow
author_flow -> profile
collections -> photocard_flow
collections -> profile
discover -> photocard_flow
favorites_flow -> photocard_flow
favorites_flow -> photos
feed_flow -> photocard_flow
feed_flow -> photos
followings -> profile
photocard_flow -> author_flow
photocard_flow -> collections
photos -> profile
profile -> author_flow
profile -> collections
profile -> followings
root -> authorization
root -> unsplash
unsplash -> authorization
unsplash -> discover
unsplash -> favorites_flow
unsplash -> feed_flow
unsplash -> profile
}
""".trimIndent()