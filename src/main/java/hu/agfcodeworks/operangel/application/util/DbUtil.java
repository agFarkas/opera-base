package hu.agfcodeworks.operangel.application.util;

import hu.agfcodeworks.operangel.application.settings.DbSettings;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DbUtil {

    private static final String DATASOURCE_URL_PATTERN = "jdbc:%s://%s:%s/%s";

    public String composeDbUrl(DbSettings dbSettings) {
        return DATASOURCE_URL_PATTERN.formatted(
                dbSettings.getDbEngine().getName(),
                dbSettings.getHost(),
                dbSettings.getPort(),
                dbSettings.getName()
        );
    }
}
