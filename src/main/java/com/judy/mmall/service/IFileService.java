package com.judy.mmall.service;

import org.springframework.web.multipart.MultipartFile;

public interface IFileService {
    public String uploadFile(MultipartFile file, String path);
}
