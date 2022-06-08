package ru.mobileup.modulegraph

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.nio.file.Files
import java.util.*

abstract class ParseModuleTask : DefaultTask() {

    @InputDirectory
    val inputDirectory: DirectoryProperty = project.objects.directoryProperty()

    @OutputFile
    val outputFile: RegularFileProperty = project.objects.fileProperty()

    @TaskAction
    fun run() {
        val files = inputDirectory.get().asFile

        val modules = getPackageModules(files)
        modules.forEach {
            searchDependencies(it, modules)
        }

        modules.forEach{
            println(it.id)
            it.dependency.forEach { module ->
                println("-> ${module.id}")
            }
            println()
        }
    }

    private fun getPackageModules(dir: File): Set<Module> {
        val set = mutableSetOf<Module>()
        dir.listFiles()?.forEach { file ->
            if (file.isDirectory) set.add(Module(file.name, file.path))
        }
        return set
    }

    /**
     * Search dependencies to [modules] in [module]
     */
    private fun searchDependencies(module: Module, modules: Set<Module>) {
        val file = File(module.path)
        modules.forEach { other ->
            if (other.id != module.id) {
                val result = checkImports(other.id, file)
                if (result) module.dependency.add(other)
            }
        }
    }

    private fun checkImports(import: String, dir: File): Boolean {
        return processFilesFromFolder(dir) {
            searchImports(import, it)
        }
    }

    private fun searchImports(import: String, file: File): Boolean {
        val strings = Files.readAllLines(file.toPath())
        val filteredStrings = strings.filter { it.contains(import) }
        return filteredStrings.isNotEmpty()
    }

    private fun processFilesFromFolder(
        folder: File,
        actionOnFile: (file: File) -> Boolean
    ): Boolean {
        val folderEntries = folder.listFiles()
        var interrupt = false
        folderEntries?.forEach { entry ->
            if (interrupt) return true
            if (entry.isDirectory) {
                interrupt = processFilesFromFolder(entry, actionOnFile)
            } else {
                return actionOnFile.invoke(entry)
            }
        }
        return false
    }

    data class Module(
        val id: String,
        val path: String,
        val dependency: MutableSet<Module> = mutableSetOf()
    )
}