package com.colorpl.global.common.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    UploadFile storeFile(MultipartFile multipartFile);
}
