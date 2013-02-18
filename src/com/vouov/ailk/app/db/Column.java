package com.vouov.ailk.app.db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User: yuml
 * Date: 13-2-18
 * Time: 下午11:27
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD) //此注解只能用来对成员变量进行注解
public @interface Column {
    String value();
}
