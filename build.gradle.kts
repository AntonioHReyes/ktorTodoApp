val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val ktorm_version: String by project

plugins {
    application
    kotlin("jvm") version "1.5.21"
}

group = "todo.back.app.tonyinc.com"
version = "0.0.1"
application {
    mainClass.set("todo.back.app.tonyinc.com.ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")

    //Gson negotiation
    implementation("io.ktor:ktor-gson:$ktor_version")
    //Databases
    implementation("mysql:mysql-connector-java:8.0.11")
    implementation("org.ktorm:ktorm-support-mysql:${ktorm_version}")
    implementation("org.ktorm:ktorm-core:${ktorm_version}")
}