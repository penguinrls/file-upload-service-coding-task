package com.file.upload;


import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * To be used for tests of which require access to an instance of an Azure SQL Database. On initialisation of the application context a test container
 * will be started.<p/>
 *
 * <b>Note</b> this container will be shared between all tests of which use this annotation without any local changes due to the cached context.<p/>
 *
 * The container will be shut down when the JVM exits as managed by the TestContainer framework (Ryuk).<p/>
 */
@Retention(RetentionPolicy.RUNTIME)
@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = DockerAzureSqlContextInitializer.class)
public @interface DataTestContainerComponentTest {
}
