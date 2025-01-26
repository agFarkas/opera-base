package hu.agfcodeworks.operangel.application.service;

import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Properties;
import java.util.PropertyResourceBundle;

@Service
public class PropertiesService {

    public Properties readProperties(@NonNull String fileName) {
        var file = new File(fileName);

        try (var inputStream = new FileInputStream(file)) {
            var resourceBundle = new PropertyResourceBundle(inputStream);

            return makeProperties(resourceBundle);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public Properties readPropertiesFromResource(@NonNull String fileName) {
        try (var inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            var resourceBundle = new PropertyResourceBundle(inputStream);

            return makeProperties(resourceBundle);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public void saveProperties(Properties properties, String path, String fileName) {
        var pathFile = new File(path);

        if(!pathFile.exists()) {
            pathFile.mkdirs();
        }

        var file = new File(fileName);
        if(!file.exists()) {
            createFile(file);
        }

        try (var outputStream = new FileOutputStream(file)) {
            properties.store(outputStream, "");
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    private static boolean createFile(File file) {
        try {
            return file.createNewFile();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    private Properties makeProperties(PropertyResourceBundle resourceBundle) {
        var properties = new Properties();

        resourceBundle.keySet()
                .forEach(k -> properties.setProperty(k, resourceBundle.getString(k)));
        return properties;
    }

    private InputStream makeResourceStream(String fileName) {
        return getClass().getResourceAsStream(fileName);
    }
}
