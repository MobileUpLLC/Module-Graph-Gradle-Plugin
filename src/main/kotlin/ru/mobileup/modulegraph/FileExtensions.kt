package ru.mobileup.modulegraph

import java.io.File
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