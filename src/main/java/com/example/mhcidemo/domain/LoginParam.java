package com.example.mhcidemo.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 20179
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "登录参数")
public class LoginParam {

    @ApiModelProperty(value = "验证码")
    private Integer code;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;
}



