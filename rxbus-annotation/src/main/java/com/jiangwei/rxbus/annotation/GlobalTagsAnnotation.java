package com.jiangwei.rxbus.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * author: jiangwei18 on 17/4/18 10:52 email: jiangwei18@baidu.com Hi: jwill金牛
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface GlobalTagsAnnotation {
    String packageName() default "";

    String className();

    String[] tags();
}
