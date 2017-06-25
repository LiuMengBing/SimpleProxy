package org.simpleproxy.dataparse.xml;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.json.JSONObject;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;

/**
 * 功能：XML解析工具类
 * @author lmb 
 * @version 1.0
 * @date 2017-6-5
 */
public class ParseXMLUtils {
	
    /**
     * 将xml格式响应报文解析为Json格式
     * @param responseXmlTemp
     * @return
     */
    public static String xml2Json(String responseXmlTemp) {
          Document doc = null;
          try {
                doc = DocumentHelper.parseText(responseXmlTemp);
          } catch (DocumentException e) {
          }
          Element rootElement = doc.getRootElement();
          Map<String,Object> mapXml = new HashMap<String,Object>();
          element2Map(mapXml,rootElement);
          String jsonXml = JSONObject.fromObject(mapXml).toString();
          System.out.println("Json >>> " + jsonXml);
          return jsonXml;
    }
    /**
     * 将xml格式响应报文解析为Map格式
     * @param responseXmlTemp
     * @param thirdXmlServiceBean
     * @return
     * @throws DocumentException
     */
    public static Map<String, Object> xml2map(String responseXmlTemp) {
          Document doc = null;
          try {
                doc = DocumentHelper.parseText(responseXmlTemp);
          } catch (DocumentException e) {
          }
          Element rootElement = doc.getRootElement();
          Map<String,Object> mapXml = new HashMap<String,Object>();
          element2Map(mapXml,rootElement);
          System.out.println("Map >>> " + mapXml);
          return mapXml;
    }
    /**
     * 使用递归调用将多层级xml转为map
     * @param map
     * @param rootElement
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static void element2Map(Map<String, Object> map, Element rootElement) {
          
          //获得当前节点的子节点
          List<Element> elements = rootElement.elements();
          if (elements.size() == 0) {
                //没有子节点说明当前节点是叶子节点，直接取值
                map.put(rootElement.getName(),rootElement.getText());
          }else if (elements.size() == 1) {
                //只有一个子节点说明不用考虑list的情况，继续递归
                Map<String,Object> tempMap = new HashMap<String,Object>();
                element2Map(tempMap,elements.get(0));
                map.put(rootElement.getName(),tempMap);
          }else {
                //多个子节点的话就要考虑list的情况了，特别是当多个子节点有名称相同的字段时
                Map<String,Object> tempMap = new HashMap<String,Object>();
                for (Element element : elements) {
                      tempMap.put(element.getName(),null);
                }
                Set<String> keySet = tempMap.keySet();
                for (String string : keySet) {
                      Namespace namespace = elements.get(0).getNamespace();
                      List<Element> sameElements = rootElement.elements(new QName(string,namespace));
                      //如果同名的数目大于1则表示要构建list
                      if (sameElements.size() > 1) {
                            List<Map> list = new ArrayList<Map>();
                            for(Element element : sameElements){
                                  Map<String,Object> sameTempMap = new HashMap<String,Object>();
                                  element2Map(sameTempMap,element);
                                  list.add(sameTempMap);
                            }
                            map.put(string,list);
                      }else {
                            //同名的数量不大于1直接递归
                            Map<String,Object> sameTempMap = new HashMap<String,Object>();
                            element2Map(sameTempMap,sameElements.get(0));
                            map.put(string,sameTempMap);
                      }
                }
          }
    }
    
    public static void main(String[] args) { 
        // 获取一个xml文件 
        String textFromFile = ReadXMLUtils.XmlToString("E://test.xml","UTF-8");
        System.out.println("xml >>> " + textFromFile);
        //将xml解析为Map
        Map resultMap = xml2map(textFromFile);
        //将xml解析为Json
        String resultJson = xml2Json(textFromFile);
    }
    
    /*  xml >>>
     	<?xml version="1.0" encoding="UTF-8"?>
		<ROOT>  
		      <HEAD>  
		            <ORIGIN_DOMAIN>kefuxitongbianma</ORIGIN_DOMAIN>  
		            <HOME_DOMAIN>CUGCRM</HOME_DOMAIN>  
		            <ACTION_CODE>1</ACTION_CODE>  
		            <BUSI_CODE>QUERYCUST</BUSI_CODE>  
		            <TRANS_ID>20160220160635123456</TRANS_ID>  
		            <RET_CODE>0000</RET_CODE>  
		            <RET_MSG>success</RET_MSG>  
		      </HEAD>  
		      <BODY>  
		            <TOTAL_RECORDS>20</TOTAL_RECORDS>  
		            <TOTAL_PAGE>10</TOTAL_PAGE>  
		            <CURRENT_PAGE>1</CURRENT_PAGE>  
		            <CUSTINFOLIST>  
		                  <CUSTINFO>  
		                        <CUST_TYPE>001</CUST_TYPE>  
		                        <VIP_FLAG>true</VIP_FLAG>  
		                  </CUSTINFO>  
		                  <CUSTINFO>  
		                        <CUST_TYPE>002</CUST_TYPE>  
		                        <VIP_FLAG>false</VIP_FLAG>  
		                  </CUSTINFO>  
		                  <CUSTINFO>  
		                        <CUST_TYPE>003</CUST_TYPE>  
		                        <VIP_FLAG>false</VIP_FLAG>  
		                  </CUSTINFO>  
		            </CUSTINFOLIST>  
		      </BODY>  
		</ROOT>
		
		Map >>> {BODY={TOTAL_RECORDS={TOTAL_RECORDS=20}, CUSTINFOLIST={CUSTINFO=[{CUST_TYPE={CUST_TYPE=001}, VIP_FLAG={VIP_FLAG=true}}, {CUST_TYPE={CUST_TYPE=002}, VIP_FLAG={VIP_FLAG=false}}, {CUST_TYPE={CUST_TYPE=003}, VIP_FLAG={VIP_FLAG=false}}]}, TOTAL_PAGE={TOTAL_PAGE=10}, CURRENT_PAGE={CURRENT_PAGE=1}}, HEAD={ACTION_CODE={ACTION_CODE=1}, ORIGIN_DOMAIN={ORIGIN_DOMAIN=kefuxitongbianma}, BUSI_CODE={BUSI_CODE=QUERYCUST}, HOME_DOMAIN={HOME_DOMAIN=CUGCRM}, TRANS_ID={TRANS_ID=20160220160635123456}, RET_MSG={RET_MSG=success}, RET_CODE={RET_CODE=0000}}}
		Json >>> {"BODY":{"TOTAL_RECORDS":{"TOTAL_RECORDS":"20"},"CUSTINFOLIST":{"CUSTINFO":[{"CUST_TYPE":{"CUST_TYPE":"001"},"VIP_FLAG":{"VIP_FLAG":"true"}},{"CUST_TYPE":{"CUST_TYPE":"002"},"VIP_FLAG":{"VIP_FLAG":"false"}},{"CUST_TYPE":{"CUST_TYPE":"003"},"VIP_FLAG":{"VIP_FLAG":"false"}}]},"TOTAL_PAGE":{"TOTAL_PAGE":"10"},"CURRENT_PAGE":{"CURRENT_PAGE":"1"}},"HEAD":{"ACTION_CODE":{"ACTION_CODE":"1"},"ORIGIN_DOMAIN":{"ORIGIN_DOMAIN":"kefuxitongbianma"},"BUSI_CODE":{"BUSI_CODE":"QUERYCUST"},"HOME_DOMAIN":{"HOME_DOMAIN":"CUGCRM"},"TRANS_ID":{"TRANS_ID":"20160220160635123456"},"RET_MSG":{"RET_MSG":"success"},"RET_CODE":{"RET_CODE":"0000"}}}
     */

}
