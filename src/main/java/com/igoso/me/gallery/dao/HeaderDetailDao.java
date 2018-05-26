package com.igoso.me.gallery.dao;

import com.igoso.me.gallery.entity.HeaderDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * created by igoso at 2018/5/26
 **/
@Repository
public interface HeaderDetailDao {

    void insert(HeaderDetail headerDetail);

    List<HeaderDetail> selectList();

}
