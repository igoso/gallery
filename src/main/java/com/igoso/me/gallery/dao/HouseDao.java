package com.igoso.me.gallery.dao;

import com.igoso.me.gallery.entity.HouseModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * created by igoso at 2018/5/13
 **/
@Repository
public interface HouseDao {

    List<HouseModel> selectList();
}
