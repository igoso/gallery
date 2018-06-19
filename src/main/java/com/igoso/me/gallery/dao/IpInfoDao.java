package com.igoso.me.gallery.dao;

import com.igoso.me.gallery.entity.IpInfo;
import com.igoso.me.gallery.entity.vo.IpStatistic;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * created by igoso at 2018/6/18
 **/

@Repository
public interface IpInfoDao extends BaseDao<IpInfo>{

    List<IpStatistic> statisticsTop10();
}
