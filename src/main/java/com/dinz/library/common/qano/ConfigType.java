/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dinz.library.common.qano;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 *
 * @author DinzeniLL
 */
@Target(ElementType.FIELD)
@Retention(RUNTIME)
public @interface ConfigType {
	Class<?> targetClass() default Object.class;

	String[] fields() default {};
}
