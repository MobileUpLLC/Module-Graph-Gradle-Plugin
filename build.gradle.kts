buildscript {

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
    }
}

apply(plugin = "kotlin")
apply(plugin = "java-gradle-plugin")

repositories {
    google()
    mavenCentral()

}
plugins {
    `java-library`
    `java-gradle-plugin`
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.6.21")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("junit:junit:4.12")
}

gradlePlugin {
    plugins {
        register("module-graph") {
            id = "ru.mobileup.module-graph"
            implementationClass = "ru.mobileup.modulegraph.ModuleGraphPlugin"
        }
    }
}
