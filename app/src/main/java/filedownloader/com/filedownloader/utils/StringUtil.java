package filedownloader.com.filedownloader.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class StringUtil {

    private static final double SPACE_KB = 1024;
    private static final double SPACE_MB = 1024 * SPACE_KB;
    private static final double SPACE_GB = 1024 * SPACE_MB;
    private static final double SPACE_TB = 1024 * SPACE_GB;

    public static String bytes2String(long sizeInBytes) {
        NumberFormat nf = new DecimalFormat();
        nf.setMaximumFractionDigits(2);
        try {
            if (sizeInBytes < SPACE_KB) {
                return nf.format(sizeInBytes) + " Byte(s)";
            } else if (sizeInBytes < SPACE_MB) {
                return nf.format(sizeInBytes / SPACE_KB) + " KB";
            } else if (sizeInBytes < SPACE_GB) {
                return nf.format(sizeInBytes / SPACE_MB) + " MB";
            } else if (sizeInBytes < SPACE_TB) {
                return nf.format(sizeInBytes / SPACE_GB) + " GB";
            } else {
                return nf.format(sizeInBytes / SPACE_TB) + " TB";
            }
        } catch (Exception e) {
            return sizeInBytes + " Byte(s)";
        }
    }

    public static String getFileNameFromUrl(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }

    public static String getFileExtFromUrl(String url) {
        String fileName = getFileNameFromUrl(url);
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

}
