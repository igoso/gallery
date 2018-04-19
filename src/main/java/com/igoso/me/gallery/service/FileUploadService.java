package com.igoso.me.gallery.service;


import com.igoso.me.gallery.entity.FileUpload;

/**
 * Created by igoso on 18-4-19.
 */
public interface FileUploadService {

    /**
     * retrieve file
     * @param fileName
     * @return
     */
    FileUpload findByFileName(String fileName);

    /**
     * upload file
     * @param fileUpload
     */
    void uploadFile(FileUpload fileUpload);
}
