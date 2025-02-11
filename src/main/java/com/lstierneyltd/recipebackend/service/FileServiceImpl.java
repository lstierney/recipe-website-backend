package com.lstierneyltd.recipebackend.service;

import com.lstierneyltd.recipebackend.entities.Recipe;
import com.lstierneyltd.recipebackend.exception.RecipeBackendException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    private final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Value("${file.upload.directory}")
    private String fileUploadDirectory;

    @Override
    public void createImageFolder(Recipe recipe) {
        Path path = Paths.get(getRecipeFolderPath(recipe));
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                System.out.println("Directory " + path + " created");
            } else {
                System.out.println("Directory " + path + " already exists, skipping creation");
            }
        } catch (Exception e) {
            throw new RecipeBackendException(e.getMessage());
        }
    }

    @Override
    public void addImageToRecipe(MultipartFile imageFile, Recipe recipe) {
        try {
            byte[] bytes = imageFile.getBytes();
            Path path = Paths.get(getRecipeFolderPath(recipe) + "/" + imageFile.getOriginalFilename());
            Files.write(path, bytes);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RecipeBackendException("Could not save uploaded file: " + e.getMessage());
        }
    }

    @Override
    public List<String> getFilesInDirectory(String dirPath) {
        List<String> fileNames = new ArrayList<>();
        Path path = Paths.get(dirPath);

        try (DirectoryStream<Path> paths = Files.newDirectoryStream(path)) {
            for (Path p : paths) {
                fileNames.add(p.getFileName().toString());
            }
        } catch (IOException e) {
            logger.error("Could not read files in dir: " + dirPath);
            // i think throwing an exception here is reasonable
            // as we expect the folder, and at least one image, to exist
            throw new RecipeBackendException(e.getMessage());
        }
        return fileNames;
    }

    private String getRecipeFolderPath(Recipe recipe) {
        return fileUploadDirectory + "/images/" + recipe.getImageFolderName();
    }
}
