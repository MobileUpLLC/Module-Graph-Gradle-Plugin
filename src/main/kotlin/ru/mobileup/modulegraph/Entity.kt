package ru.mobileup.modulegraph

import kotlinx.serialization.Serializable

@Serializable
data class Module(
    val id: String,
    val path: String,
    val dependency: MutableSet<DependencyModule> = mutableSetOf()
)

@Serializable
data class DependencyModule(
    val id: String,
    val path: String,
)

fun Module.toDependency(): DependencyModule{
    return DependencyModule(id, path)
}