package hu.agfcodeworks.operangel.application.mapper;

import hu.agfcodeworks.operangel.application.settings.DbEngine;
import hu.agfcodeworks.operangel.application.settings.DbSettings;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

public class DbSettingsProperiesMapperTest {

    private final DbSettingsProperiesMapper dbSettingsProperiesMapper = new DbSettingsProperiesMapper();

    @Test
    void propertiesToDtoTest() {
        var properties = makeProperties();
        var dbSettings = dbSettingsProperiesMapper.propertiesToDto(properties);

        assertThat(dbSettings.getDbEngine().getName()).isEqualTo("postgresql");
        assertThat(dbSettings.getHost()).isEqualTo("localhost");
        assertThat(dbSettings.getPort()).isEqualTo(5432);
        assertThat(dbSettings.getName()).isEqualTo("test-db");
        assertThat(dbSettings.getUsername()).isEqualTo("user");
        assertThat(dbSettings.getPassword()).isEqualTo("p@s5w0rd");
    }

    @Test
    void dtoToPropertiesTest() {
        var dbSettings = makeDto();
        var properties = dbSettingsProperiesMapper.dtoToProperties(dbSettings);

        assertThat(properties.getProperty("db.engine")).isEqualTo("postgresql");
        assertThat(properties.getProperty("db.host")).isEqualTo("localhost");
        assertThat(properties.getProperty("db.port")).isEqualTo("5432");
        assertThat(properties.getProperty("db.name")).isEqualTo("test-db");
        assertThat(properties.getProperty("db.auth.username")).isEqualTo("user");
        assertThat(properties.getProperty("db.auth.password")).isEqualTo("p@s5w0rd");
    }

    private Properties makeProperties() {
        var properties = new Properties();

        properties.put("db.engine", "postgresql");
        properties.put("db.host", "localhost");
        properties.put("db.port", "5432");
        properties.put("db.name", "test-db");
        properties.put("db.auth.username", "user");
        properties.put("db.auth.password", "p@s5w0rd");

        return properties;
    }

    private DbSettings makeDto() {
        return DbSettings.builder()
                .withDbEngine(DbEngine.byName("postgresql"))
                .withHost("localhost")
                .withPort(5432)
                .withName("test-db")
                .withUsername("user")
                .withPassword("p@s5w0rd")
                .build();
    }
}
