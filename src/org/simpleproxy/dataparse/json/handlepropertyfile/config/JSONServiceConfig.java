package org.simpleproxy.dataparse.json.handlepropertyfile.config;

/**
 * 功能：JSON bean的处理接口
 * @author lmb 
 * @version 1.0
 * @date 2017-6-5
 */
public interface JSONServiceConfig {

	/**
	 * 获取JSONServiceBean对象
	 * @param serviceName
	 * @return
	 */
	public JSONServiceBean getJSONServiceBean(String serviceName);
	/**
	 * 添加JSONServiceBean对象
	 * @param serviceName
	 * @return
	 */
	void addJSONServiceBean(JSONServiceBean jsonServiceBean);
	/**
	 * 移除JSONServiceBean对象
	 * @param serviceName
	 * @return
	 */
	void removeJSONServiceBean(String serviceName);
	/**
	 * 通过文件名加载配置文件
	 * @param serviceName
	 * @return
	 */
	void reload(String fileName) throws Exception;
}
