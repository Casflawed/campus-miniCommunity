package com.flameking.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/** 登录时
 * 用户名和密码的校验
 */
@Data
public class LoginDto implements Serializable {

    @Size(min = 3,max = 20)
    @NotBlank(message = "昵称不能为空")
    private String username;

    @Size(min = 6,max = 15)
    @NotBlank(message = "密码不能为空")
    private String password;
}
