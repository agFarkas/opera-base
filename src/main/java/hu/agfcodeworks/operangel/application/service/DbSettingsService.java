package hu.agfcodeworks.operangel.application.service;

import hu.agfcodeworks.operangel.application.mapper.DbSettingsProperiesMapper;
import hu.agfcodeworks.operangel.application.settings.DbSettings;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.UncheckedIOException;
import java.util.Properties;

import static hu.agfcodeworks.operangel.application.constants.FilePaths.FILENAME_DB_SETTINGS;
import static hu.agfcodeworks.operangel.application.constants.FilePaths.PATH_DB_SETTINGS;

@Service
@AllArgsConstructor
public class DbSettingsService {

    private static final String DEFAULT_PATH_DB_SETTINGS = "default-settings/db-connection.properties";

    private final PropertiesService propertiesService;

    private final DbSettingsProperiesMapper dbSettingsProperiesMapper;

    public DbSettings obtainDbSettings() {
        var dbSettings = obtainProperties();

        return dbSettingsProperiesMapper.propertiesToDto(dbSettings);
    }

    private Properties obtainProperties() {
        try {
            return propertiesService.readProperties(FILENAME_DB_SETTINGS);
        } catch (UncheckedIOException ex) {
            var properties = propertiesService.readPropertiesFromResource(DEFAULT_PATH_DB_SETTINGS);

            return properties;
        }
    }

    public void saveDbSettings(DbSettings dbSettings) {
        var properties = dbSettingsProperiesMapper.dtoToProperties(dbSettings);

        propertiesService.saveProperties(properties, PATH_DB_SETTINGS, FILENAME_DB_SETTINGS);
    }
}
