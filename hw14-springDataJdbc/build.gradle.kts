dependencies {
    implementation("ch.qos.logback:logback-classic")
    implementation("com.google.code.gson:gson")
    implementation ("org.flywaydb:flyway-core")
    implementation("org.postgresql:postgresql")
    implementation ("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")

    runtimeOnly("org.flywaydb:flyway-database-postgresql")
}