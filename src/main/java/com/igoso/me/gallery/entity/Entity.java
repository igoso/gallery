package com.igoso.me.gallery.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Created by igoso on 18-4-19.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class Entity {

    private Long id;
    private Date createTime;
    private Date updateTime;
    private String createBy;
    private String updateBy;
    private int status;
}
