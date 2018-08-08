package com.ym.frame;


import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.alibaba.fastjson.JSONObject;

/**
 * controller基础类
 * @作者 栗超
 * @时间 2018年5月12日 上午8:55:58
 * @说明
 */
public class BaseController extends Constants{
	private static final Logger logger = Logger.getLogger(BaseController.class);
	 
	 @InitBinder
	 public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
			if (request.getCharacterEncoding() == null || !request.getCharacterEncoding().toLowerCase().equals("utf-8")) {
				logger.info("getCharacterEncoding:" + request.getCharacterEncoding() + ":",new Exception("打印堆栈"));
			}
			
			request.setCharacterEncoding("utf-8");
			binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
	 }
	 
	 /**
	  * 发送json
	  * @param json
	  * @param response
	  */
	 protected static void sendJson(String json, HttpServletResponse response){
		  try {
				logger.info("json===>\n" + json);
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter out;
				
				out = response.getWriter();
				out.print(json);
				out.flush();
				out.close();
			} catch (IOException e) {
				logger.error("BaseController.sendJson出错:",e);
			}
      }
	  
}
