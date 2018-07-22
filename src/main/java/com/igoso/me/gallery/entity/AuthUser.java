package com.igoso.me.gallery.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * created by igoso at 2018/7/21
 **/
@Getter
@Setter
@NoArgsConstructor
public class AuthUser extends Entity{
    //base
    private String username;
    private String phone;
    private String region;
    private int gender; //0-female, 1-male,2-other

    //extends
    private String userAgent;
    private String registerIp;
    private Date lastLoginTime;
    private Long loginCount;

}
