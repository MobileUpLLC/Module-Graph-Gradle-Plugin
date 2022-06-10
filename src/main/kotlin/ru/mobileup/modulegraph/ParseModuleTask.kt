package ru.mobileup.modulegraph

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.nio.file.Files

abstract class ParseModuleTask : DefaultTask() {

    @InputDirectory
    val inputDirectory: Property<String> = project.objects.property(String::class.java)

    @Input
    val applicationId: Property<String> = project.objects.property(String::class.java)

    @OutputFile
    val outputFile: Property<String> = project.objects.property(String::class.java)

    @TaskAction
    fun run() {
        val files = File(project.projectDir.path + "/" + inputDirectory.get())
        val modules = findPackageModuleDependencies(files)
        prepareOutput(modules)
    }

    private fun findPackageModuleDependencies(files: File): Set<Module> {
        val modules = getPackageModules(files)
        modules.forEach {
            println("SearchDependency for module - ${it.id}")
            searchDependencies(it, modules)
        }
        return modules
    }

    private fun prepareOutput(modules: Set<Module>) {
        val json = Json.encodeToString(modules)
        val resultPath = File(project.projectDir.path + "/" + outputFile.get()).toPath()
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
                val import = "import ${applicationId.get()}.${other.id}"
                println("Check Import: ${other.id} at ${module.id} ")
                val result = checkImports(import, file)
                if (result) module.dependency.add(other.toDependency())
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
        val result = filteredStrings.isNotEmpty()
        if(file.path.contains("app_theme")) println("AppThemeFound: Import - ${result}")

        return result
    }

    private fun processFilesFromFolder(
        folder: File,
        actionOnFile: (file: File) -> Boolean
    ): Boolean {
        val folderEntries = folder.listFiles()
        var interrupt = false
        var hasImports: Boolean
        folderEntries?.forEach { entry ->
            if(entry.path.contains("app_theme")) println("AppThemeFound: Entry - ${entry.path}")
            when {
                interrupt -> return true
                entry.isDirectory -> interrupt = processFilesFromFolder(entry, actionOnFile)
                else -> {
                    hasImports = actionOnFile.invoke(entry)
                    if(entry.path.contains("app_theme")) println("AppThemeFound: ${entry.path} has imports - ${hasImports}")
                    if (hasImports) return true
                }
            }
        }
        return interrupt
    }
}