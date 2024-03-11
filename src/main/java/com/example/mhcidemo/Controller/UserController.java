package com.example.mhcidemo.Controller;


import com.example.mhcidemo.Service.UserService;
import com.example.mhcidemo.Utils.JwtUtils;
import com.example.mhcidemo.common.StatusCode;
import com.example.mhcidemo.domain.LoginParam;
import com.example.mhcidemo.domain.Result;
import com.example.mhcidemo.domain.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
@CrossOrigin
@Api(tags = "用户模块接口")
public class UserController {

    @Autowired
    private UserService userService;

    // 处理登录请求的方法
    @PostMapping("/login")
    public ResponseEntity<Result<?>> login(@RequestBody User user) {
        User user1 = userService.login(user.getUsername(), user.getPassword());

        if (user1 != null) {
            // 登录成功，生成Token
            String token = JwtUtils.generateToken(user1.getUsername());
            // 创建带有Token的成功响应
            return ResponseEntity.ok(Result.success("用户登录成功", token));
        } else {
            // 登录失败
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Result.error(StatusCode.ERROR, "用户名或密码错误"));
        }
    }


    // 处理注册请求的方法
    @PostMapping("/register")
    public ResponseEntity<Result<?>> register(@RequestBody User user) {
        boolean registerSuccess = userService.register(user);

        if (registerSuccess) {
            // 如果注册成功，使用Result类返回成功响应
            return ResponseEntity.ok(Result.success("用户注册成功"));
        } else {
            // 如果注册失败，使用Result类返回错误响应
            return ResponseEntity.badRequest().body(Result.error(StatusCode.ERROR, "注册失败，可能是用户名已存在或其他原因"));
        }
    }
    @ApiOperation(value = "发送验证码")
    @GetMapping("/sendMsg")
    public Result sendMsg(
            @ApiParam(value = "手机号")
            String phone) {
        return userService.sendMsg(phone);

    }
    @ApiOperation(value = "注册通过验证码")
    @PostMapping("/login/code")
    public ResponseEntity<Result<?>> loginByPhone(
            @ApiParam(value = "登录参数")
            @RequestBody LoginParam loginParam
    ) {
        Result result = userService.Login(loginParam);
        return ResponseEntity.ok(Result.success("用户注册成功"));
    }
    @ApiOperation(value = "登录通过验证码")
    @PostMapping("/logon/code")
    public ResponseEntity<Result<?>> logonByPhone(
            @ApiParam(value = "登录参数")
            @ModelAttribute LoginParam loginParam
    ) {
        Result result = userService.Logon(loginParam);
        if (result.getStatus()==StatusCode.OK){
            String token = JwtUtils.generateToken(loginParam.getUsername());
            return ResponseEntity.ok(Result.success("用户登录成功", token));
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Result.error(StatusCode.ERROR, "用户名或密码错误"));
        }
    }



    @ApiOperation(value = "查询所有用户")
    @GetMapping("/getAll")
    public Result<List<User>> selectAll() {
        List<User> users = userService.selectAll();
        return Result.success(users);
    }
}

