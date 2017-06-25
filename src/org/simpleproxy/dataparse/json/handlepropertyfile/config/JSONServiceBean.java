package org.simpleproxy.dataparse.json.handlepropertyfile.config;

import java.io.Serializable;
import java.util.Map;
/**
 * 功能：针对jsonservice.xml的javabean
 * @author lmb 
 * @version 1.0
 * @date 2017-6-5
 */
public class JSONServiceBean implements Serializable{

	private static final long serialVersionUID = 1L;
	/** 公共参数*/
	private String appKey;
	private String appId;
	private String version;
	/** service信息 */
	private String serviceName;
	private String url;
	private String connectionTimeout;
	private String socketTimeout;
	private String desc;
	private Map<String, JSONFieldBean> params;
	
	public JSONServiceBean() {
	}

	public JSONServiceBean(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(String connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public String getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(String socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Map<String, JSONFieldBean> getParams() {
		return params;
	}

	public void setParams(Map<String, JSONFieldBean> params) {
		this.params = params;
	}
	
}
