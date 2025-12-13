plugins {
    alias(libs.plugins.paperweight.userdev)
    alias(libs.plugins.resource.factory)
    alias(libs.plugins.run.paper)
}

group = "com.uravgcode"
version = "0.1.0"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper.api)
}

paperPluginYaml {
    main = "com.uravgcode.chestsortplus.ChestSortPlus"
    bootstrapper = "com.uravgcode.chestsortplus.ChestSortPlusBootstrap"
    foliaSupported = true
    apiVersion = "1.21.11"

    name = "chestsort-plus"
    description = "a modern lightweight chestsort plugin"
    website = "https://uravgcode.com"
    authors.add("UrAvgCode")
}

runPaper {
    folia.registerTask()
}

tasks {
    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.release.set(21)
    }

    runServer {
        minecraftVersion("1.21.11")
    }
}
