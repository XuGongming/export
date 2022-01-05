package com.example.demo.demo.dto;

import com.example.demo.demo.common.ExportField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class UserVO {
    private Long id;
    @ExportField(fieldName = "名字")
    private String nickName;
    @ExportField(fieldName = "登录账号")
    private String loginName;

    private Byte sex;
    @ExportField(fieldName = "联系电话")
    private String mobile;


    private Long cuserId;
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime ctime;

    private Long uuserId;
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExportField(fieldName = "更新时间")
    private LocalDateTime utime;


}
