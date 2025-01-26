package hu.agfcodeworks.operangel.application.mapper;

import hu.agfcodeworks.operangel.application.settings.DbEngine;
import hu.agfcodeworks.operangel.application.settings.DbSettings;
import lombok.NonNull;

import java.util.Properties;

public class DbSettingsProperiesMapper extends PropertiesMapper<DbSettings> {

    private static final String DB_ENGINE = "db.engine";
    private static final String DB_HOST = "db.host";
    private static final String DB_PORT = "db.port";
    private static final String DB_NAME = "db.name";
    private static final String DB_USERNAME = "db.auth.username";
    private static final String DB_PASSWORD = "db.auth.password";

    @Override
    public Properties dtoToProperties(@NonNull DbSettings dbSettings) {
        var properties = new Properties();

        properties.put(DB_ENGINE, dbSettings.getDbEngine().getName());
        properties.put(DB_HOST, dbSettings.getHost());
        properties.put(DB_PORT, Integer.toString(dbSettings.getPort()));

        properties.put(DB_NAME, dbSettings.getName());
        properties.put(DB_USERNAME, dbSettings.getUsername());
        properties.put(DB_PASSWORD, dbSettings.getPassword());

        return properties;
    }

    @Override
    public DbSettings propertiesToDto(@NonNull Properties properties) {
        var dbEngine = properties.getProperty(DB_ENGINE);

        return DbSettings.builder()
                .withDbEngine(DbEngine.byName(dbEngine))
                .withHost(properties.getProperty(DB_HOST))
                .withPort(mapIntProperty(properties.getProperty(DB_PORT)))
                .withName(properties.getProperty(DB_NAME))
                .withUsername(properties.getProperty(DB_USERNAME))
                .withPassword(properties.getProperty(DB_PASSWORD))
                .build();
    }

    private static Integer mapIntProperty(String property) {
        try {
            return Integer.parseInt(property);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("The value of '%s' must be number.".formatted(property));
        }
    }
}
