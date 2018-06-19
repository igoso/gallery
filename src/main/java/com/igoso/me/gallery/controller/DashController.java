package com.igoso.me.gallery.controller;

import com.igoso.me.gallery.entity.IpInfo;
import com.igoso.me.gallery.service.IpInfoService;
import com.igoso.me.gallery.task.IpDetailTask;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * created by igoso at 2018/6/18
 **/
@Controller
@RequestMapping("/dash")
public class DashController {


    @Resource
    private IpInfoService ipInfoService;

    @RequestMapping("/ipinfo/detail/db/{ip}")
    @ResponseBody
    public Object checkIpDetail(@PathVariable(name = "ip") String ip) {
        if (StringUtils.isEmpty(ip)) {
            return "ip is empty";
        }


        if (ip.equals("all")) {
            List<IpInfo> ipInfos =  ipInfoService.selectList();
            if (CollectionUtils.isEmpty(ipInfos)) {
                return "get empty";
            }else {
                return ipInfos;
            }
        }

        IpInfo ipInfo = ipInfoService.fetchOne(ip);

        if (null != ipInfo) {
            return ipInfo;
        }else {
            return "not found";
        }
    }

    @RequestMapping("/ipinfo/detail/save/{ip}")
    @ResponseBody
    public String saveIp(@PathVariable(name = "ip") String ip) {
        if (StringUtils.isEmpty(ip)) {
            return "ip is empty";
        }


        IpInfo ipInfo = ipInfoService.fetchDetailTaobao(ip);
        if (ipInfoService.saveOne(ipInfo)) {
            return "save [" + ip + "]" + " ok.";
        }

        return "fail";
    }

    @Resource
    private IpDetailTask ipDetailTask;

    @RequestMapping("/ipinfo/detail/fresh/run")
    @ResponseBody
    public String freshRunDirect() {
        try {
            ipDetailTask.freshIpDetails();
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }


    @RequestMapping("/ipinfo/detail/top10")
    @ResponseBody
    public Object detailTop10() {
        return ipInfoService.statisticsTop10();
    }

}
