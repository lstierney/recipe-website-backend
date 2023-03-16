package com.lstierneyltd.recipebackend.service;

import com.lstierneyltd.recipebackend.exception.RecipeBackendException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileServiceImpl implements FileService {
    private final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
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
            String message = "Could not save uploaded file: " + e.getMessage();
            logger.error(message);
            throw new RecipeBackendException(message);
        }
    }
}
