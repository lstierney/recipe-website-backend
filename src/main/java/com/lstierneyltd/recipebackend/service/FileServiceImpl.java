package com.lstierneyltd.recipebackend.service;

import com.lstierneyltd.recipebackend.exception.RecipeBackendException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileServiceImpl implements FileService {
    @Value("${file.upload.directory}")
    private String fileUploadDirectory;

    // TODO deal with "images/"
    @Override
    public void saveMultiPartFile(final MultipartFile multipartFile) {
        try {
            byte[] bytes = multipartFile.getBytes();
            Path path = Paths.get(fileUploadDirectory + "images/" + multipartFile.getOriginalFilename());
            Files.write(path, bytes);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RecipeBackendException("Could not save uploaded file: " + e.getMessage());
        }
    }
}
