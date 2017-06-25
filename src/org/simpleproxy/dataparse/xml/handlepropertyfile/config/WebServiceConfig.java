package org.simpleproxy.dataparse.xml.handlepropertyfile.config;
/**
 * 功能：WebService bean的处理接口
 * @author lmb 
 * @version 1.0
 * @date 2017-6-5
 */
public interface WebServiceConfig {

	/** 通过名称获取WebServiceBean*/
	public WebServiceBean getWebServiceBean(String name);
	/** 添加WebServiceBean*/
	void addWebServiceBean(WebServiceBean webServiceBean); 
	/** 通过名称移除WebServiceBean*/
	void removeWebServiceBean(String name);
	/** 通过文件名来加载属性文件*/
	void reload(String fileName)throws Exception;
}
