package com.igoso.me.gallery.controller;

import com.igoso.me.gallery.service.HouseService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * created by igoso at 2018/5/13
 **/
@RequestMapping("/house")
@Controller
public class HouseController {

    @Resource
    private HouseService houseService;

    @RequestMapping("/index")
    public String index() {
        return "house";
    }

    @RequestMapping("/list")
    @ResponseBody
    public Object list() {
        return houseService.queryList();
    }
}
