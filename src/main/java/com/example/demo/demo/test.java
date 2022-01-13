package com.example.demo.demo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xugm
 * @create 2022/1/6 14:03
 */
public class test {
    public static void main(String[] args) throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Map<String, String> headers = new HashMap(2);
        headers.put("1", "11");
        headers.put("2", "22");
        Class<?> mapType = headers.getClass();
        //获取指定属性，也可以调用getDeclaredFields()方法获取属性数组
        Field threshold =  mapType.getDeclaredField("threshold");
        //将目标属性设置为可以访问
        threshold.setAccessible(true);
        //获取指定方法，因为HashMap没有容量这个属性，但是capacity方法会返回容量值
        Method capacity = mapType.getDeclaredMethod("capacity");
        //设置目标方法为可访问
        capacity.setAccessible(true);
        //打印刚初始化的HashMap的容量、阈值和元素数量
        System.out.println("容量："+capacity.invoke(headers)+"    阈值："+threshold.get(headers)+"    元素数量："+headers.size());


    }
}
