import com.github.gradle.node.npm.task.NpmTask

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.dependency.management)
    alias(libs.plugins.node.gradle)
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
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    implementation(libs.postgresql)
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.java.time)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.migration.core)
    implementation(libs.exposed.migration.jdbc)
    implementation(libs.jackson.core)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.jackson.dataformat.xml)
    implementation(libs.jjwt.api)
    implementation(libs.jjwt.impl)
    implementation(libs.jjwt.jackson)
    implementation(libs.qdrant.client)
    implementation(libs.kotlin.logging)
    implementation(libs.arrow.core.jvm)
    runtimeOnly(libs.arrow.core)
    testImplementation(libs.arrow.test)

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.springmockk)
    testImplementation(libs.htmlunit)
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
    workingDir.set(file("src/web"))
}

tasks.register<NpmTask>("buildReact") {
    description = "Build React application"
    dependsOn("npmInstall")
    args.set(listOf("run", "build"))
    workingDir.set(file("src/web"))

    inputs.dir(file("src/web/src"))
    inputs.file(file("src/web/package.json"))
    inputs.file(file("src/web/package-lock.json"))
    outputs.dir(file("src/web/build"))
}

tasks.register<NpmTask>("startReact") {
    description = "Start React development server"
    dependsOn("npmInstall")
    args.set(listOf("dev"))
    workingDir.set(file("src/web"))
}

tasks.register<Copy>("copyReactBuild") {
    description = "Copy React build to static resources"
//    dependsOn("buildReact") // FIXME

    from(file("src/web/build"))
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
        delete(file("src/web/build"))
        delete(file("src/web/node_modules"))
        delete(file("src/main/resources/static"))
    }
}
