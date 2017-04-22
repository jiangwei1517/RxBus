package com.jiangwei.rxbus;

import com.jiangwei.rxbus.annotation.GlobalTagsAnnotation;

/**
 * Created by jiangwei18
 */
@GlobalTagsAnnotation(
        packageName = "com.rxbus",
        className = "GlobalMessage",
        tags = {
                "GM1",
                "GM2"
        }
)
public @interface GlobalMessageDef {
}
