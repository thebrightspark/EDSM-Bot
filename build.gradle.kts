import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.2.6.RELEASE"
	id("io.spring.dependency-management") version "1.0.9.RELEASE"
	kotlin("jvm") version "1.3.72"
	kotlin("plugin.spring") version "1.3.72"
}

group = "brightpspark"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenLocal()
	mavenCentral()
	jcenter()
}

dependencies {
	implementation("org.springframework.boot", "spring-boot-starter-web") {
		exclude(module = "spring-boot-starter-tomcat")
	}
	implementation("org.jetbrains.kotlin", "kotlin-reflect")
	implementation("org.jetbrains.kotlin", "kotlin-stdlib-jdk8")
	implementation("com.fasterxml.jackson.module", "jackson-module-kotlin", "2.10.3")
	implementation("net.dv8tion", "JDA", "4.1.1_101") {
		exclude(module = "opus-java")
	}
	implementation("com.github.bvanseg", "kotlincommons", "2.2.3-DEV")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}
