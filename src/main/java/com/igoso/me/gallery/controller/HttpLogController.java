package com.igoso.me.gallery.controller;

import com.igoso.me.gallery.dao.HeaderDetailDao;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * created by igoso at 2018/5/26
 **/
@RequestMapping("/log")
@Controller
public class HttpLogController {

    @Resource
    private HeaderDetailDao headerDetailDao;

    @RequestMapping("/http")
    public String httpLog() {
        return "http_log";
    }

    @RequestMapping("/http/list")
    @ResponseBody
    public Object httpList() {
        return headerDetailDao.selectList();
    }

}
