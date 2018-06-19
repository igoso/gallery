package com.igoso.me.gallery;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan(basePackages = "com.igoso.me.gallery.dao")
@EnableTransactionManagement
@EnableScheduling
@SpringBootApplication
public class GalleryApplication {
        public static void main(String[] args) {
                SpringApplication.run(GalleryApplication.class, args);
        }
}
