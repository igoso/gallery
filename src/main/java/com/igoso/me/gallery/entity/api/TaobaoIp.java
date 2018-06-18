package com.igoso.me.gallery.entity.api;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * created by igoso at 2018/6/18
 **/

@Getter
@Setter
@NoArgsConstructor
public class TaobaoIp {
    private int code;
    private Data data;

    @NoArgsConstructor
    @Getter
    @Setter
    public class Data{
        private String ip;
        private String country;
        private String area;
        private String region;
        private String city;
        private String county;
        private String isp;
        private String country_id;
        private String area_id;
        private String region_id;
        private String city_id;
        private String county_id;
        private String isp_id;
    }
}
