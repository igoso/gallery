package com.igoso.me.gallery.dao;

import java.util.List;

/**
 * created by igoso at 2018/6/18
 **/
public interface BaseDao<T> {

    /**
     * do insert
     * @param obj
     */
    void insert(T obj);

    /**
     * select List
     * @return
     */
    List<T> selectList();


    /**
     * select one
     * @param param
     * @return
     */
    T selectOne(String param);
}
