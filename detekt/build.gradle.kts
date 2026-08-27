import dev.detekt.gradle.Detekt
import dev.detekt.gradle.DetektCreateBaselineTask
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.withType

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    // detekt
    alias(libs.plugins.ktlint) apply false
    //alias(libs.plugins.detekt.plugin) apply false
    id("dev.detekt") version "2.0.0-alpha.6" apply false
    //alias(libs.plugins.detekt.plugin) apply false
}










