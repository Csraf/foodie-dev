package com.imooc.controller;

import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBO;
import com.imooc.service.UserService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "注册用户", tags = {"注册登录的相关接口"})
@RestController
@RequestMapping("/passport")
public class PassportController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户名是否存在", notes = "用户名是否存在", httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public IMOOCJSONResult usernameIsExist(String username){
        // 1. 用户名不能为空
        if(StringUtils.isBlank(username)){
            return IMOOCJSONResult.errorMsg("用户名不能为空");
        }
        boolean isExist = userService.queryUsernameIsExist(username);
        // 2. 查找注册的用户名是否存在
        if(isExist){
            return IMOOCJSONResult.errorMsg("用户名已经存在");
        }
        // 3. 请求成功，用户名没有重复
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户注册", notes = "用户注册", httpMethod = "POST")
    @PostMapping("/register")
    public IMOOCJSONResult register(@RequestBody UserBO userBO,
                                    HttpServletRequest request,
                                    HttpServletResponse response){
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPassword = userBO.getConfirmPassword();
        // 1. 用户名，密码，确认密码都不能为空
        if(StringUtils.isBlank(username) ||
                StringUtils.isBlank(password) ||
                StringUtils.isBlank(confirmPassword)){
            return IMOOCJSONResult.errorMsg("用户名、密码和确认密码都不能为空");
        }
        // 2. 密码长度不能小于6
        if(password.length() < 6 || confirmPassword.length() < 6){
            return IMOOCJSONResult.errorMsg("密码和确认密码长度必须小于 6");
        }
        // 3. 密码和确认密码相同
        if(!password.equals(confirmPassword)){
            return IMOOCJSONResult.errorMsg("两次密码输入不相同");
        }
        // 4. 用户名是否存在
        boolean isExist = userService.queryUsernameIsExist(username);
        if(isExist){
            return IMOOCJSONResult.errorMsg("用户名已经存在");
        }
        // 5. 注册用户
        Users userResult = userService.createUser(userBO);

        userResult = setNullProperty(userResult);
        CookieUtils.setCookie(request,response,"user",
                JsonUtils.objectToJson(userResult),
                true);

        return IMOOCJSONResult.ok(userResult);
    }

    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "GET")
    @PostMapping("/login")
    public IMOOCJSONResult login(@RequestBody UserBO userBO,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        // 1. 用户名，密码，确认密码都不能为空
        if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            return IMOOCJSONResult.errorMsg("用户名、密码和确认密码都不能为空");
        }
        // 2. 判断用户名和密码是否正确
        Users userResult = userService.queryUserForLogin(username, MD5Utils.getMD5Str(password));
        if(userResult == null){
            return IMOOCJSONResult.errorMsg("用户名和密码不正确");
        }
        userResult = setNullProperty(userResult);
        CookieUtils.setCookie(request,response,"user",
                JsonUtils.objectToJson(userResult),
                true);

        return IMOOCJSONResult.ok(userResult);
    }

    @ApiOperation(value = "用户退出登录", notes = "用户退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    public IMOOCJSONResult logout(HttpServletRequest request,
                                  HttpServletResponse response){
        CookieUtils.deleteCookie(request, response, "user");
        // TODO 用户退出登录，需要清空购物车
        // TODO 分布式会话中需要清除用户数据
        return IMOOCJSONResult.ok();
    }


    private Users setNullProperty(Users userResult){
        userResult.setUpdatedTime(null);
        userResult.setCreatedTime(null);
        userResult.setSex(null);
        userResult.setBirthday(null);
        userResult.setMobile(null);
        userResult.setEmail(null);
        userResult.setPassword(null);
        return userResult;
    }
}
