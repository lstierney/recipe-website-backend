package com.lstierneyltd.recipebackend.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    void saveMultiPartFile(MultipartFile multipartFile);
}
