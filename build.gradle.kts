import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

group = "dev.mpardo"
version = "0.0.1"

repositories {
    mavenCentral()
}

val koinVersion: String by project
val kotlinVersion: String by project

dependencies {
    implementation("io.insert-koin:koin-core:$koinVersion")
    
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    
    testImplementation(kotlin("test"))
}

tasks.test {
    minHeapSize = "2048m"
    maxHeapSize = "2048m"
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}