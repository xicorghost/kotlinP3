// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    // KSP desde libs.versions.toml
    alias(libs.plugins.ksp) apply false

    // Hilt también debe ir como alias
    alias(libs.plugins.hilt) apply false
}