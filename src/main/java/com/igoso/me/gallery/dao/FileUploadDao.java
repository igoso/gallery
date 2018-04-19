package com.igoso.me.gallery.dao;

import com.igoso.me.gallery.entity.FileUpload;
import org.springframework.stereotype.Repository;

/**
 * Created by igoso on 18-4-19.
 */
@Repository
public interface FileUploadDao{

    FileUpload findByFileName(String fileName);

    void saveAndFlush(FileUpload fileUpload);
}
