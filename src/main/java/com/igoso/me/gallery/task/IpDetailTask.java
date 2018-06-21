package com.igoso.me.gallery.task;

import com.igoso.me.gallery.dao.HeaderDetailDao;
import com.igoso.me.gallery.entity.HeaderDetail;
import com.igoso.me.gallery.entity.IpInfo;
import com.igoso.me.gallery.service.IpInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 周期性刷新IP详情，调用外部API
 * created by igoso at 2018/6/18
 **/
@Service
public class IpDetailTask {
    private static final Logger LOGGER = LoggerFactory.getLogger("IPDETAIL_TASK");

    @Resource
    private IpInfoService ipInfoService;

    @Resource
    private HeaderDetailDao headerDetailDao;

    // everyday 1h
    @Scheduled(cron = "0 0 1 * * ? ")
    public void freshIpDetails() {
        LOGGER.info("scheduled ip detail fill start");
        List<HeaderDetail> headerDetails = headerDetailDao.selectListBeforeDays(1);
        Set<String> ips = new HashSet<>();
        initIpList(headerDetails, ips);


        List<IpInfo> ipInfos = new ArrayList<>();
        try {
            for (String ip : ips) {
                IpInfo ipInfo = ipInfoService.fetchDetailTaobao(ip);
                if (null != ipInfo) {
                    ipInfos.add(ipInfo);
                    ipInfoService.saveOne(ipInfo);
                }

                // taobao api limit 10 per/s
                TimeUnit.MILLISECONDS.sleep(200);
            }
        } catch (Exception e) {
            LOGGER.error("scheduled ip detail fill fail:{}", e.getMessage());
        }

        LOGGER.info("scheduled ip detail fill success, size:{}", ipInfos.size());
    }


    private static void initIpList(List<HeaderDetail> headerDetails, Set<String> ips) {
        for (HeaderDetail detail : headerDetails) {
            ips.add(detail.getRemoteAddr());
        }
    }
}
