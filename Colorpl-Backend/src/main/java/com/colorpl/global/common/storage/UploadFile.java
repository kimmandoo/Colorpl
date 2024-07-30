package com.colorpl.global.common.storage;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UploadFile {

    private String uploadFileName;
    private String storeFileName;
}
