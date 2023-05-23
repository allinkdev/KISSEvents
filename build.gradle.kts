plugins {
    id("java")
    id("maven-publish")
    id("me.champeau.jmh") version "0.7.1" // Benchmarking
}

group = "com.github.allinkdev"
version = "1.0.0"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral();
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}