plugins {
    java
    id("org.springframework.boot") version "4.1.0"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.openapi.generator") version "7.23.0"
}


group = "org.example"
version = "0.0.1-SNAPSHOT"
description = "ms-account-reservation"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-liquibase")
    compileOnly("org.projectlombok:lombok")
    implementation("org.mapstruct:mapstruct:1.6.3")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    runtimeOnly("org.postgresql:postgresql")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.3")

    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")
}


openApiGenerate {
    generatorName.set("spring") // язык генерируемого кода
    inputSpec.set("$rootDir/src/main/resources/ms-upgrade-api.yaml") // путь к вашему yaml-файлу
    outputDir.set("$buildDir/generated-sources/openapi") // директория для вывода сгенерированного кода
    apiPackage.set("com.example.api")
    invokerPackage.set("com.example.invoker")
    modelPackage.set("com.example.model")

    configOptions.set(mapOf(
        "interfaceOnly" to "true",         // Генерировать ТОЛЬКО интерфейсы контроллеров, без реализации
        "useSpringBoot3" to "true",        // Генерировать аннотации jakarta.* (для Spring Boot 3.x/4.x)
        "openApiNullable" to "false",      // Отключаем лишние обертки JsonNullable
        "skipDefaultInterface" to "true"   // Отключаем дефолтные пустые реализации методов в интерфейсе
    ))
}

// Автоматически подключаем сгенерированный код в исходники проекта, чтобы его видела IDEA
sourceSets {
    main {
        java {
            srcDir("$buildDir/generated-sources/openapi/src/main/java")
        }
    }
}

// Заставляем проект сначала генерировать код, а потом компилировать Java файлы
tasks.compileJava {
    dependsOn(tasks.openApiGenerate)
}

tasks.withType<Test> {
    useJUnitPlatform()
}





