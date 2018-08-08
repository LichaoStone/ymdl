package com.ym.frame.aspect.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分页注解
 * 用于控制数据分页
 * @作者 栗超
 * @时间 2018年5月25日 上午8:55:06
 * @说明
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PaginationController {
	
	/**
	 * 前端组件的类型
	 * @return
	 */
	String webTableType() default "dataTables";
}

