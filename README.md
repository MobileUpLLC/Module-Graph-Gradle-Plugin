# Code Quality for Android
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

Gradle plugin for creating a dependency diagram graph of package-modules. It looks for imports in nested packages and creates a .dot file which is used to generate the svg image of the graph using GraphViz.

## Usage

1) Add dependency
```kotlin
plugins {
    id("ru.mobileup.modulegraph") version "1.0.0"
}
buildscript {
    repositories {
        mavenCental()
    }
}
```

2) Setup plugin
```kotlin
moduleGraph {
    featuresPackage = 'com.domain.package'
    featuresDirectory = project.file('src/main/kotlin/com/domain/package')
    outputDirectory = project.file('module_graph')
}
```

3) To generate dependencies graph Run `gradlew generateModuleGraph`.

4) To detekt dependency cycles run `gradlew detektCycle`

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
