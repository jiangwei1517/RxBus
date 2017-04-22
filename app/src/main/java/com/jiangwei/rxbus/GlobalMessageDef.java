package com.jiangwei.rxbus;

import com.jiangwei.rxbus.annotation.GlobalTagsAnnotation;

/**
 * Created by Ju Yanwen on 2016/12/12.
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
