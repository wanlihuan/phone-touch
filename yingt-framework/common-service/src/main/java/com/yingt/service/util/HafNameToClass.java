package com.yingt.service.util;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by laihuan.wan on 2017/5/9 0009.
 */
public class HafNameToClass {

    /**
     * （反射机制）类名转化成实体类对象
     * @param className
     * @return
     */
    public static Object newInstanceClass(String className) {
        Object objectClass = null;
        try {
            objectClass =  Class.forName(className)
                    .getConstructor().newInstance();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return objectClass;
    }
}
