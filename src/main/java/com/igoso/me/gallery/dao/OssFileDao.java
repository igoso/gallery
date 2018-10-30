package com.igoso.me.gallery.dao;

import com.igoso.me.gallery.entity.OssFile;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * created by igoso at 2018/5/20
 **/
@Repository
public interface OssFileDao {

    void insert(OssFile ossFile);

    List<OssFile> selectList();

    OssFile selectOne(String filename);

    Long countTotalSize();

    int delete(String filename);
}
