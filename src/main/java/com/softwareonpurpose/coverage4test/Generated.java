package com.softwareonpurpose.coverage4test;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.METHOD;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(METHOD)
public @interface Generated {
}
