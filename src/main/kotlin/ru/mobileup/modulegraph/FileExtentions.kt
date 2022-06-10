package ru.mobileup.modulegraph

import java.nio.file.Files
import java.nio.file.Path

fun Path.createPathIfNotExist() {
    if (Files.notExists(this.parent)) {
        Files.createDirectories(this.parent)
    }
}