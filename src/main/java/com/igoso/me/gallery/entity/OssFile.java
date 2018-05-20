package com.igoso.me.gallery.entity;

import lombok.*;

/**
 * created by igoso at 2018/5/20
 **/

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OssFile extends Entity{
    private String filename;
    private String mimeType;
    private String size;
    private String height;
    private String width;
}
