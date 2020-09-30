package com.example.o2o.util;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Random;

/**
 *
 */
public class ImageUtil {

    private static final String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);
    private static final Random random = new Random();
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public File transferCommonsMultipartFileToFile(CommonsMultipartFile cFile) {

        File file = new File(cFile.getOriginalFilename());
        try {
            cFile.transferTo(file);
        } catch (IOException e) {
            logger.error("fail to transfer common multipartFile to file: " + e.getMessage());
        }

        return file;
    }
    /**
     * handle the thumbnail from images uploaded by users
     * and return the relative path of image that is stored
     * @param file
     * @param targetAddr
     * @return
     */
    public static String generateThumbnail(File file, String targetAddr) {
        String fileName = generateRandomName();
        String extension = getFileExtension(file);
        makeDirPath(targetAddr);
        String relativeAddr = targetAddr + fileName + extension;
        logger.debug("current relative address is: " + relativeAddr);
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
        logger.debug("current absolute address is: " + PathUtil.getImgBasePath() + relativeAddr);

        try {
            Thumbnails.of(file)
                    .size(200, 200)
                    .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "watermark.jpg")), 0.25f)
                    .outputQuality(0.8)
                    .toFile(dest);

        } catch (IOException e) {
            logger.error("fail to create thumbnail: " + e.getMessage());
        }

        return relativeAddr;
    }

    /**
     * create directory for the target address
     * @param targetAddr
     */
    private static void makeDirPath(String targetAddr) {
        Path path = Paths.get(PathUtil.getImgBasePath() + targetAddr);
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

    private static String getFileExtension(File file) {
        String originFile = file.getName();
        return originFile.substring(originFile.lastIndexOf("."));
    }


    private static String generateRandomName() {
        LocalDateTime now = LocalDateTime.now();
        return simpleDateFormat.format(now) + random.nextInt(90000) + 10000;
    }

    public static void main(String[] args) throws IOException {
        String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        Thumbnails.of(new File("/Users/xinjiezeng/Downloads/images/yellows.jpg"))
                .size(200, 200)
                .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "watermark.jpg")), 0.25f)
        .outputQuality(0.8)
        .toFile(new File("/Users/xinjiezeng/Downloads/images/newYellows.jpg"));



    }

}
