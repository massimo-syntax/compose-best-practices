import dev.detekt.gradle.Detekt
import dev.detekt.gradle.DetektCreateBaselineTask

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    // detekt
    //alias(libs.plugins.detekt.plugin) apply false
    id("dev.detekt") version "2.0.0-alpha.6" apply false
    // ktlint
    alias(libs.plugins.ktlint) apply false
}

// DETEKT CONFIGURATIONS
tasks.withType<Detekt>().configureEach {
    reports {
        html.required.set(true) // observe findings in your browser with structure and code snippets
        checkstyle.required.set(true) // checkstyle(xml) like format mainly for integrations like Jenkins
        sarif.required.set(true) // standardized SARIF format (https://sarifweb.azurewebsites.net/) to support integrations with GitHub Code Scanning
        markdown.required.set(true) // simple Markdown format
    }
}

// Kotlin DSL
tasks.withType<Detekt>().configureEach {
    jvmTarget = "1.8"
}
tasks.withType<DetektCreateBaselineTask>().configureEach {
    jvmTarget = "1.8"
}







