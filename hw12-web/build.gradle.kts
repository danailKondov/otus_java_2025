dependencies {
    implementation("ch.qos.logback:logback-classic")
    implementation("com.google.code.gson:gson")

    implementation("org.eclipse.jetty.ee10:jetty-ee10-servlet")
    implementation("org.eclipse.jetty:jetty-server")
    implementation("org.eclipse.jetty.ee10:jetty-ee10-webapp")
    implementation("org.eclipse.jetty:jetty-security")
    implementation("org.eclipse.jetty:jetty-http")
    implementation("org.eclipse.jetty:jetty-io")
    implementation("org.eclipse.jetty:jetty-util")
    implementation("org.freemarker:freemarker")

    implementation("org.flywaydb:flyway-core")
    implementation("org.postgresql:postgresql")
    implementation("org.flywaydb:flyway-core")
    implementation("com.zaxxer:HikariCP")
    implementation("org.hibernate.orm:hibernate-core")
    runtimeOnly("org.flywaydb:flyway-database-postgresql")

    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
}