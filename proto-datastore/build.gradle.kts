// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("com.google.protobuf") version "0.10.0" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "2.4.10" apply false
}

buildscript{
    dependencies {
        classpath("com.google.protobuf:protobuf-gradle-plugin:0.10.0")
    }
}