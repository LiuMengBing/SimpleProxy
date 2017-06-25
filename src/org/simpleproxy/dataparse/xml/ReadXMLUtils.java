package org.simpleproxy.dataparse.xml;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * 功能：读取XML文件的工具类
 * @author lmb 
 * @version 1.0
 * @date 2017-6-5
 *
 */
public class ReadXMLUtils {

	 /** 
     * 加载xml文件 
     * @return Document 
     */  
    public static Document load(String url){  
	      Document document=null;   
	      try {   
	          SAXBuilder reader = new SAXBuilder();    
	          document=reader.build(new File(url));   
	     } catch (Exception e) {   
	          e.printStackTrace();   
	     }   
	      return document;  
    }  
      
    /** 
     * 将xml文件转换为String串 
     * @return 
     */  
    public static String XmlToString(String url,String decode){  
          Document document=null;   
	      document=load(url);   
	         
	      Format format =Format.getPrettyFormat();       
	      format.setEncoding(decode);//设置编码格式    
	         
	      StringWriter out=null; //输出对象   
	      String sReturn =""; //输出字符串   
	      XMLOutputter outputter =new XMLOutputter();    
	      out=new StringWriter();    
	      try {   
	         outputter.output(document,out);   
	      } catch (IOException e) {   
	         e.printStackTrace();   
	      }    
	      sReturn=out.toString();    
	      return sReturn;   
    }   
    public static void main(String[] args) {
    	System.out.println(XmlToString("E://test.xml","UTF-8"));
	}
}
