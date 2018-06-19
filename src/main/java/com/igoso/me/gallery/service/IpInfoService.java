package com.igoso.me.gallery.service;

import com.alibaba.fastjson.JSON;
import com.igoso.me.gallery.dao.IpInfoDao;
import com.igoso.me.gallery.entity.IpInfo;
import com.igoso.me.gallery.entity.api.TaobaoIp;
import com.igoso.me.gallery.entity.vo.IpStatistic;
import com.igoso.me.gallery.util.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * created by igoso at 2018/6/18
 **/

@Service
public class IpInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IpInfoService.class);

    @Resource
    private IpInfoDao ipInfoDao;

    public IpInfo fetchOne(String ip) {
        IpInfo ipInfo = null;
        try {
            ipInfo = ipInfoDao.selectOne(ip);
        } catch (Exception e) {
            LOGGER.error("get ip info from db error:{}", e.getMessage());
        }

        return ipInfo;
    }


    public boolean saveOne(IpInfo ipInfo) {
        try {
            if (null == ipInfo || StringUtils.isEmpty(ipInfo.getIp())) {
                LOGGER.error("empty, ip:{}",JSON.toJSONString(ipInfo));
                return false;
            }
            ipInfoDao.insert(ipInfo);
            return true;
        } catch (Exception e) {
            LOGGER.error("save ip info to db error:{}", e.getMessage());
        }

        return false;
    }


    public List<IpInfo> selectList() {
        try {
            return ipInfoDao.selectList();
        } catch (Exception e) {
            LOGGER.error("get list for ip details error:{}",e.getMessage());
        }

        return null;
    }

    public List<IpStatistic> statisticsTop10() {
        //get top10 ip
        try {
            return ipInfoDao.statisticsTop10();
        } catch (Exception e) {
            LOGGER.error("get top10 ip error:{}",e.getMessage());
        }
        return null;
    }


    public IpInfo fetchDetailTaobao(String ip) {
        if (StringUtils.isEmpty(ip)) {
            return null;
        }

        String taobaoUrl = "http://ip.taobao.com/service/getIpInfo.php?ip=%s";

        String result = HttpUtils.doGet(String.format(taobaoUrl, ip));
        LOGGER.info("get from taobao, result:{}", result);

        TaobaoIp taobaoIp = JSON.parseObject(result, TaobaoIp.class);
        return IpInfo.fromTaobaoIp(taobaoIp);
    }

}
