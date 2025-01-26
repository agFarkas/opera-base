package hu.agfcodeworks.operangel.application.util;

import lombok.experimental.UtilityClass;

import java.io.File;

@UtilityClass
public class FileUtil {

    public boolean fileExists(String path) {
        return new File(path)
                .exists();
    }
}
