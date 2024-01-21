package com.example.utils;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {
    private BeanCopyUtils(){

    }

    // 第一个参数是你传过来的实体类，第二个就是要通过反射的方式创建vo对象了
    // 单个对象的拷贝
    public static <V> V copyBean(Object source,Class<V> clazz) {  // 需要使用一个方法泛型
        // 创建目标对象
        V vo = null;
        try {
            vo = clazz.newInstance();
            // 实现属性拷贝
            BeanUtils.copyProperties(source, vo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 返回结果
        return vo;  // alt enter 提升作用域
    }

    // 拷贝bean的集合
    public static <O,V> List<V> copyBeanList(List<O> list ,Class<V> clazz){

        return list.stream()
                .map(o -> copyBean(o,clazz))
                .collect(Collectors.toList());
    }

}
