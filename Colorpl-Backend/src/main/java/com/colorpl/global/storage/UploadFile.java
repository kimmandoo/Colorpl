package com.colorpl.global.storage;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UploadFile {

    private String uploadFilename;
    private String storeFilename;
}
