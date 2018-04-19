package com.igoso.me.gallery.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by igoso on 18-4-19.
 */
@Getter
@Setter
public abstract class Entity {

    private Long id;
    private Date createTime;
    private Date updateTime;
    private String createBy;
    private String updateBy;
    private int status;
}
