plugins {
    alias(libs.plugins.paperweight.userdev)
    alias(libs.plugins.resource.factory)
    alias(libs.plugins.run.paper)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper.api)
    implementation(project(":api"))
}

paperPluginYaml {
    main = "com.uravgcode.chestsortplus.ChestSortPlus"
    bootstrapper = "com.uravgcode.chestsortplus.ChestSortPlusBootstrap"
    foliaSupported = true
    apiVersion = "26.1"

    name = rootProject.name
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
    inputs.properties(templateProperties)
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

    jar {
        archiveBaseName.set(rootProject.name)
        dependsOn(":api:classes")
        from(project(":api").sourceSets.main.get().output)
    }

    runServer {
        minecraftVersion(libs.versions.minecraft.get())
        jvmArgs("-Dcom.mojang.eula.agree=true")
    }
}
