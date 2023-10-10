import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import java.util.Properties

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.serialization") version ("1.9.0")
    id("com.github.gmazzo.buildconfig") version("3.1.0")
}

kotlin {
    jvmToolchain(17)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

group = "com.ap"
version = "1.0-SNAPSHOT"

val properties: Properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())

buildConfig {
    buildConfigField("String", "SUPABASE_ANON_KEY", "\"${properties.getProperty("SUPABASE_ANON_KEY")}\"")
    buildConfigField("String", "SUPABASE_URL", "\"${properties.getProperty("SUPABASE_URL")}\"")
    buildConfigField("String", "GOOGLE_MAPS_KEY", "\"${properties.getProperty("GOOGLE_MAPS_KEY")}\"")
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.compose.material3:material3-desktop:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("io.insert-koin:koin-core:3.4.3")
    implementation("io.insert-koin:koin-compose:1.0.4")
    implementation("io.github.jan-tennert.supabase:postgrest-kt:1.3.0")
    implementation("io.ktor:ktor-client-cio-jvm:2.3.4")

    implementation("org.slf4j:slf4j-nop:2.0.9")

    implementation("io.ktor:ktor-client-core:2.3.4")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.4")
    implementation("io.ktor:ktor-serialization-kotlinx-json:1.6.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "SS"
            packageVersion = "1.0.0"
        }
    }
}
