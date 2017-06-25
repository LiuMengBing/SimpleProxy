package org.simpleproxy.dataparse.xml.handlepropertyfile;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import org.dom4j.Element;
import org.simpleproxy.dataparse.xml.handlepropertyfile.config.WebServiceBean;
import org.simpleproxy.dataparse.xml.handlepropertyfile.config.WebServiceConfig;
import org.simpleproxy.dataparse.xml.handlepropertyfile.config.WebServiceConfigImpl;
/**
 * 功能：根据属性文件组装xml报文
 * @author lmb 
 * @version 1.0
 * @date 2017-6-5
 */
public class InstallXmlByPropertyFile {

	static WebServiceConfig webServiceConfig = new WebServiceConfigImpl();
	
	/**
	 * 根据配置文件Bean和Map参数获取xml
	 * @param webServiceBean
	 * @param inParams
	 * @return
	 */
	public static String getXml(WebServiceBean webServiceBean,Map<String,Object> inParams){
		String requestXml = null;
		//获取不变应答包体
		String requestSameSpace = webServiceBean.getRequestSameSpace();
		//读取不变请求包体中的可变子包体
		List<String> variableSpaceList = getVariableSpaceList(requestSameSpace);
		//把参数传入可变子包体中,变为Map<variableSpace,String>字符串
		Map<String,String> VariableSpaceMap = getVariableSpaceMap(variableSpaceList,webServiceBean,inParams);
		//把可变包体加入到不变包体中
		requestXml = getXml(VariableSpaceMap,requestSameSpace,variableSpaceList);
		return requestXml;
	}
	
	/**
	 * 把可变包体加入到不变包体中
	 * @param VariableSpaceMap
	 * @param requestSameSpace
	 * @param variableSpaceList
	 * @return
	 */
	public static String getXml(Map<String,String> VariableSpaceMap,String requestSameSpace,List<String> variableSpaceList){
		//遍历所有可变子包体
		for (String string : variableSpaceList) {
			requestSameSpace = requestSameSpace.replace(":"+string+":", VariableSpaceMap.get(string));
		}
		return requestSameSpace;
	}
	
	/**
	 * 把参数传入可变子包体中,变为Map<variableSpace,String>字符串
	 * @param variableSpaceList
	 * @param service
	 * @param inParaMap
	 * @return
	 */
	public static Map<String,String> getVariableSpaceMap(List<String> variableSpaceList,WebServiceBean webServiceBean,Map<String, Object> inParaMap){
		Map<String,String> VariableSpaceMap = new HashMap<String, String>();
		for (String string : variableSpaceList) {
			//获取可变子包体字符串
			List<Element> variableSpaceSon = webServiceBean.getVariableSpaceSon();
			for (Element element : variableSpaceSon) {
				//找出当前可变节点
				if(string.contentEquals(element.attributeValue("name"))){
					//获取到了可变子包体字符串
					String temp  = element.getText().trim();
					//TODO 以后应该会加上判断，判断该参数是否应该加入到当前可变参数中去
					//参数分为2类，一个实在可变参数中直接写入，一个是要加入参数中
					//获取字段参数
					List<Map<String,String>> fields = webServiceBean.getFields();
					String parameterSpace = "";
					for (Map<String, String> map : fields) {
						//参数需要根据生成规则自动生成
						if(map.get("rule") != null){
							//添加字段生成规则
							StringBuffer sb = new StringBuffer();
							String ruleExp = null;
							String[] rules = map.get("rule").split(";");
							if (null != rules && rules.length > 0) {
								for(String r : rules){
									if(r.contains("String")){
										ruleExp = r.replace("String","").replace("[","").replace("]","");
										sb.append(ruleExp);
									}else if (r.contains("Date")) {
										ruleExp = r.replace("Date","").replace("[","").replace("]","");
										String ymd = org.simpleproxy.date.DateUtils.getDateStr(new Date(),ruleExp);
										sb.append(ymd);
									}else if (r.contains("Num")) {
										ruleExp = r.replace("Num","").replace("[","").replace("]","");
										Random random = new Random();
										for (int i = 0; i < Integer.valueOf(ruleExp); i++) {
											sb.append(random.nextInt(10));
										}
									}
								}
							}
							temp = temp.replace(":" + map.get("name") + ":",sb);
						}
						if(string.contentEquals(map.get("variableSpaceSon"))&&temp.indexOf(map.get("name"))<0){
							parameterSpace = parameterSpace+"<"+map.get("name")+">:"+map.get("name")+":</"+map.get("name")+">";
						}
					}
					temp = temp.replace(":parameterSpace:", parameterSpace);
					//遍历所有请求参数
					if(!(inParaMap==null || inParaMap.size()<1)){
						for (Entry<String, Object> entry : inParaMap.entrySet()) {  
							temp =temp.replace(":"+entry.getKey()+":", entry.getValue().toString());
						}
					}
					//判断可变子包体是否已字符串的形式放入
					if("String".contentEquals(element.attributeValue("type"))){
						temp = "<![CDATA["+temp.trim() +"]]>";
					}
					//把装入完成的可变字符串装入
					VariableSpaceMap.put(string, temp);
				}
			}
		}
		return VariableSpaceMap;
	}
	
	/**
	 * 对请求不变包体解析出可变包体的字符串集合
	 * @param requestSameSpace 不变包体字符串
	 * @return
	 */
	public static List<String> getVariableSpaceList(String requestSameSpace){
		List<String> variableSpaceList = new ArrayList<String>();
		int i = 1;//计数
		while(true){
			if(requestSameSpace.indexOf("variableSpace"+i)>0){
				variableSpaceList.add("variableSpace"+i);
				i++;
			}else{
				break;
			}
		}
		return variableSpaceList;
	}
	
	public static void main(String[] args) {
		WebServiceBean webServiceBean = webServiceConfig.getWebServiceBean("serviceName");
		Map<String,Object> inParams = null;
		String xml = getXml(webServiceBean,inParams);
		System.out.println(xml);
		/*
		<?xml version="1.0" encoding="UTF-8"?>
	    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://ws.eomtm.metarnet.com">
		   <soapenv:Header/>
		   <soapenv:Body>
		      <IsAlive>
		         <serialNo>201706291455303829</serialNo>
		         <serCaller>CRM</serCaller>
		      </IsAlive>
		   </soapenv:Body>
		</soapenv:Envelope>
		*/
	}
	
}
