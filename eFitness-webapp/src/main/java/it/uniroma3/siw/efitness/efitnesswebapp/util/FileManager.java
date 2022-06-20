package it.uniroma3.siw.efitness.efitnesswebapp.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.multipart.MultipartFile;

public class FileManager {

    /*creates the directory and stores the file in it */
    public static String store(MultipartFile file, String directory) {
        new File(directory).mkdir();
        Path fileNameAndPath  = Paths.get(directory, file.getOriginalFilename());
        try {
            Files.write(fileNameAndPath, file.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileNameAndPath.getFileName().toString();
    }

    public static void removeImg(String directory, String name) {
        Path fileNameAndPath  = Paths.get(directory + "/" + name);
        try {
            Files.delete(fileNameAndPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeImgAndDir(String directory, String name) {
        removeImg(directory, name);
        removeDirectory(directory);
    }

    public static void removeImgs(String directory, String[] names) {
        for(String name : names) {
            removeImg(directory, name);
        }
    }

    public static void removeImgsAndDir(String directory, String[] names) {
        removeImgs(directory, names);
        removeDirectory(directory);
    }

    private static void removeDirectory(String directory) {
        new File(directory).delete();
    }

    public static void dirEmpty(String directory) {
        for(File file : new File(directory).listFiles()) {
            if(file.isDirectory()) dirEmptyEndDelete(directory + "/" + file.getName());
            try {
                Files.delete(file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void dirEmptyEndDelete(String directory) {
        dirEmpty(directory);
        removeDirectory(directory);
    }

    public static void dirChangeName(String oldDir, String newDir) {
        new File(oldDir).renameTo(new File(newDir));
    }

}

