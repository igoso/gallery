package com.igoso.me.gallery.util;

import lombok.Getter;
import lombok.Setter;

/**
 * created by igoso at 2018/5/20
 **/
public class ResponseUtil {


    public static Response build() {
        return new Response();
    }

    @Getter
    @Setter
    public static class Response<T> {
        T data;
        Integer code = 0;
        String message = "";

        Response(){

        }

        public Response success() {
            return this;
        }

        public Response<T> success(T data) {
            this.code = 0;
            this.data = data;
            return this;
        }

        public Response failure() {
            this.code = -1;
            return this;
        }

        public Response failure(String message) {
            this.code = -1;
            this.message = message;
            return this;
        }

        public Response code(Integer code) {
            this.code = code;
            return this;
        }

        public Response message(String message) {
            this.message = message;
            return this;
        }
    }
}
