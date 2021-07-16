import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
    kotlin("kapt") version "1.5.10"
    id("org.jetbrains.compose") version "0.5.0-build228"
    id("com.github.ben-manes.versions") version "0.39.0"
    id("com.diffplug.spotless") version "5.14.1"
}

group = "com.skaggsm"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://maven.skaggsm.com")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("com.skaggsm:jetpack-compose-optics:0.1.2")
    implementation("io.arrow-kt:arrow-optics:1.0.0-SNAPSHOT")
    kapt("io.arrow-kt:arrow-meta:1.0.0-SNAPSHOT")
}

compose.desktop {
    application {
        mainClass = "com.skaggsm.compose.MainKt"
        nativeDistributions {
            targetFormats(Dmg, Msi, Deb)
        }
    }
}

tasks.withType<DependencyUpdatesTask> {
    gradleReleaseChannel = "current"
    rejectVersionIf {
        candidate.version.contains("""-M\d+""".toRegex())
    }
}

spotless {
    kotlin {
        ktlint("0.41.0")
    }
    kotlinGradle {
        ktlint("0.41.0")
    }
}
