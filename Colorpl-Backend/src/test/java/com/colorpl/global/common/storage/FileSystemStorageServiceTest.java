package com.colorpl.global.common.storage;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

@SpringBootTest
class FileSystemStorageServiceTest {

    @Autowired
    private FileSystemStorageService storageService;

    @Test
    void storeFile() {
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.jpg",
            "image/jpeg",
            "test".getBytes());
        UploadFile uploadFile = storageService.storeFile(file);
        Assertions.assertThat(uploadFile).isNotNull();
    }
}