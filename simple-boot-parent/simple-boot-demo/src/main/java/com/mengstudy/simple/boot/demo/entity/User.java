package com.mengstudy.simple.boot.demo.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created on 2020/4/7 20:41 .<br>
 *
 * @author gary.fu
 */
@Data
public class User {
    private Long id;
    private String name;
    private Date birth;
    private String email;
}