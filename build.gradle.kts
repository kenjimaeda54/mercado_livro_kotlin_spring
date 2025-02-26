plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.4.0"
	id("io.spring.dependency-management") version "1.1.6"

	//jpa
	kotlin("plugin.jpa") version "1.4.21"

	//jacoco
	jacoco
}

group = "com.mercadolivro"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	//database
	implementation ("org.springframework.boot:spring-boot-starter-data-jpa")
	runtimeOnly ("com.mysql:mysql-connector-j")
     //toda vez que alterar precisa sincronizar o gradlew
	//da ultima ve mostrou no canto superior um icone, mas tambem pode clicar no icone na barra de ferrametna

	//flyway --> migration
	//precisa dentro de resources ser db.migration para funcionar a criação tabela
	implementation("org.flywaydb:flyway-core:11.1.0")
	implementation("org.flywaydb:flyway-mysql:11.1.0")

	//validator
	implementation("org.springframework.boot:spring-boot-starter-validation")

	//docs
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")

	//securty
	implementation("org.springframework.boot:spring-boot-starter-security")

	//jwt
	implementation("io.jsonwebtoken:jjwt:0.12.6")

	//test
	testImplementation("io.mockk:mockk:1.13.14")
	testImplementation("org.springframework.security:spring-security-test:6.4.2")


	//vem com o inialiazer
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
