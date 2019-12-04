import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

repositories {
    mavenCentral()
    jcenter()
    maven("https://dl.bintray.com/mockito/maven/")
}

val ktorVersion: String by project
val logbackVersion: String by project
val okHttpVersion: String by project
val jacksonVersion: String by project
val junitVersion: String by project

plugins {
    application
    id("com.github.johnrengelman.shadow")
    kotlin("jvm")
}

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-jackson:$ktorVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    runtimeOnly("ch.qos.logback:logback-classic:$logbackVersion")
    testImplementation("com.squareup.okhttp3:okhttp:$okHttpVersion")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
}

tasks {
    named<Test>("test") {
        useJUnitPlatform()
        testLogging {
            displayGranularity = 2
            events = setOf(STARTED, FAILED, PASSED, SKIPPED, STANDARD_ERROR, STANDARD_OUT)
            showStandardStreams
        }
    }
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "12"
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }
}