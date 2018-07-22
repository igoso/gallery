package com.igoso.me.gallery.controller;

import com.igoso.me.gallery.entity.AuthUser;
import com.igoso.me.gallery.service.RegisterService;
import com.igoso.me.gallery.util.ResponseUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * created by igoso at 2018/7/21
 **/
@Controller
public class AuthController {
    private static final String PHONE_NUMBER_REG = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";

    @RequestMapping("/login")
    public String login() {
        return "/auth/login";
    }

    @RequestMapping("/register")
    public String register() {
        return "/auth/register";
    }

    @Resource
    private RegisterService registerService;

    @RequestMapping(value = "/ajax/code/verify", method = RequestMethod.POST)
    @ResponseBody
    public Object ajaxRegContext(@RequestBody RegisterContext context) {
        //执行注册开始
        if (!checkPhoneNumber(context.getPhone())) {
            return ResponseUtil.build().failure("invalid");
        }
        registerService.authenticate(context.getPhone()); //send verify code
        String requestId = UUID.randomUUID().toString();
        context.setRequestId(requestId);
        //作为请求校验
        registerService.genRequest(requestId, context.getPhone());
        return context;
    }

    @RequestMapping(value = "/ajax/reg/commit", method = RequestMethod.POST)
    @ResponseBody
    public Object ajaxRegCommit(@RequestBody RegisterContext context) {
        if (context == null || context.getUser() == null) {
            return ResponseUtil.build().failure("invalid");
        }

//        if (!registerService.checkRequest(context.getPhone(), context.getRequestId())) {
//            return ResponseUtil.build().failure("illegal request");
//        }


        String msg = registerService.register(context.getUser());
        if ("OK".equals(msg)) {
            return ResponseUtil.build().success();
        } else {
            return ResponseUtil.build().failure(msg);
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    static class RegisterContext {
        private String requestId;
        private String phone;
        private String code;
        private AuthUser user;
    }

    private boolean checkPhoneNumber(String phone) {
        Pattern pattern = Pattern.compile(PHONE_NUMBER_REG);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }
}
