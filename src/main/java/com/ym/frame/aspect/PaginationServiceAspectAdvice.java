package com.ym.frame.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.ym.frame.aspect.annotation.PaginationService;
import com.ym.frame.page.PageContext;


@Aspect
@Component
public class PaginationServiceAspectAdvice {

	@Around(value = "com.ym.frame.aspect.pointcut.SystemArchitecture.globalServiceLayer() "
			+ "&& @annotation(paginationService)")
	public Object paginationService(ProceedingJoinPoint jp,PaginationService paginationService) throws Throwable {
		PageContext context = PageContext.getContext();
		context.setPaginationService(true);
			
		Object o = null;
		try{
			o = jp.proceed();
		}catch(Throwable t){
			throw t;
		}finally{
			context.setPaginationService(false);
		}
		return o;
	}
}
