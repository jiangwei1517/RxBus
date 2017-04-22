package com.jiangwei.rxbus.compiler;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.lang.model.SourceVersion;

import com.google.auto.service.AutoService;
import com.jiangwei.rxbus.annotation.GlobalTag;
import com.jiangwei.rxbus.annotation.GlobalTagsAnnotation;

/**
 * author: jiangwei18 on 17/4/18 11:24 email: jiangwei18@baidu.com Hi: jwill金牛
 */
@AutoService(Processor.class)
public class GlobalTagProcessor extends TagProcessor {

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotationTypes = new HashSet<>();
        annotationTypes.add(GlobalTagsAnnotation.class.getCanonicalName());
        return annotationTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return GlobalTagsAnnotation.class;
    }

    @Override
    protected String getPackageName(Annotation a) {
        return ((GlobalTagsAnnotation) a).packageName();
    }

    @Override
    protected String getClassName(Annotation a) {
        return ((GlobalTagsAnnotation) a).className();
    }

    @Override
    protected String[] getTags(Annotation a) {
        return ((GlobalTagsAnnotation) a).tags();
    }

    @Override
    protected Class<? extends Annotation> getFieldTag() {
        return GlobalTag.class;
    }
}
