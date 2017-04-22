package com.jiangwei.rxbus.compiler;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

/**
 * author: jiangwei18 on 17/4/18 14:11 email: jiangwei18@baidu.com Hi: jwill金牛
 */

public abstract class TagProcessor extends AbstractProcessor {

    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnvironment.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(getAnnotationClass());
        for (Element element : elements) {
            Annotation annotation = element.getAnnotation(getAnnotationClass());
            String packageName = getPackageName(annotation);
            String className = getClassName(annotation);
            String[] tags = getTags(annotation);
            try {
                if (element.getKind() == ElementKind.ANNOTATION_TYPE) {
                    if (packageName == null || packageName.trim().length() == 0) {
                        do {
                            element = element.getEnclosingElement();
                        } while (element.getKind() != ElementKind.PACKAGE);
                        packageName = ((PackageElement) element).getQualifiedName().toString();
                    }
                } else {
                    throw new IllegalStateException("TagsAnnotation can only be used on annotation");
                }
            } catch (Exception e) {
                if (messager != null) {
                    messager.printMessage(Diagnostic.Kind.ERROR, "TagsAnnotation can only be used on annotation",
                            element);
                } else {
                    e.printStackTrace();
                }
            }
            if (className == null || className.trim().length() == 0) {
                className = "RxBus" + annotation.annotationType().getSimpleName();
            }
            int startId = requestStartId(tags.length);
            TypeSpec.Builder builder = TypeSpec.classBuilder(className).addModifiers(Modifier.PUBLIC, Modifier.FINAL);
            for (String tag : tags) {
                    builder.addField(FieldSpec.builder(TypeName.INT, tag).addAnnotation(getFieldTag())
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                        .initializer(String.valueOf(startId++)).build());
            }
            builder.addMethod(
                    MethodSpec.methodBuilder("contains").addParameter(TypeName.INT, "tag").returns(TypeName.BOOLEAN)
                            .addStatement("return tag >= $L && tag <= $L", tags[0], tags[tags.length - 1])
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC).build());
            JavaFile javaFile = JavaFile.builder(packageName, builder.build()).build();
            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public int requestStartId(int size) {
        int startId = 0;
        RandomAccessFile file = null;
        ReentrantLock reentrantLock = new ReentrantLock();
        try {
            reentrantLock.lock();
            File buildFolder = new File("build");
            if (!buildFolder.exists()) {
                buildFolder.mkdir();
            }
            file = new RandomAccessFile(new File("build/rxbus-id.tmp"), "rw");
            try {
                startId = file.readInt();
            } catch (EOFException e) {
                // Continue, file is empty
            }
            file.seek(0);
            file.writeInt(startId + size);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            reentrantLock.unlock();
        }
        return startId;
    }

    protected abstract Class<? extends Annotation> getAnnotationClass();

    protected abstract String getPackageName(Annotation a);

    protected abstract String getClassName(Annotation a);

    protected abstract String[] getTags(Annotation a);

    protected abstract Class<? extends Annotation> getFieldTag();
}
