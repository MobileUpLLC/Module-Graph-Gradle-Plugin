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
    val folderEntries = listFiles()
    var interrupt = false
    folderEntries?.forEach { entry ->
        when {
            interrupt -> return true
            entry.isDirectory -> interrupt = entry.processFilesFromFolder(actionOnFile)
            else -> {
                val actionInterrupt = actionOnFile.invoke(entry)
                if (actionInterrupt) return true
            }
        }
    }
    return interrupt
}