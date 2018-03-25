package com.judy.mmall.service.impl;

import com.google.common.collect.Lists;
import com.judy.mmall.service.IFileService;
import com.judy.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service("iFileService")
public class FileServiceImpl implements IFileService {
    private Logger logger = LoggerFactory.getLogger(IFileService.class);

    @Override
    public String uploadFile(MultipartFile file, String path) {
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);
        String uploadFileName = UUID.randomUUID().toString() + "." + fileExtension;
        logger.info("开始上传文件，上传的文件名:{},路径:{},新文件名:{}", originalFilename, path, uploadFileName);

        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }

        File targetFile = new File(path, uploadFileName);

        try {
            file.transferTo(targetFile);
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            targetFile.delete();
        } catch (IOException e) {
            logger.error("上传文件异常", e);
            return null;
        }

        return targetFile.getName();
    }
}
