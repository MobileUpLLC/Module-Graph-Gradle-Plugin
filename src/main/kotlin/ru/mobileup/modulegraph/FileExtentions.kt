package ru.mobileup.modulegraph

import java.nio.file.Files
import java.nio.file.Path

fun Path.createPathIfNotExist() {
    println("CheckPath for : ${this.toAbsolutePath()}")
    if (Files.notExists(this.toAbsolutePath().parent)) {
        println("createDirectories for : ${this.toAbsolutePath().parent}")
        Files.createDirectories(this.toAbsolutePath().parent)
    }
}