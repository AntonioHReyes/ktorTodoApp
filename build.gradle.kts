val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val ktorm_version: String by project
val appengine_version: String by project
val appengine_plugin_version: String by project

buildscript {
    repositories {
        jcenter()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.21")
        classpath("com.google.cloud.tools:appengine-gradle-plugin:2.2.0")
    }
}

apply(plugin="war")
apply(plugin="com.google.cloud.tools.appengine")

configure<com.google.cloud.tools.gradle.appengine.standard.AppEngineStandardExtension> {
    deploy {
        projectId = "ktorapps"
        version = "1"
        stopPreviousVersion = true // etc
    }
}

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
    jcenter()
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-server-servlet:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")

    //Gson negotiation
    implementation("io.ktor:ktor-gson:$ktor_version")
    //Databases
    implementation("mysql:mysql-connector-java:8.0.11")
    implementation("org.ktorm:ktorm-support-mysql:${ktorm_version}")
    implementation("org.ktorm:ktorm-core:${ktorm_version}")

    //AppEngine
    compileOnly("com.google.appengine:appengine:+")
    compileOnly("javax.servlet:javax.servlet-api:3.1.0")
}