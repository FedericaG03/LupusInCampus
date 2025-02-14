// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    jacoco // Aggiunge il plugin per la copertura del codice
}