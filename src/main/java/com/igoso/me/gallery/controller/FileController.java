package com.igoso.me.gallery.controller;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.igoso.me.gallery.entity.FileUpload;
import com.igoso.me.gallery.entity.OssFile;
import com.igoso.me.gallery.service.FileUploadService;
import com.igoso.me.gallery.service.OssFileService;
import com.igoso.me.gallery.util.ResponseUtil;
import com.igoso.me.gallery.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.*;

/**
 * Created by igoso on 18-4-19.
 */
@Controller
public class FileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);
    @Resource
    private FileUploadService fileUploadService;

    @RequestMapping(value = "/download", method = RequestMethod.POST)
    public ResponseEntity downloadFile(@RequestParam("filename") String filename) {

        FileUpload fileUpload = fileUploadService.findByFileName(filename);
        if (null == fileUpload) {
            return new ResponseEntity<>("{}", HttpStatus.NOT_FOUND);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("content-disposition", "attachment; filename=" + fileUpload.getFileName());
        String primaryType, subType;
        try {
            primaryType = fileUpload.getMimeType().split("/")[0];
            subType = fileUpload.getMimeType().split("/")[1];
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        headers.setContentType(new MediaType(primaryType, subType));

        return new ResponseEntity<>(fileUpload.getFileData(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity uploadFile(MultipartHttpServletRequest request) {
        try {
            Iterator<String> itr = request.getFileNames();
            while (itr.hasNext()) {
                String uploadedFileName = itr.next();
                MultipartFile file = request.getFile(uploadedFileName);
                String mimeType = file.getContentType();
                String fileName = file.getOriginalFilename();
                byte[] bytes = file.getBytes();

                FileUpload newFile = new FileUpload(fileName, bytes, mimeType);
                //set create time
                newFile.setCreateTime(new Date());
                fileUploadService.uploadFile(newFile);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    //aliyun oss demo
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.accessId}")
    private String accessId;

    @Value("${aliyun.oss.accessKey}")
    private String accessKey;

    @Value("${aliyun.oss.bucket}")
    private String bucket;

    @Value("${aliyun.oss.dir.root}")
    private String rootDir;


    @Value("${file.danger.op.code}")
    private String secretCode;

    @RequestMapping(value = "/send_request",method = RequestMethod.GET)
    public void sendRequest(HttpServletRequest request, HttpServletResponse response){
        String host = "https://" + bucket + "." + endpoint;
        OSSClient client = new OSSClient(endpoint, accessId, accessKey);
        try {
            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            String dir = rootDir + "/" + TimeUtil.currentMonth() + "/";
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = client.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = client.calculatePostSignature(postPolicy);
            String callback = generateCallbackParams();

            Map<String, String> respMap = new LinkedHashMap<String, String>();
            respMap.put("accessid", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            //respMap.put("expire", formatISO8601Date(expiration));
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            respMap.put("callback", callback);
            LOGGER.debug("send request:{}", JSON.toJSONString(respMap));
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST");
            response(request, response, JSON.toJSONString(respMap));

        } catch (Exception e) {
            LOGGER.error("error occurred:{}",e.getMessage());
        }
        client.shutdown();
    }

    private void response(HttpServletRequest request, HttpServletResponse response, String results) throws IOException {
        String callbackFunName = request.getParameter("callback");
        if (callbackFunName==null || callbackFunName.equalsIgnoreCase(""))
            response.getWriter().println(results);
        else
            response.getWriter().println(callbackFunName + "( "+results+" )");
        response.setStatus(HttpServletResponse.SC_OK);
        response.flushBuffer();
    }


    private String generateCallbackParams() {
        String body = "{\n" +
                "    \"filename\": ${object},\n" +
                "    \"mimeType\": ${mimeType},\n" +
                "    \"size\": \"${size}\",\n" +
                "    \"height\": \"${imageInfo.height}\",\n" +
                "    \"width\": \"${imageInfo.width}\"\n" +
                "}";
        Map<String, String> params = new HashMap<>();
        params.put("callbackUrl", "https://www.igosh.com/oss/callback");
        params.put("callbackHost", "www.igosh.com");
        params.put("callbackBody", body);
        params.put("callbackBodyType", "application/json");

        return Base64.getEncoder().encodeToString(JSON.toJSONBytes(params));
    }

    @Resource
    private OssFileService ossFileService;


    @RequestMapping("/oss/callback")
    @ResponseBody
    public Object callback(HttpServletRequest request,@RequestBody String result) {
        LOGGER.info(result);
        try {
            ossFileService.verifyOssCallbackRequest(request,result);
            OssFile ossFile = JSON.parseObject(result, OssFile.class);
            boolean res = ossFileService.save(ossFile);
            return res?ResponseUtil.build().success(result):ResponseUtil.build().failure();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return ResponseUtil.build().failure(e.getMessage());
        }

    }

    @RequestMapping("/oss/list")
    @ResponseBody
    public Object ossList() {
       return ossFileService.queryList();
    }

    @RequestMapping("/oss/delete")
    @ResponseBody
    public Object ossDelete(@RequestBody Map<String, String> request) {
        if (request == null
                || StringUtils.isEmpty(request.get("code"))
                || StringUtils.isEmpty(request.get("filename"))) {
            return ResponseUtil.build().failure("参数错误");
        }

        String code = request.get("code");
        String filename = request.get("filename");

        if (!checkCode(code)) {
            return ResponseUtil.build().failure("invalid code");
        }

        if (ossFileService.delete(filename)) {
            return ResponseUtil.build().success();
        }
        return ResponseUtil.build().failure("fail delete :" + filename);
    }

    @RequestMapping("/oss/files/size")
    @ResponseBody
    public Long totalSize() {
        return ossFileService.countTotalSize();
    }

    @RequestMapping("/oss/files")
    public String ossFiles() {
        return "ossfiles";
    }
     
     private boolean checkCode(String code) {
        try {
            String secretKey = "79877c8694c71fb7ae5594d01488de83";
            byte[] bytes = MessageDigest.getInstance("MD5").digest((code+secretKey).getBytes("UTF-8"));
            StringBuilder md5hex = new StringBuilder(new BigInteger(1, bytes).toString(16));
            for (int i = 0; i < 32 - md5hex.length(); i++) {
                md5hex.insert(0, "0");
            }
            return secretCode.equalsIgnoreCase(md5hex.toString());
        } catch (Exception e) {
         return false;
        }
    }

}
