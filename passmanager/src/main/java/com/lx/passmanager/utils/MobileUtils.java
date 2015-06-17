/**    
 * FileName: MobileUtils.java
 * Version Info: mobile-service
 * Date: 2014年12月25日 
 * Copyright  Corporation 2014     
 * All Rights Reserved  
 */

package com.lx.passmanager.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**    
 *
 * Project Name：mobile-service    
 * Class Name：MobileUtils    
 * Class Description：   
 * Creater ：xliu    
 * Create Time：2014年12月25日 下午12:22:16    
 * Modifier：xliu    
 * Modification Time：2014年12月25日 下午12:22:16    
 * Modify Notes：    
 * 
 */

public class MobileUtils {

    public static final String USER_DIR = "user.dir";

    private static final Logger LOG = LoggerFactory.getLogger(MobileUtils.class);

    /**
     * 
     * get resource stream from jar    
     * @param relativeResourcePath
     * @return 
     * @throws Exception
     */
    public static InputStream getInputStreamFromJar(String relativeResourcePath) throws Exception {
        InputStream inStream = MobileUtils.class.getClassLoader().getResourceAsStream(relativeResourcePath);
        return inStream;
    }

    /**
     * 
     * get absolute Resource Path from jar  which like "xxxx.jar!/directory/file" 
     * It's impossible to get stream !!!!  and throws FileNotFoundException like this:
     * {
     *  File file = new File(absoluteResourcePath)    
     *  FileInputStream fis = new FileInputStream(file);
     * }
     * @param relativeResourcePath
     * @return  absoluteResourcePath
     * @throws Exception
     */
    public static String getFilePathFromJar(String relativeResourcePath) throws Exception {
        URL url = MobileUtils.class.getClassLoader().getResource(relativeResourcePath);
        return url.getFile();
    }

    /**
     * get runtime path  eg: if run in config directory  and the path is xxx/xxx/config
     * @return current path
     */
    public static String getCurrentPathFromDisk() {
        return System.getProperty(USER_DIR);
    }

    //    /**
    //     * Available on JDK1.7 or above
    //     * create directory 
    //     * @param directory
    //     * @return directory
    //     */
    //    public static File createDirectory(File directory) {
    //        if (directory == null) {
    //            throw new IllegalArgumentException("Illegal directory!");
    //        }
    //
    //        boolean exists = Files.exists(directory.toPath());
    //        if (!exists) {
    //            try {
    //                Files.createDirectories(directory.toPath());
    //            } catch (IOException e) {
    //                LOG.error("create " + directory.getAbsolutePath() + " failed!", e);
    //            }
    //        }
    //        return directory;
    //    }

    public static File createDirectory(File directory) {
        if (directory == null) {
            throw new IllegalArgumentException("Illegal directory!");
        }
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return directory;
    }

    //    /**
    //     * Available on JDK1.7 or above
    //     * create file by directory and file name, It will override existing Files
    //     * @param directory
    //     * @param fileName
    //     * @return created file
    //     */
    //    public static File createFile(File directory, String fileName) {
    //        if (StringUtils.isBlank(fileName) || fileName.contains(File.separator)) {
    //            throw new IllegalArgumentException("Illegal file name!");
    //        }
    //        File file = getAbsoluteFilePath(directory, fileName);
    //        try {
    //            createDirectory(directory);
    //            Files.deleteIfExists(file.toPath());
    //            file = Files.createFile(file.toPath()).toFile();
    //        } catch (IOException e) {
    //            LOG.error("create " + directory.getAbsolutePath() + File.separator + fileName + " failed!", e);
    //        }
    //        return file;
    //    }

    public static File createFile(File directory, String fileName) {
        if (StringUtils.isBlank(fileName) || fileName.contains(File.separator)) {
            throw new IllegalArgumentException("Illegal file name!");
        }
        File file = getAbsoluteFilePath(directory, fileName);
        try {
            createDirectory(directory);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            LOG.error("create " + directory.getAbsolutePath() + File.separator + fileName + " failed!", e);
        }
        return file;
    }

    private static File getAbsoluteFilePath(File directory, String fileName) {
        File file = new File(directory.getAbsolutePath() + File.separator + fileName);
        return file;
    }

    /**
     * create file by absolute file path, It will override existing Files
     * @param absoluteFilePath
     * @return created file
     */
    public static File createFile(String absoluteFilePath) {
        if (StringUtils.isBlank(absoluteFilePath)) {
            throw new IllegalArgumentException("Illegal file path!");
        }

        String directory = absoluteFilePath.substring(0, absoluteFilePath.lastIndexOf(File.separator));
        String fileName = absoluteFilePath.substring(absoluteFilePath.lastIndexOf(File.separator) + 1,
                absoluteFilePath.length());
        return createFile(new File(directory), fileName);
    }

    public static void main(String[] args) {
        String file = "C:\\Users\\xliu\\Desktop\\files\\v3\\f1\\fv1";
        createFile(file + File.separator + "123");
    }

}
