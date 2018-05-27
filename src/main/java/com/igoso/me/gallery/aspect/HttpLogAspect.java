package com.igoso.me.gallery.aspect;

import com.alibaba.fastjson.JSON;
import com.igoso.me.gallery.dao.HeaderDetailDao;
import com.igoso.me.gallery.entity.HeaderDetail;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * created by igoso at 2018/5/26
 **/
@Aspect
@Component
public class HttpLogAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger("HTTP_LOG");

    private static final String LOG_HTTP_URI_PREFIX = "/log/http";

    @Resource
    private HeaderDetailDao headerDetailDao;

    @Pointcut("execution (public * com.igoso.me.gallery.controller.*.*(..))")
    public void log() {

    }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null == attributes) {
            LOGGER.error("get servlet request attributes null");
            return;
        }
        HttpServletRequest request = attributes.getRequest();

        //exclude itself
        if (request.getRequestURI().contains(LOG_HTTP_URI_PREFIX)) {
            LOGGER.info("request http log self");
            return;
        }

        Map<String,String> headers = new HashMap<>();
        String name;
        request.getAttributeNames();
        for (Enumeration e = request.getHeaderNames(); e.hasMoreElements();) {
            name = e.nextElement().toString();
            headers.put(name, request.getHeader(name));
        }

        headers.put("uri", request.getRequestURI());
        headers.put("queryString", request.getQueryString());
        headers.put("remoteAddr", request.getRemoteAddr());
        headers.put("method", request.getMethod());
        headers.put("remoteHost",request.getRemoteHost());
        headers.put("remotePort",String.valueOf(request.getRemotePort()));
        headers.put("protocol",request.getProtocol());

        HeaderDetail headerDetail = new HeaderDetail(headers, JSON.toJSONString(headers));
        LOGGER.debug(JSON.toJSONString(headers,true));

        try {
            headerDetailDao.insert(headerDetail);
        } catch (Exception e) {
            LOGGER.error("insert header detail error:{}",e);
        }

    }

}
