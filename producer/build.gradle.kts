import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
//        maven {
//            url = uri("https://repo.spring.io/snapshot")
//        }
        mavenCentral()
        mavenLocal()
    }

    dependencies {
        classpath("gradle.plugin.org.springframework.cloud:spring-cloud-contract-gradle-plugin:3.0.3")
    }
}

plugins {
    id("java")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")

    id("org.springframework.cloud.contract") version "3.0.3"
    id("maven-publish")
}

group = "me.jerry.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
    mavenLocal()
}

extra["springCloudVersion"] = "2020.0.3"


dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

dependencies {
    // implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")

    testImplementation("org.springframework.cloud:spring-cloud-starter-contract-verifier")
    testImplementation("org.springframework.cloud:spring-cloud-contract-spec-kotlin")

    // for compatibility
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.jetbrains.kotlin:kotlin-scripting-compiler-embeddable")
}


contracts {

    testFramework.set(org.springframework.cloud.contract.verifier.config.TestFramework.JUNIT5)
    testMode.set(org.springframework.cloud.contract.verifier.config.TestMode.EXPLICIT)
    baseClassForTests.set("me.jerry.example.contract.producer.ProducerBase")

//    packageWithBaseClasses = 'com.example'
//    baseClassMappings {
//        baseClassMapping(".*intoxication.*", "com.example.intoxication.BeerIntoxicationBase")
//    }
}


tasks.withType<Delete> {
    doFirst {
        delete("~/.m2/repository/me/jerry/example/consumer")
    }
}

tasks {
    contractTest {
        useJUnitPlatform()
        systemProperty("spring.profiles.active", "gradle")
        testLogging {
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }
        afterSuite(KotlinClosure2({ desc: TestDescriptor, result: TestResult ->
            if (desc.parent == null) {
                if (result.testCount == 0L) {
                    throw IllegalStateException("No tests were found. Failing the build")
                } else {
                    println("Results: (${result.testCount} tests, ${result.successfulTestCount} successes, ${result.failedTestCount} failures, ${result.skippedTestCount} skipped)")
                }
            } else { /* Nothing to do here */
            }
        }))
    }
}

//contractTest {
//    useJUnitPlatform()
//    testLogging {
//        exceptionFormat = 'full'
//    }
//    afterSuite { desc, result ->
//        if (!desc.parent) {
//            println "Results: (${result.testCount} tests, ${result.successfulTestCount} successes, ${result.failedTestCount} failures, ${result.skippedTestCount} skipped)"
//            boolean skipTests = Boolean.parseBoolean(project.findProperty('SKIP_TESTS') ?: "false")
//            if (result.testCount == 0 && !skipTests) {
//                throw new IllegalStateException("No tests were found. Failing the build")
//            }
//        }
//    }
//}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}


publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifact(tasks.named("bootJar"))

            artifact(tasks.named("verifierStubsJar"))

            // https://github.com/spring-gradle-plugins/dependency-management-plugin/issues/273
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
        }
    }
}
