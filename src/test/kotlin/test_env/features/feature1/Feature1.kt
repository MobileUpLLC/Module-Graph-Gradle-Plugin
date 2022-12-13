package test_env.features.feature1

import test_env.features.cycle.Cycle
import test_env.features.feature2.Feature2

class Feature1 {
    val a = Feature2()
    val b = Cycle()
}