package ru.mobileup.modulegraph.gradle.tasks

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import ru.mobileup.modulegraph.Module
import ru.mobileup.modulegraph.createPathIfNotExist
import ru.mobileup.modulegraph.processFilesFromFolder
import ru.mobileup.modulegraph.toDependency
import java.io.File
import java.nio.file.Files

abstract class ParseModuleDependenciesTask : DefaultTask() {

    private fun getImportString(module: Module) = "import ${applicationId.get()}.${module.id}"

    @InputDirectory
    val featuresDirectory: DirectoryProperty = project.objects.directoryProperty()

    @Input
    val applicationId: Property<String> = project.objects.property(String::class.java)

    @OutputFile
    val outputJsonFile: RegularFileProperty = project.objects.fileProperty()

    @TaskAction
    fun run() {
        val files = featuresDirectory.get().asFile
        val modules = findPackageModuleDependencies(files)
        prepareOutput(modules)
    }

    private fun findPackageModuleDependencies(files: File): Set<Module> {
        val modules = getPackageModules(files)
        modules.forEach { searchDependencies(it, modules) }
        return modules
    }

    private fun prepareOutput(modules: Set<Module>) {
        val json = Json.encodeToString(modules)
        val resultPath = outputJsonFile.get().asFile.toPath()
        resultPath.createPathIfNotExist()
        Files.writeString(resultPath, json)
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
                val import = getImportString(other)
                val result = checkImports(import, file)
                if (result) module.dependency.add(other.toDependency())
            }
        }
    }

    private fun checkImports(import: String, dir: File): Boolean {
        return dir.processFilesFromFolder {
            searchImports(import, it)
        }
    }

    private fun searchImports(import: String, file: File): Boolean {
        val strings = Files.readAllLines(file.toPath())
        val filteredStrings = strings.filter { it.contains(import) }
        return filteredStrings.isNotEmpty()
    }
}