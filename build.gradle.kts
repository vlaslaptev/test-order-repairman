plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.16.0"
}

group = "com.vlaptev"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("junit:junit:4.13.2")
}

intellij {
    version.set("2022.2")
    plugins.set(listOf("com.intellij.java"))
}
