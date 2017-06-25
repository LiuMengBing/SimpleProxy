package org.simpleproxy.dataparse.xml.handlepropertyfile.config;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
/**
 * 功能：针对webservice.xml的javabean
 * @author lmb 
 * @version 1.0
 * @date 2017-6-5
 */
public class WebServiceBean implements Serializable{

	private static final long serialVersionUID = 1L;
	/**服务名称*/
	private String name;
	/**返回报文的根节点*/
	private String responseRoot;
	/**请求地址*/
	private String wsdl;
	/**返回码*/
	private String rspcode;
	/**返回描述*/
	private String rspdesc;
	/**命名空间*/
	private String targetNamespace;
	/**不变包体*/
	private String requestSameSpace;
	/**可变包体*/
	private String variableSpace;
	/**处理方法（服务端）*/
	private String method;
	/**服务描述*/
	private String desc;
	/**可变包体子集合*/
	private List<Element> variableSpaceSon;
	/**service配置集*/
	private List<Map<String,String>> fields;

	public WebServiceBean(){}
	
	public WebServiceBean(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getResponseRoot() {
		return responseRoot;
	}

	public void setResponseRoot(String responseRoot) {
		this.responseRoot = responseRoot;
	}

	public String getWsdl() {
		return wsdl;
	}

	public void setWsdl(String wsdl) {
		this.wsdl = wsdl;
	}

	public String getRspcode() {
		return rspcode;
	}

	public void setRspcode(String rspcode) {
		this.rspcode = rspcode;
	}

	public String getRspdesc() {
		return rspdesc;
	}

	public void setRspdesc(String rspdesc) {
		this.rspdesc = rspdesc;
	}

	public String getTargetNamespace() {
		return targetNamespace;
	}

	public void setTargetNamespace(String targetNamespace) {
		this.targetNamespace = targetNamespace;
	}

	public String getRequestSameSpace() {
		return requestSameSpace;
	}

	public void setRequestSameSpace(String requestSameSpace) {
		this.requestSameSpace = requestSameSpace;
	}

	public String getVariableSpace() {
		return variableSpace;
	}

	public void setVariableSpace(String variableSpace) {
		this.variableSpace = variableSpace;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<Element> getVariableSpaceSon() {
		return variableSpaceSon;
	}

	public void setVariableSpaceSon(List<Element> variableSpaceSon) {
		this.variableSpaceSon = variableSpaceSon;
	}

	public List<Map<String, String>> getFields() {
		return fields;
	}

	public void setFields(List<Map<String, String>> fields) {
		this.fields = fields;
	}
	
}
