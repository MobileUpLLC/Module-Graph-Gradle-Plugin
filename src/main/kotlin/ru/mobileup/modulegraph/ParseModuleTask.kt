package ru.mobileup.modulegraph

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.util.*

abstract class ParseModuleTask : DefaultTask() {

    @InputDirectory
    val inputDirectory: DirectoryProperty = project.objects.directoryProperty()

    @OutputFile
    val outputFile: RegularFileProperty = project.objects.fileProperty()

    @TaskAction
    fun run() {
        val files = inputDirectory.get().asFile
        val stack = Stack<String>()
        processFilesFromFolder(files){ file ->
            stack.add(file.path)
        }
        stack.forEach(::println)
        println(stack.size)
        val dir = outputFile.get().asFile
        dir.createNewFile()
        val writer = dir.writer()
        stack.forEach(writer::write)
        writer.close()
    }

    private fun processFilesFromFolder(folder: File, actionOnFile: (file: File) -> Unit) {
        val folderEntries = folder.listFiles()
        folderEntries?.forEach { entry ->
            if (entry.isDirectory) {
                processFilesFromFolder(entry, actionOnFile)
            } else {
                actionOnFile.invoke(entry)
            }
        }
    }
}