package com.example.o2o.util;

public class PathUtil {

    private static String separator = System.getProperty("file.separator");

    public static String getImgBasePath() {
        String os = System.getProperty("os.name");
        String basePath = "";

        if (os.toLowerCase().startsWith("win")) {
            basePath = "D:/projectdev/images";
        }
        else {
            basePath = "/Users/xinjiezeng/Downloads/images";
        }

        return basePath.replace("/", separator);
    }

    public static String getShopImagePath(long shopId) {
        String imagePath = "/upload/items/shop/" + shopId;
        return imagePath.replace("/", separator);
    }
}
