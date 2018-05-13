package com.igoso.me.gallery.service;

import com.igoso.me.gallery.dao.HouseDao;
import com.igoso.me.gallery.entity.HouseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * created by igoso at 2018/5/13
 **/
@Service
public class HouseService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HouseService.class);

    @Resource
    private HouseDao houseDao;

    public List<HouseModel> queryList() {
        List<HouseModel> houseList = new ArrayList<>();
        try {
            houseList = houseDao.selectList();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        return houseList;
    }
}
