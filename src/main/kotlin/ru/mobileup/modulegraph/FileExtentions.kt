package ru.mobileup.modulegraph

import org.gradle.api.Project
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

fun Path.createPathIfNotExist() {
    if (Files.notExists(this.toAbsolutePath().parent)) {
        Files.createDirectories(this.toAbsolutePath().parent)
    }
}

fun String.getFileFromProjectRelativePath(project: Project): File {
    return File(project.projectDir.path + "/" + this)
}

fun File.processFilesFromFolder(actionOnFile: (file: File) -> Boolean): Boolean {
    val folderEntries = listFiles()
    var interrupt = false
    var hasImports: Boolean
    folderEntries?.forEach { entry ->
        when {
            interrupt -> return true
            entry.isDirectory -> interrupt = entry.processFilesFromFolder(actionOnFile)
            else -> {
                hasImports = actionOnFile.invoke(entry)
                if (hasImports) return true
            }
        }
    }
    return interrupt
}