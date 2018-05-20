package com.igoso.me.gallery.service;

import com.igoso.me.gallery.dao.OssFileDao;
import com.igoso.me.gallery.entity.OssFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * created by igoso at 2018/5/20
 **/
@Service
public class OssFileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OssFileService.class);
    private static final String ISS_URL = "http://iss.igosh.com/";


    @Resource
    private OssFileDao ossFileDao;

    public List<OssFile> queryList() {
        List<OssFile> fileList = new ArrayList<>();
        try {
            fileList = ossFileDao.selectList();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        return fileList;
    }

    public OssFile selectOne(String filename) {
        try {
            return ossFileDao.selectOne(filename);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        return null;
    }

    public boolean save(OssFile ossFile) {
        try {
            if (null == ossFile || null == ossFile.getFilename()) {
                return false;
            }

            String filename = ISS_URL + ossFile.getFilename();
            //文件已上传过，不再额外保存记录
            if (selectOne(filename) != null) {
                LOGGER.warn("file has been uploaded:{}",filename);
                return false;
            }
            ossFile.setFilename(filename);
            ossFileDao.insert(ossFile);

            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return false;
        }
    }

}
