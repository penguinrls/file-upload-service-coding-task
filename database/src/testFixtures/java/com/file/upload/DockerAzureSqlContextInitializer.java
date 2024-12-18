package com.file.upload;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.utility.DockerImageName;

public class DockerAzureSqlContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private final String DOCKER_IMAGE = "mcr.microsoft.com/azure-sql-edge:latest";

    private final MSSQLServerContainer<?> sqlServer = new MSSQLServerContainer<>(
            DockerImageName.parse(DOCKER_IMAGE)
                    .asCompatibleSubstituteFor("mcr.microsoft.com/mssql/server"))
                    .withInitScript("init-sql-server.sql");

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        sqlServer.start();

        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                applicationContext,
                "spring.datasource.url=" + sqlServer.getJdbcUrl(),
                "spring.datasource.driverClassName=" + sqlServer.getDriverClassName(),
                "spring.datasource.username=" + sqlServer.getUsername(),
                "spring.datasource.password=" + sqlServer.getPassword(),
                "spring.liquibase.url=" + sqlServer.getJdbcUrl(),
                "spring.liquibase.user=" + sqlServer.getUsername(),
                "spring.liquibase.password=" + sqlServer.getPassword()
        );
    }
}
