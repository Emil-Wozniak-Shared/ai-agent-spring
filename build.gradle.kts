import com.github.gradle.node.npm.task.NpmTask

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.4"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.github.node-gradle.node") version "7.0.1"
}

group = "pl.ejdev"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

extra["springAiVersion"] = "1.0.1"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.ai:spring-ai-starter-vector-store-qdrant")
    implementation("org.springframework.ai:spring-ai-advisors-vector-store")
    implementation("org.springframework.ai:spring-ai-starter-model-openai")
    implementation("org.springframework.security:spring-security-crypto")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("com.fasterxml.jackson.core:jackson-core:2.20.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.20.0")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("io.qdrant:client:1.11.0")

    implementation("io.jsonwebtoken:jjwt-api:0.11.1")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.1")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.1")

    implementation("io.arrow-kt:arrow-core-jvm:2.1.2")
    runtimeOnly("io.arrow-kt:arrow-core:2.1.2")
    testImplementation("io.arrow-kt:arrow-test:0.10.4")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("com.ninja-squad:springmockk:4.0.2")
    testImplementation("org.htmlunit:htmlunit:4.15.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.ai:spring-ai-bom:${property("springAiVersion")}")
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    maxHeapSize = "1G"
    testLogging {
        events("passed")
    }
}

tasks.named<NpmTask>("npmInstall") {
    dependsOn("nodeSetup")
    args.set(listOf("install"))
    workingDir.set(file("src/fe"))
}

tasks.register<NpmTask>("buildReact") {
    description = "Build React application"
    dependsOn("npmInstall")
    args.set(listOf("run", "build"))
    workingDir.set(file("src/fe"))

    inputs.dir(file("src/fe/src"))
    inputs.file(file("src/fe/package.json"))
    inputs.file(file("src/fe/package-lock.json"))
    outputs.dir(file("src/fe/build"))
}

tasks.register<NpmTask>("startReact") {
    description = "Start React development server"
    dependsOn("npmInstall")
    args.set(listOf("start"))
    workingDir.set(file("src/fe"))
}

tasks.register<Copy>("copyReactBuild") {
    description = "Copy React build to static resources"
//    dependsOn("buildReact") // FIXME

    from(file("src/fe/build"))
    into(file("src/main/resources/static"))

    doFirst {
        delete(file("src/main/resources/static"))
    }
}

tasks.named("processResources") {
    dependsOn("copyReactBuild")
}

tasks.named("clean") {
    doLast {
        delete(file("src/fe/build"))
        delete(file("src/fe/node_modules"))
        delete(file("src/main/resources/static"))
    }
}
