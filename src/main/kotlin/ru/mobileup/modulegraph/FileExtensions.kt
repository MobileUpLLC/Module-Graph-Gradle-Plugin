package ru.mobileup.modulegraph

import guru.nidi.graphviz.model.MutableGraph
import guru.nidi.graphviz.parse.Parser
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

fun Path.createPathIfNotExist() {
    if (Files.notExists(this.toAbsolutePath().parent)) {
        Files.createDirectories(this.toAbsolutePath().parent)
    }
}

fun File.processFilesFromFolder(actionOnFile: (file: File) -> Boolean): Boolean {
    listFiles()?.forEach { entry ->
        when {
            entry.isDirectory -> {
                val interrupt = entry.processFilesFromFolder(actionOnFile)
                if (interrupt) return true
            }
            else -> {
                val interrupt = actionOnFile.invoke(entry)
                if (interrupt) return true
            }
        }
    }
    return false
}

fun File.importGraphByDot(): MutableGraph {
    return try {
        Parser().read(this)
    } catch (exception: IOException) {
        throw IOException("Can't parse graph from file: ${this.path}", exception)
    }
}