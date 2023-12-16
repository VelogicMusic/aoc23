plugins {
    kotlin("jvm") version "1.9.21"
    application
}

group = "de.simon-einzinger"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.jkcclemens:khttp:-SNAPSHOT")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}

tasks.getByName("run", JavaExec::class) {
    standardInput = System.`in`
}
