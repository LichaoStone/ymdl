package com.ym.frame.aspect;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * @description  导出excel
 * 传入参数 obj=  {"tbody":[{data:".."},...],
 * 				 "thead",[{"name":"用户名称","data":"USERNAME","width":"133","dimColumn":"0"},
 * 				         {"name":"用户组","data":"GROUP_NAME","width":"108","dimColumn":"0"},...],
 * 				 "title","导出我的excel"}
 * @param JSONObject obj
 * @return HSSFWorkbook hfb
 * @author SmartMan
 *
 */
public class DatatablesExcel{
	public static HSSFWorkbook creatExcel(Map obj) throws JSONException, UnsupportedEncodingException{
		//获取表体数据
		List bodyList = (List) obj.get("tbody");
		//获取表头数据
		JSONArray headArr = (JSONArray) obj.get("thead");
		//表格名称
		String title = "";
		if(obj.containsKey("title")){
				title = obj.get("title")+"";
		}
		//第一步，创建一个webbook，对应一个Excel文件     
		HSSFWorkbook wb = new HSSFWorkbook();      
		//第二步，在webbook中添加一个sheet,对应Excel文件中的sheet      
		HSSFSheet sheet = wb.createSheet("sheet1");     
		//第四步，创建单元格，并设置值表头样式  设置表头居中 
		HSSFCellStyle titlestyle = wb.createCellStyle(); 
		HSSFCellStyle style = wb.createCellStyle();
		titlestyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); 
		titlestyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		titlestyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		titlestyle.setBorderTop((short) 1);
		titlestyle.setBorderRight((short) 1);
		titlestyle.setBorderBottom((short) 1);
		titlestyle.setBorderLeft((short) 1);
		style.setBorderTop((short) 1);
		style.setBorderRight((short) 1);
		style.setBorderBottom((short) 1);
		style.setBorderLeft((short) 1);
		style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		
		//单元格行创建
		int rowNum = 0;
		//第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
		HSSFRow row = sheet.createRow((int)rowNum);
		//表格的标题
		if( !"".equals(title) ){
			
			HSSFCell firstCell = row.createCell(rowNum);
			rowNum++;
			firstCell.setCellValue(title);
			firstCell.setCellValue(title);
			firstCell.setCellStyle(titlestyle); 
			CellRangeAddress region=new CellRangeAddress(0,0,0,headArr.length()-1);
			sheet.addMergedRegion(region);
		}
		
		
		
		//循环遍历表头内容并将值放到excel表格的表头中
		ArrayList<String> wordOrNum = new ArrayList<String>();//记录一列是文字还是数值
		row = sheet.createRow(rowNum);
		rowNum++;
		for(int i=0,j=headArr.length();i<j;i++){
			JSONObject trObj = headArr.getJSONObject(i);       
			if(trObj.getInt("dimColumn") == 1){
				wordOrNum.add("word");
			}else{
				wordOrNum.add("number");
			}
			
			HSSFCell cell = row.createCell(i);  
			cell.setCellValue(trObj.getString("name")); 
			cell.setCellStyle(titlestyle); 
			if(trObj.has("width")){
				int columnWidth=(int)Double.parseDouble(trObj.getString("width"))/7*256;
				if(columnWidth<256*256&&columnWidth>0){
					sheet.setColumnWidth(i,columnWidth);
				}
			}else if(trObj.has("minWidth")){
				int columnWidth=(int)Double.parseDouble(trObj.getString("minWidth"))/7*256;
				if(columnWidth<256*256&&columnWidth>0){
					sheet.setColumnWidth(i,columnWidth);
				}
			}
		}
		//循环遍历表格数据
		for(int i=0;i<bodyList.size();i++){
			Map rowMap = (Map) bodyList.get(i);
			row = sheet.createRow(rowNum);
			rowNum++;
			for(int m=0,n=headArr.length();m<n;m++){
				JSONObject trObj = headArr.getJSONObject(m);
				HSSFCell tdcell = row.createCell(m); 
				String cellType=wordOrNum.get(m);
				String str="";//定义一个字符串作为放入单元格的值
				//判断是否存在formatter属性值
				if(trObj.has("formatter")){
					if("enum".equals(trObj.getString("formatter"))){
						if(rowMap.containsKey("field")){
						JSONArray sourceArr=trObj.getJSONArray("source");//获取source属性的value值
						String[] org=(rowMap.get(trObj.getString("field"))+"").split(",");//获得枚举类型的单元格的数据存入到数组中去
						for(int c=0,d=org.length;c<d;c++){//循环遍历比较数组中的值，拼字符串str
							for(int e=0,f=sourceArr.length();e<f;e++){
								JSONObject sourceObj = sourceArr.getJSONObject(e);
								if(org[c].equals(sourceObj.getString("key"))){
									if(c>0){//通过判断数组当中数的个数从而在value值之间加逗号
										str+=","+sourceObj.getString("value");
									}else{
										str+=sourceObj.getString("value");
									}
								}	
							}		
						}
						}else{
							str="";
						}
						setValue(cellType,str,tdcell);
					}else if("yesNo".equals(trObj.getString("formatter"))){
						if(rowMap.containsKey("field")){
							if("0".equals(rowMap.containsKey("field"))){
								tdcell.setCellValue("否");
							}else{
								tdcell.setCellValue("是");
							}
						}else{
							tdcell.setCellValue("");
						}
						
					}else{
						if(rowMap.containsKey(trObj.getString("field"))){
							str=rowMap.get(trObj.getString("field"))+"";
						}else{
							str="";
						}
						setValue(cellType,str,tdcell);
					}
				}else{
					str = rowMap.get(trObj.getString("data"))+"";
					setValue(cellType,str,tdcell);
				}
				tdcell.setCellStyle(style);
			}	
			
		}
		
		return wb;
	}
	
	private static String setValue(String cellType,String str,HSSFCell tdcell) throws UnsupportedEncodingException{
		if("number".equals(cellType)){
			Pattern pattern = Pattern.compile("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
			Matcher isNum = pattern.matcher(str);
			if (!str.equals("")&&isNum.matches()) {
				Double num = Double.parseDouble(str);
				tdcell.setCellValue(num);
			}else{
				tdcell.setCellValue(URLDecoder.decode(str.replaceAll("%", "%25"),"utf-8"));
			}
		}else{
			tdcell.setCellValue(URLDecoder.decode(str.replaceAll("%", "%25"),"utf-8"));
		}
		return null;	
	}
}