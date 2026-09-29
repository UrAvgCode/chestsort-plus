plugins {
    alias(libs.plugins.paperweight.userdev)
    alias(libs.plugins.resource.factory)
    alias(libs.plugins.run.paper)
}

group = "com.uravgcode"
version = "0.5.0"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper.api)
}

paperPluginYaml {
    main = "com.uravgcode.chestsortplus.ChestSortPlus"
    bootstrapper = "com.uravgcode.chestsortplus.ChestSortPlusBootstrap"
    foliaSupported = true
    apiVersion = "26.1"

    description = "a modern lightweight chestsort plugin"
    website = "https://uravgcode.com"
    authors.add("UrAvgCode")
}

runPaper {
    folia.registerTask()
}

val templateProperties = mapOf(
    "name" to rootProject.name,
    "version" to project.version,
)

val generateTemplates = tasks.register<Copy>("generateTemplates") {
    from(layout.projectDirectory.dir("src/main/templates"))
    into(layout.buildDirectory.dir("generated/sources/templates"))
    expand(templateProperties)
}

sourceSets {
    main {
        java.srcDir(generateTemplates)
    }
}

tasks {
    processResources {
        filteringCharset = "UTF-8"
        filesMatching("config.yml") {
            expand(templateProperties)
        }
    }

    runServer {
        minecraftVersion("26.3")
    }
}
