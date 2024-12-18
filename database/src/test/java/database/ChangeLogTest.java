package database;

import com.file.upload.DataJpaTestContainerTest;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@TestPropertySource(properties = {
        "spring.liquibase.enabled=false",
})
@DataJpaTestContainerTest
class ChangeLogTest {

    @Autowired
    protected DataSource dataSource;

    protected Liquibase liquibase;

    @BeforeEach
    void configureLiquibase() throws SQLException, DatabaseException {
        System.out.println("Configuring Liquibase...");
        Connection connection = dataSource.getConnection();
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        liquibase = new Liquibase("liquibase/db.changelog-master.xml", new ClassLoaderResourceAccessor(), database);
        System.out.println("Liquibase Configured");
    }

    @Test
    void testLiquibaseMigrations() throws Exception {
        // when
        System.out.println("Performing Liquibase Migration...");
        liquibase.update("");

        // then
        assertVetTable();


        System.out.println("Liquibase Migration Complete.");
    }

    private void assertVetTable() throws Exception {
        Table fileUploadHistoryTable = new Table(dataSource, "FILE_UPLOAD_HISTORY");
        assertThat(fileUploadHistoryTable.getColumnsNameList())
                .contains(
                        "REQUEST_ID",
                        "REQUEST_URI",
                        "REQUEST_TIME_STAMP",
                        "RESPONSE_CODE",
                        "REQUEST_IP_ADDRESS",
                        "REQUEST_COUNTRY_CODE",
                        "REQUEST_IP_PROVIDER",
                        "TIME_LAPSED_OF_REQUEST"
                );

        assertColumnsAreNotNullable("FILE_UPLOAD_HISTORY",
                of(
                        "REQUEST_ID"
                )
        );

        assertPrimaryKeyCreated("FILE_UPLOAD_HISTORY", "PK_FILE_UPLOAD_HISTORY_ID", "REQUEST_ID");
        assertIndexCreated("FILE_UPLOAD_HISTORY", "PK_FILE_UPLOAD_HISTORY_ID", true);
    }

    private void assertColumnsAreNotNullable(String tableName, List<String> notNullColumns) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            ResultSet columns = connection.getMetaData().getColumns(null, null, tableName, null);
            Set<String> notNullColumnsExpected = new LinkedHashSet<>();
            Set<String> validColumns = new LinkedHashSet<>();

            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                if (notNullColumns.contains(columns.getString("COLUMN_NAME"))) {
                    assertThat(columns.getString("IS_NULLABLE"))
                            .describedAs("Expected column '%s.%s' to be NOT NULLABLE, but was NULLABLE", tableName, columnName)
                            .isEqualTo("NO");
                    notNullColumnsExpected.add(columnName);
                }
                validColumns.add(columnName);
            }

            Set<String> validNotNullColumns = new HashSet<>(notNullColumns);
            validNotNullColumns.removeAll(validColumns);

            if (!validNotNullColumns.isEmpty()) {
                fail("Unable to find Columns '" + String.join(",", validNotNullColumns) + "' in table '" + tableName + "'");
            }
            assertThat(notNullColumns).containsExactlyInAnyOrderElementsOf(notNullColumnsExpected);
        }
    }

    private void assertPrimaryKeyCreated(String tableName, String primaryKeyName, String primaryKeyColumnName) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            ResultSet keys = connection.getMetaData().getPrimaryKeys(null, null, tableName);
            List<List<String>> validKeys = new ArrayList<>();

            while (keys.next()) {
                validKeys.add(of(keys.getString("PK_NAME"), keys.getString("COLUMN_NAME")));
            }

            assertThat(validKeys).contains(of(primaryKeyName, primaryKeyColumnName));
        }
    }

    private void assertIndexCreated(String tableName, String indexName, boolean unique) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            ResultSet index = connection.getMetaData().getIndexInfo(null, null, tableName, unique, true);
            List<String> indexes = new ArrayList<>();

            while (index.next()) {
                indexes.add(index.getString("INDEX_NAME"));
            }

            if (!indexes.contains(indexName)) {
                fail("Unable to find Index '" + indexName + "' for table '" + tableName + "'");
            }
        }
    }
}
