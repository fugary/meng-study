package com.mengstudy.simple.boot.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * Created on 2020/4/8 10:55 .<br>
 *
 * @author gary.fu
 */
@TableName("t_user")
@Data
public class User {

    private Long id;
    private String userName;
    private String nickName;
    private Date birth;
    private String userPassword;
    private Integer userStatus;
}
