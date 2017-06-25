package org.simpleproxy.dataparse.xml.handlepropertyfile.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.simpleproxy.dataparse.ResourceUtils;
/**
 * 功能：WebService bean的处理接口的实现类
 * @author lmb 
 * @version 1.0
 * @date 2017-6-5
 */
public class WebServiceConfigImpl implements WebServiceConfig {

	protected  Map<String,WebServiceBean> webServiceCache = new HashMap<String,WebServiceBean>();
	
	private String wsLocation;

	public Map<String, WebServiceBean> getWebServiceCache() {
		return webServiceCache;
	}

	public void setWebServiceCache(Map<String, WebServiceBean> webServiceCache) {
		this.webServiceCache = webServiceCache;
	}

	public String getWsLocation() {
		return wsLocation;
	}

	public void setWsLocation(String wsLocation) {
		this.wsLocation = wsLocation;
	}

	@Override
	public WebServiceBean getWebServiceBean(String name) {
		return webServiceCache.get(name);
	}

	@Override
	public void addWebServiceBean(WebServiceBean webServiceBean) {
		webServiceCache.put(webServiceBean.getName(),webServiceBean);		
	}

	@Override
	public void removeWebServiceBean(String name) {
		webServiceCache.remove(name);		
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
		wsLocation = wsLocation == null ? "webservice.xml" : wsLocation;
		File file = new File(ResourceUtils.getPath(wsLocation));
		if (!file.exists()) {
			file.mkdir();
		} 
		reload(wsLocation);
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
		}else{
			reload(file);
		}
	}

	/**
	 * 通过File对象来加载属性文件
	 * @param file
	 * @throws Exception
	 */
	public void reload(File file) throws Exception {
		InputStream source = new FileInputStream(file);
		SAXReader reader = new SAXReader();
		Document doc = reader.read(source);
		Element root = doc.getRootElement();
		Element requestSameSpaceXml = root.element("requestSameSpace");//读取不变请求包体
		Element variableSpaceXml = root.element("variableSpace");//读取可变包体
		List<Element> variableSpaceSonXml = variableSpaceXml.selectNodes("variableSpaceSon");//读取可变包体子集
		String targetNamespace = "";
		if(root.element("targetNamespace")!=null){
			targetNamespace = root.element("targetNamespace").getText();//读取targetNamespace命名空间
		}
		String wsdl = "";
		if(root.element("wsdl")!=null){
			wsdl = root.element("wsdl").getText();//读取wsdl
		}
		String requestSameSpace = null;
		String variableSpace = null;
		if (requestSameSpaceXml != null){
			requestSameSpace = requestSameSpaceXml.getText();
		}
		for ( Iterator<Element> services = root.elementIterator("service");services.hasNext();) {
			//读取service节点
			Element element = services.next();
			String name = element.attributeValue("name");
			/**如果service配置了targetNamespace,则此配置优先*/
			List<Map<String,String>> Fields = new ArrayList<Map<String,String>>();//读取Field集合
			List<Element> tempfields = element.selectNodes("field");
			for (Element field : tempfields) {
				Map<String, String> fieldMap = new HashMap<String, String>();
				fieldMap.put("name", field.attributeValue("name"));
				fieldMap.put("rule", field.attributeValue("rule"));
				fieldMap.put("variableSpaceSon", field.attributeValue("variableSpaceSon"));
				fieldMap.put("desc", field.attributeValue("desc"));
				Fields.add(fieldMap);
			}
			WebServiceBean webServiceBean = new WebServiceBean(name);
			webServiceBean.setRequestSameSpace(requestSameSpace);
			webServiceBean.setVariableSpace(variableSpace);
			webServiceBean.setFields(Fields);
			webServiceBean.setWsdl(wsdl);
			webServiceBean.setTargetNamespace(targetNamespace);
			webServiceBean.setVariableSpaceSon(variableSpaceSonXml);
			webServiceBean.setMethod(element.attributeValue("method"));
			webServiceBean.setResponseRoot(element.attributeValue("responseRoot"));
			webServiceBean.setDesc(element.attributeValue("desc"));
			webServiceBean.setRspcode(element.attributeValue("rspcode"));
			webServiceBean.setRspdesc(element.attributeValue("rspdesc"));
			if(this.getWebServiceBean(name)!=null){
				throw new Exception("请检查属性文件中是否重名");
			}else{
				this.addWebServiceBean(webServiceBean);
			}
		}
	}
	
}
