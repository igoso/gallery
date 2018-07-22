package com.igoso.me.gallery.service;

import com.alibaba.fastjson.JSON;
import com.igoso.me.gallery.dao.AuthUserDao;
import com.igoso.me.gallery.entity.AuthUser;
import com.igoso.me.gallery.service.aliyun.SmsService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * created by igoso at 2018/7/21
 **/
@Service
public class RegisterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterService.class);

    private static Map<String, VerifyEntity> VERIFY_MAP = new ConcurrentHashMap<>();
    private static Map<String, String> REQUEST_MAP = new ConcurrentHashMap<>();

    private static final int VERIFY_CODE_MAP_MAX_SIZE = 100;//max code map size
    private static final Long VERIFY_CODE_VALID_TIME_LIMIT = 5 * 60 * 1000L;//5min

    @Resource
    private SmsService smsService;

    @Resource
    private AuthUserDao authUserDao;


    public void genRequest(String requestId, String phone) {
        REQUEST_MAP.put(phone, requestId);
    }


    public boolean checkRequest(String phone, String requestId) {
        if (REQUEST_MAP.get(phone) != null && REQUEST_MAP.get(phone).equals(requestId)) {
            REQUEST_MAP.remove(phone);
            return true;
        } else {
            return false;
        }
    }

    public boolean verify(String code, String phone) {
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(phone)) {
            return false;
        }

        if (VERIFY_MAP.get(phone) != null) {
            VerifyEntity e = VERIFY_MAP.get(phone);
            VERIFY_MAP.remove(phone);
            return e.check(code);
        }

        return true;
    }

    public void authenticate(String phone) {
        String code = generateCode();
        VerifyEntity entity = new VerifyEntity();
        entity.setVerifyTimestamp(System.currentTimeMillis());
        entity.setCode(code);
        if (VERIFY_MAP.size() < VERIFY_CODE_MAP_MAX_SIZE) {
            if (smsService.send(code, phone)) {
                VERIFY_MAP.put(code, entity);
            }
        } else {
            LOGGER.error("over max register online");
        }
    }


    public String register(AuthUser user) {
        try {
            AuthUser old = authUserDao.selectOne(user.getPhone());
            if (null != old) {
                return "already registered";
            }

            authUserDao.insert(user);
            return "OK";
        } catch (Exception e) {
            LOGGER.error("register user error in saving db, user:{}", JSON.toJSONString(user), e);
            return "internal error";
        }
    }


    private static String generateCode() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(new Random().nextInt(10)).append("");
        }

        return code.toString();
    }


    @Getter
    @Setter
    @NoArgsConstructor
    private class VerifyEntity {
        private String code;
        private Long verifyTimestamp;//timestamp

        private boolean check(String code) {
            return code.equals(this.code) && System.currentTimeMillis() - verifyTimestamp <= VERIFY_CODE_VALID_TIME_LIMIT;
        }
    }

}
