pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven(url = "https://repo.papermc.io/repository/maven-public/")
    }
}

rootProject.name = "chestsort-plus"
include("api", "plugin")
