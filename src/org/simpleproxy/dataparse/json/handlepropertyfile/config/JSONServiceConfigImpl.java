package org.simpleproxy.dataparse.json.handlepropertyfile.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.simpleproxy.dataparse.ResourceUtils;
/**
 * 功能：JSON bean的处理接口实现类
 * @author lmb 
 * @version 1.0
 * @date 2017-6-5
 */
public class JSONServiceConfigImpl implements JSONServiceConfig {

	protected Map<String,JSONServiceBean> jsonServiceBeanCache = new HashMap<String,JSONServiceBean>();
	
	private String jsonLocation;
	
	public Map<String, JSONServiceBean> getJsonServiceBeanCache() {
		return jsonServiceBeanCache;
	}

	public void setJsonServiceBeanCache(
			Map<String, JSONServiceBean> jsonServiceBeanCache) {
		this.jsonServiceBeanCache = jsonServiceBeanCache;
	}

	public String getJsonLocation() {
		return jsonLocation;
	}

	public void setJsonLocation(String jsonLocation) {
		this.jsonLocation = jsonLocation;
	}

	@Override
	public JSONServiceBean getJSONServiceBean(String serviceName) {
		return jsonServiceBeanCache.get(serviceName);
	}

	@Override
	public void addJSONServiceBean(JSONServiceBean jsonServiceBean) {
		jsonServiceBeanCache.put(jsonServiceBean.getServiceName(),jsonServiceBean);
	}

	@Override
	public void removeJSONServiceBean(String serviceName) {
		jsonServiceBeanCache.remove(serviceName);
	}

	/**
	 * Spring实例化该类时会执行该方法加载相关的配置文件（也可以使用InitializingBean的afterPropertiesSet()方法）
	 * http://blog.csdn.net/lmb55/article/details/70880067
	 * <beans>  
	 * 		<bean name ="webServiceConfig" class ="org.simpleproxy.dataparse.xml.handlepropertyfile.config.WebServiceConfigImpl" init-method ="init"></bean>  
	 * </beans>  
	 * @throws Exception
	 */
	public void init() throws Exception{
		jsonLocation = jsonLocation == null ? "jsonservice.xml" : jsonLocation;
		File file = new File(ResourceUtils.getPath(jsonLocation));
		if (!file.exists()) {
			file.mkdir();
		} 
		reload(jsonLocation);
	}
	
	@Override
	public void reload(String fileName) throws Exception {
		fileName = fileName.startsWith("/") ? fileName.substring(1) : fileName;
//		File file = org.springframework.util.ResourceUtils.getFile("classpath:" + fileName);
		File file = null;
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File configFile : files) {
				reload(configFile);
			}
		} else {
			reload(file);
		}
	}

	/**
	 * 通过File对象来加载配置文件
	 * @param file
	 * @throws Exception
	 */
	public void reload(File file) throws Exception {
		InputStream source = new FileInputStream(file);
		SAXReader reader = new SAXReader();
		Document doc = reader.read(source);

		Element root = doc.getRootElement();
		
		Element appKeyEle = root.element("appKey");
		String appKey = null;
		if (null != appKeyEle) {
			appKey = appKeyEle.getTextTrim();
		}
		
		Element appIdEle = root.element("appId");
		String appId = null;
		if (null != appIdEle) {
			appId = appIdEle.getTextTrim();
		}

		Element versionEle = root.element("version");
		String version = null;
		if (null != versionEle) {
			version = versionEle.getTextTrim();
		}
		
		for (Iterator<Element> services = root.elementIterator("service"); services
				.hasNext();) {
			Element element = services.next();
			String serviceName = element.attributeValue("name");
			JSONServiceBean thirdJsonServiceBean = new JSONServiceBean(serviceName);
			//设置公共参数
			thirdJsonServiceBean.setAppKey(appKey);
			thirdJsonServiceBean.setAppId(appId);
			thirdJsonServiceBean.setVersion(version);
			//设置service参数
			thirdJsonServiceBean.setDesc(element.attributeValue("desc"));
			thirdJsonServiceBean.setUrl(element.attributeValue("url"));
			thirdJsonServiceBean.setConnectionTimeout(element
					.attributeValue("connectionTimeout"));
			thirdJsonServiceBean.setSocketTimeout(element
					.attributeValue("socketTimeout"));
			
			Iterator eleIter=element.elementIterator();
			Map<String,JSONFieldBean> params=new HashMap<String,JSONFieldBean>();
			JSONFieldBean jsonFieldBean=null;
			while(eleIter.hasNext()){
				Element ele=(Element)eleIter.next();
				jsonFieldBean=new JSONFieldBean();
				jsonFieldBean.setName(ele.getName());
				jsonFieldBean.setValue(ele.getTextTrim());
				jsonFieldBean.setRule(ele.attributeValue("rule"));
				jsonFieldBean.setDefautVal(ele.attributeValue("defaultVal"));
				jsonFieldBean.setReqSerialNo(ele.attributeValue("reqSerialNo"));
				params.put(ele.getName(), jsonFieldBean);
			}
			thirdJsonServiceBean.setParams(params);
			
			if (this.getJSONServiceBean(serviceName) != null) {
				throw new Exception("请检查配置文件中是否重名");
			} else {
				this.addJSONServiceBean(thirdJsonServiceBean);
			}
		}
	}

}
