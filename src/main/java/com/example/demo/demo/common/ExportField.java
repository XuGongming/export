package com.example.demo.demo.common;

import java.lang.annotation.*;


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ExportField {
    String fieldName() ;
    int width() default 1;
    int height() default 1;
}