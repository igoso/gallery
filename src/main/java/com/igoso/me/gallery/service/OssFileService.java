package com.igoso.me.gallery.service;

import com.aliyun.oss.common.utils.BinaryUtil;
import com.igoso.me.gallery.dao.OssFileDao;
import com.igoso.me.gallery.entity.OssFile;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

/**
 * created by igoso at 2018/5/20
 **/
@Service
public class OssFileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OssFileService.class);
    private static final String ISS_URL = "http://iss.igosh.com/";


    @Resource
    private OssFileDao ossFileDao;

    public List<OssFile> queryList() {
        List<OssFile> fileList = new ArrayList<>();
        try {
            fileList = ossFileDao.selectList();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        return fileList;
    }

    public OssFile selectOne(String filename) {
        try {
            return ossFileDao.selectOne(filename);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        return null;
    }

    //verify oss callback request
    public void verifyOssCallbackRequest(HttpServletRequest request, String body) throws Exception {
        String authorizationInput = request.getHeader("Authorization");
        String pubKeyInput = request.getHeader("x-oss-pub-key-url");
        byte[] authorization = BinaryUtil.fromBase64String(authorizationInput);
        if (Strings.isEmpty(pubKeyInput) || Strings.isEmpty(authorizationInput)) {
            throw new IllegalAccessException("oss-pub-key-url or authorization is null");
        }
        byte[] pubKey = BinaryUtil.fromBase64String(pubKeyInput);
        String pubKeyAddr = new String(pubKey);
        if (!pubKeyAddr.startsWith("http://gosspublic.alicdn.com/") && !pubKeyAddr.startsWith("https://gosspublic.alicdn.com/")) {
            throw new IllegalArgumentException("pub key addr must be oss address");
        }
        String retString = executeGet(pubKeyAddr);
        retString = retString.replace("-----BEGIN PUBLIC KEY-----", "");
        retString = retString.replace("-----END PUBLIC KEY-----", "");
        String queryString = request.getQueryString();
        String uri = request.getRequestURI();
        String authStr = java.net.URLDecoder.decode(uri, "UTF-8");
        if (queryString != null && !queryString.equals("")) {
            authStr += "?" + queryString;
        }
        authStr += "\n" + body;

        if (!doCheck(authStr, authorization, retString)) {
            throw new IllegalAccessException("request not from oss");
        }
    }

    private String executeGet(String url) {
        BufferedReader in = null;

        String content = null;
        try {
            // 实例化HTTP方法
            HttpGet request = new HttpGet();
            request.setURI(new URI(url));
            HttpResponse response = HttpClientBuilder.create().build().execute(request);

            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line;
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line).append(NL);
            }
            in.close();
            content = sb.toString();
        } catch (Exception e) {
            LOGGER.error("execute get error:{}",e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();// 最后要关闭BufferedReader
                } catch (Exception e) {
                    LOGGER.error("close buffered reader error:{}",e.getMessage());
                }
            }
        }
        return content;
    }

    private boolean doCheck(String content, byte[] sign, String publicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = BinaryUtil.fromBase64String(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            java.security.Signature signature = java.security.Signature.getInstance("MD5withRSA");
            signature.initVerify(pubKey);
            signature.update(content.getBytes());
            return signature.verify(sign);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean save(OssFile ossFile) {
        try {
            if (null == ossFile || null == ossFile.getFilename()) {
                return false;
            }

            String filename = ISS_URL + ossFile.getFilename();
            //文件已上传过，不再额外保存记录
            if (selectOne(filename) != null) {
                LOGGER.warn("file has been uploaded:{}",filename);
                return false;
            }
            ossFile.setFilename(filename);
            ossFileDao.insert(ossFile);

            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return false;
        }
    }

}
