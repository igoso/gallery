package com.igoso.me.gallery;

import com.alibaba.fastjson.JSON;
import com.igoso.me.gallery.entity.api.TaobaoIp;
import com.igoso.me.gallery.util.HttpUtils;
import org.junit.Test;

/**
 * created by igoso at 2018/6/18
 **/
public class ApiTest {


    @Test
    public void taobaoIpTest() {
        String url = "http://ip.taobao.com/service/getIpInfo.php?ip=%s";
        String ip = "11.38.88.45";

        String result = HttpUtils.doGet(String.format(url, ip));
        System.out.println(result);

        TaobaoIp taobaoIp = JSON.parseObject(result, TaobaoIp.class);

        System.out.println(taobaoIp.getData());
    }
}
