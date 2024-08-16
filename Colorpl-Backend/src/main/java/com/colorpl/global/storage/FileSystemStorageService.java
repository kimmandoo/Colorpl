package com.colorpl.global.storage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class FileSystemStorageService implements StorageService {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    @Override
    public UploadFile storeFile(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return null;
        }
        String originalFilename = multipartFile.getOriginalFilename();
        String storeFilename = createStoreFilename(originalFilename);
        try {
            multipartFile.transferTo(new File(getFullPath(storeFilename)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new UploadFile(originalFilename, storeFilename);
    }

    @Override
    public void deleteFile(String filename) {
        Path path = Paths.get(fileDir, filename);
        File file = path.toFile();

        if (!file.delete()) {
            log.info("파일 삭제에 실패했습니다. {}", filename);
        }
    }

    private String createStoreFilename(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
