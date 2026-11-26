plugins {
    alias(libs.plugins.paperweight.userdev) apply false
    alias(libs.plugins.resource.factory) apply false
    alias(libs.plugins.run.paper) apply false
}

allprojects {
    group = "com.uravgcode"
    version = "0.7.0"
}
