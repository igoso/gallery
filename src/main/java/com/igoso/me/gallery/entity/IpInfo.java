package com.igoso.me.gallery.entity;

import com.igoso.me.gallery.entity.api.TaobaoIp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * created by igoso at 2018/6/18
 **/

@NoArgsConstructor
@Getter
@Setter
public class IpInfo extends Entity{
    private String ip;
    private String country;
    private String area;
    private String region;
    private String city;
    private String county;
    private String isp;
    //detail source: taobao, manual, other
    private String source;


    /**
     *  convert from ip.taobao.com
     * @param taobaoIp
     * @return
     */
    public static IpInfo fromTaobaoIp(TaobaoIp taobaoIp) {
        if (null != taobaoIp && taobaoIp.getCode() == 0 && null != taobaoIp.getData()) {
            IpInfo ipInfo = new IpInfo();
            ipInfo.setIp(taobaoIp.getData().getIp());
            ipInfo.setCountry(taobaoIp.getData().getCountry());
            ipInfo.setArea(taobaoIp.getData().getArea());
            ipInfo.setRegion(taobaoIp.getData().getRegion());
            ipInfo.setCity(taobaoIp.getData().getCity());
            ipInfo.setCounty(taobaoIp.getData().getCounty());
            ipInfo.setIsp(taobaoIp.getData().getIsp());

            ipInfo.setSource("TAOBAO");

            return ipInfo;
        }else {
            return null;
        }
    }
}
