package com.example.demo.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.demo.enumeration.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@TableName("user")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User implements Serializable {
    private Integer id;
    private String name;
    private Integer grade;
    private UserStatus status;
}
