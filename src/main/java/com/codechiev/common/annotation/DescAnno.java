package com.codechiev.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface DescAnno {
    String value();
}
