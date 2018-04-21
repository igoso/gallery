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
public class FileUpload extends Entity{
    private String fileName;
    private byte[] fileData;
    private String mimeType;
}
