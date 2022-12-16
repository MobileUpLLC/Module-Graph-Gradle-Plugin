# Module Graph Gradle Plugin
[![Maven Central](https://img.shields.io/maven-central/v/ru.mobileup/module-graph)](https://repo1.maven.org/maven2/ru/mobileup/module-graph)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

Gradle plugin for creating a dependency diagram graph of package-modules. It looks for imports in nested packages and creates a .dot file which is used to generate the png image of the graph. You can also check the dependency graph for cycles. 

## Usage

1) Add dependency to root build.gradle
```kotlin
plugins {
    id("ru.mobileup.module-graph") version "1.3.1" apply false
}
buildscript {
    repositories {
        mavenCental()
    }
}
```

2) Setup plugin at features build.gradle
```kotlin
plugins {
    id("ru.mobileup.module-graph")
}

moduleGraph {
    featuresPackage = "com.domain.package"
    featuresDirectory = project.file("src/main/kotlin/com/domain/package")
    outputDirectory = project.file("module_graph")
}
```

3) To generate dependencies graph run `gradlew generateModuleGraph`.

4) To detect dependency cycles run `gradlew detectGraphCycles`

## License
```
MIT License

Copyright (c) 2022 MobileUp

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
