import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    kotlin("multiplatform") version "1.5.10"
    kotlin("kapt") version "1.5.10"
    id("org.jetbrains.compose") version "0.5.0-build228"
    id("com.github.ben-manes.versions") version "0.39.0"
    id("com.diffplug.spotless") version "5.14.1"
}
buildscript {
    repositories {
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        mavenLocal()
    }
    dependencies {
        classpath("io.arrow-kt:arrow-optics-gradle-plugin:1.5.10-SNAPSHOT")
    }
}
apply(plugin = "io.arrow-kt.optics")

group = "com.skaggsm"
version = "0.1.0"

val implementation: Configuration by configurations.creating
implementation.extendsFrom(configurations.commonMainImplementation.get())

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://maven.skaggsm.com")
    mavenLocal()
}

kotlin {
    jvm {
        val main by compilations.getting {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    js(IR) {
        browser()
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation(compose.runtime)
                implementation("io.arrow-kt:arrow-optics:1.0.0-SNAPSHOT")
                implementation("com.skaggsm:jetpack-compose-optics:0.1.2")
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("io.arrow-kt:arrow-optics-gradle-plugin:1.5.10-SNAPSHOT")
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(compose.web.core)
                implementation(compose.web.widgets)
            }
        }
    }
}

compose.desktop {
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
