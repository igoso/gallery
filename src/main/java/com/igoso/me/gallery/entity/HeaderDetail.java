package com.igoso.me.gallery.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * created by igoso at 2018/5/26
 **/

@Getter
@Setter
@NoArgsConstructor
public class HeaderDetail extends Entity{

    private String acceptLanguage;
    private String method;
    private String remoteHost;
    private String remotePort;
    private String remoteAddr;
    private String queryString;
    private String uri;
    private String accept;
    private String localHost;
    private String acceptEncoding;
    private String userAgent;
    private String protocol;
    private String extensions;//json

    public HeaderDetail(Map<String,String> origin, String extensions) {
        this.acceptLanguage = origin.get(HeaderAttributes.ACCEPT_LANGUAGE);
        this.method = origin.get(HeaderAttributes.METHOD);
        this.remoteAddr = origin.get(HeaderAttributes.REMOTE_ADDR);
        this.remoteHost = origin.get(HeaderAttributes.REMOTE_HOST);
        this.remotePort = origin.get(HeaderAttributes.REMOTE_PORT);
        this.queryString = origin.get(HeaderAttributes.QUERY_STRING);
        this.uri = origin.get(HeaderAttributes.URI);
        this.accept = origin.get(HeaderAttributes.ACCEPT);
        this.localHost = origin.get(HeaderAttributes.LOCAL_HOST);
        this.acceptEncoding = origin.get(HeaderAttributes.ACCEPT_ENCODING);
        this.userAgent = origin.get(HeaderAttributes.USER_AGENT);
        this.protocol = origin.get(HeaderAttributes.PROTOCOL);

        this.extensions = extensions; //other
    }

    private class HeaderAttributes{
        static final String ACCEPT_LANGUAGE = "accept-language";
        static final String METHOD = "method";
        static final String REMOTE_ADDR = "remoteAddr";
        static final String REMOTE_HOST = "remoteHost";
        static final String REMOTE_PORT = "remotePort";
        static final String QUERY_STRING = "queryString";
        static final String URI = "uri";
        static final String ACCEPT = "accept";
        static final String LOCAL_HOST = "host";
        static final String ACCEPT_ENCODING = "accept-encoding";
        static final String USER_AGENT = "user-agent";
        static final String PROTOCOL = "protocol";
    }
}


