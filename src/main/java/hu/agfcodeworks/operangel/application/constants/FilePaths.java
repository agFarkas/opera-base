package hu.agfcodeworks.operangel.application.constants;

public interface FilePaths {

    String PATH_DB_SETTINGS = "configuration";

    String FILENAME_DB_SETTINGS = "%s/db-connection.properties".formatted(PATH_DB_SETTINGS);

    String PATH_DB_CHANGELOG = "db-change/changelog.xml";
}
