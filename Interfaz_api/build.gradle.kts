import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)

    // Estas implementaciones sigu8ientes son para el error "org.slf4j.impl.StaticLoggerBinder" --> Error en memoria
    implementation ("org.slf4j:slf4j-api:1.7.36")
    implementation ("org.slf4j:slf4j-simple:1.7.36")


    // IMPLEMETACION DE LA LIBRERIA DE KTOR
    implementation("io.ktor:ktor-client-core:2.3.5")
    implementation("io.ktor:ktor-client-cio:2.3.5") // Cliente HTTP basado en coroutines
    implementation("io.ktor:ktor-client-content-negotiation:2.3.5") // Soporte JSON automático
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.5") // Serializacion
    implementation("io.ktor:ktor-client-serialization:2.3.5")


}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Interfaz_api"
            packageVersion = "1.0.0"
        }
    }
}
