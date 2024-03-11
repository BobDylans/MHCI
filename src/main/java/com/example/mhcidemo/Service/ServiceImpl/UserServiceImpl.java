package com.example.mhcidemo.Service.ServiceImpl;


import com.example.mhcidemo.Mapper.UserMapper;
import com.example.mhcidemo.Service.UserService;
import com.example.mhcidemo.Utils.JwtUtils;
import com.example.mhcidemo.Utils.SMSUtils;
import com.example.mhcidemo.Utils.ValidateCodeUtils;

import com.example.mhcidemo.common.StatusCode;
import com.example.mhcidemo.domain.LoginParam;
import com.example.mhcidemo.domain.Result;
import com.example.mhcidemo.domain.User;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.mhcidemo.common.StatusCode.PHONE_CODE;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean register(User user) {
        // 检查用户是否已存在
        User existingUser = userMapper.selectByUsername(user.getUsername());
        if (existingUser != null) {
            // 用户已存在，返回false
            return false;
        }
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 保存用户
        userMapper.insert(user);
        return true;
    }

    @Override
    public User login(String username, String password) {
        // 通过用户名查找用户
        User user = userMapper.selectByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            // 密码匹配，返回用户信息
            return user;
        }
        // 用户名不存在或密码不匹配，返回null
        return null;
    }

    @Override
    public Result sendMsg(String phone) {
        if (!ObjectUtils.isEmpty(phone)) {
            //生成随机的4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            //发送短信,还需要更改
            SMSUtils.sendMessage("融销通", "SMS_461965427", phone, code);
            //需要将生成的验证码保存到redis中进行缓存
            stringRedisTemplate.opsForValue().set(PHONE_CODE + phone, code);
            stringRedisTemplate.expire(PHONE_CODE + phone, 5, TimeUnit.MINUTES);
            log.info("验证码: ${}",code);
            return Result.success("发送成功");
        }
        return Result.error(200,"发送失败");
    }

    @Override
    public Result Login(LoginParam loginParam) {
        //获取手机号
        String phone = loginParam.getPhone();
        //获取验证码
        String code = loginParam.getCode().toString();

        //从Redis中获取保存的验证码
        String codeInRedis = stringRedisTemplate.opsForValue().get(PHONE_CODE + phone);
        //进行验证码的比对（页面提交的验证码和Redis中保存的验证码比对）
        if (codeInRedis != null && codeInRedis.equals(code)) {

            User user = userMapper.selectByPhone(phone);
            if (user == null) {
                //判断当前手机号对应的用户是否为新用户，如果是新用户就自动完成注册
                user = new User();
                user.setPhone(phone);
                user.setRole("user");
                user.setUsername(phone);
                String encodePassword = passwordEncoder.encode(loginParam.getPassword());
                user.setPassword(encodePassword);

                userMapper.insert(user);
            }else {
                return Result.error(StatusCode.ERROR,"注册失败");
            }


            return Result.success("注册成功");
        }
        return Result.error(StatusCode.ERROR,"注册失败");
    }

    @Override
    public List<User> selectAll() {
        return userMapper.selectAll();
    }

    @Override
    public Result Logon(LoginParam loginParam) {
        String phone = loginParam.getPhone();
        //获取验证码
        String code = loginParam.getCode().toString();

        //从Redis中获取保存的验证码
        String codeInRedis = stringRedisTemplate.opsForValue().get(PHONE_CODE + phone);
        //进行验证码的比对（页面提交的验证码和Redis中保存的验证码比对）
        if (codeInRedis != null && codeInRedis.equals(code)) {

            // 创建带有Token的成功响应
            return Result.success("用户登录成功");

        }

        return Result.error(StatusCode.ERROR,"登录失败");
    }
}
