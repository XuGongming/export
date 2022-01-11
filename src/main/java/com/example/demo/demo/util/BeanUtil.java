package com.example.demo.demo.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xugm
 * @create 2022/1/10 18:12
 */
public class BeanUtil {
    public static Map<String, Object> objectToMap(Object obj) {
        Map<String, Object> map = new HashMap<String, Object>();
        Class<?> clazz = obj.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {//获取本身和父级对象
            Field[] fields = clazz.getDeclaredFields();//获取所有私有字段
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object value = null;
                try {
                    value = field.get(obj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                map.put(fieldName, value);
            }
        }
        return map;
    }
}
