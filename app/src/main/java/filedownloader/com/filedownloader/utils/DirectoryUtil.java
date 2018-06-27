package filedownloader.com.filedownloader.utils;

import java.io.File;

public class DirectoryUtil {

    public static String createDirectory(File file) {
        if (!file.isDirectory()) {
            file.mkdir();
            return file.getAbsolutePath();
        }
        return file.getAbsolutePath();
    }

}
