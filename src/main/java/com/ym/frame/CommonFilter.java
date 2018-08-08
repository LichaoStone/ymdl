package com.ym.frame;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;


/**
 * 全局过滤器
 * @作者 栗超
 * @时间 2018年5月12日 上午8:50:45
 * @说明
 */
public class CommonFilter extends BaseController implements Filter{
	private static final Logger logger = Logger.getLogger(CommonFilter.class);
	
	public static void main(String[] args) {
	}
	
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		HttpSession session = ((HttpServletRequest) request).getSession();
		logger.info("commonFilter--session"+session.getId()+":START...");
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		//请求url
		String requestUrl = httpRequest.getRequestURI();
		String queryString=httpRequest.getQueryString();
		logger.info("请求url:"+requestUrl);
		logger.info("查询参数:"+queryString);
	}

	public void init(FilterConfig config) throws ServletException {
	}
}
