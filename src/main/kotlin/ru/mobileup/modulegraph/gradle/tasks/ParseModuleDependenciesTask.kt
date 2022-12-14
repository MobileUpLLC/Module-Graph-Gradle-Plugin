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
import ru.mobileup.modulegraph.*
import java.io.File
import java.nio.file.Files

abstract class ParseModuleDependenciesTask : DefaultTask() {

    @InputDirectory
    val featuresDirectory: DirectoryProperty = project.objects.directoryProperty()

    @Input
    val featuresPackage: Property<String> = project.objects.property(String::class.java)

    @OutputFile
    val outputJsonFile: RegularFileProperty = project.objects.fileProperty()

    private val jsonFormatter = Json {
        prettyPrint = true
    }

    @TaskAction
    fun run() {
        val files = featuresDirectory.get().asFile
        val modules = findPackageModuleDependencies(files)
        prepareOutput(modules)
    }

    private fun findPackageModuleDependencies(files: File): List<Module> {
        val modules = getPackageModules(files)
        modules.forEach { searchDependencies(it, modules) }
        return modules
    }

    private fun prepareOutput(modules: List<Module>) {
        val json = jsonFormatter.encodeToString(modules)
        val resultPath = outputJsonFile.get().asFile.toPath()
        resultPath.createPathIfNotExist()
        Files.writeString(resultPath, json)
    }

    private fun getPackageModules(dir: File): List<Module> {
        val list = mutableListOf<Module>()
        dir.getChildDirs()?.forEach { list.add(it.createModule()) }

        return list.sortedBy { it.id }
    }

    /**
     * Search dependencies to [modules] in [module]
     */
    private fun searchDependencies(module: Module, modules: List<Module>) {
        val file = File(module.getAbsolutePath())
        val tempList = mutableListOf<DependencyModule>()
        modules.forEach { other ->
            if (other.id != module.id) {
                val import = other.getImportString()
                val result = checkImports(import, file)
                if (result) tempList.add(other.toDependency())
            }
        }
        module.dependency.addAll(tempList.sortedBy { it.id })
    }

    private fun checkImports(import: String, dir: File): Boolean {
        return dir.processFilesFromFolder {
            searchImports(import, it)
        }
    }

    private fun searchImports(import: String, file: File): Boolean {
        file.useLines { strings ->
            strings.forEach {
                if (it.contains(import)) return true
            }
        }
        return false
    }

    private fun File.createModule() = Module(name, getFileRelativePath())
    private fun Module.getImportString() = "import ${featuresPackage.get()}.${id}"
    private fun File.getFileRelativePath() = "/${toRelativeString(featuresDirectory.get().asFile)}"
    private fun Module.getAbsolutePath() = featuresDirectory.get().asFile.absolutePath + path
}