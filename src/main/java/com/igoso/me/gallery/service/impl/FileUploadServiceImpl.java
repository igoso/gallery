package com.igoso.me.gallery.service.impl;

import com.igoso.me.gallery.dao.FileUploadDao;
import com.igoso.me.gallery.entity.FileUpload;
import com.igoso.me.gallery.service.FileUploadService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by igoso on 18-4-19.
 */
@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Resource
    private FileUploadDao fileUploadDao;

    @Override
    public FileUpload findByFileName(String fileName) {
        return fileUploadDao.findByFileName(fileName);
    }

    @Override
    public void uploadFile(FileUpload fileUpload) {
        fileUploadDao.saveAndFlush(fileUpload);
    }
}
