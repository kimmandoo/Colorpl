package com.colorpl.global.common.storage;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    UploadFile storeFile(MultipartFile multipartFile) throws IOException;
}
