package com.ym.test;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


/**
 * 测试
 * @作者 栗超
 * @时间 2018年8月8日 上午9:36:08
 * @说明
 */
@Controller
@RequestMapping("/test")
public class TestController {
	
	@RequestMapping("/toList")
	public ModelAndView toList(HttpServletRequest request) {
		
		Map<String, Object> model = new HashMap<String, Object>();
		return new ModelAndView("/test/test", model);
	}
}
