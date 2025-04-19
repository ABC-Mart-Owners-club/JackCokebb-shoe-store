plugins {
    id("java")
}

group = "org.shoestore"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // Mockito
    testImplementation("org.mockito:mockito-core:5.12.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.12.0")
}

tasks.test {
    useJUnitPlatform()

    // 콘솔에 테스트 통과 여부 출력
    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
    }
}