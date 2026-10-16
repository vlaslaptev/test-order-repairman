plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.16.0"
}

group = "com.vlaptev"
version = "1.1"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    testImplementation("junit:junit:4.13.2")
}

intellij {
    version.set("2021.3.3")
    plugins.set(listOf("com.intellij.java"))
}

tasks {
    withType<JavaCompile>().configureEach {
        options.release.set(11)
    }

    patchPluginXml {
        sinceBuild = "213.7172.25"
        untilBuild = "232.*"
    }
}
