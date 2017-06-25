package org.simpleproxy.dataparse.json.handlepropertyfile;

import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import org.simpleproxy.dataparse.json.handlepropertyfile.config.JSONFieldBean;
import org.simpleproxy.dataparse.json.handlepropertyfile.config.JSONServiceBean;
import org.simpleproxy.dataparse.json.handlepropertyfile.config.JSONServiceConfig;
import org.simpleproxy.dataparse.json.handlepropertyfile.config.JSONServiceConfigImpl;
import org.simpleproxy.date.DateUtils;
/**
 * 功能：通过属性文件组装JSON报文
 * @author lmb 
 * @version 1.0
 * @date 2017-6-5
 */
public class InstallJsonByPropertyFile {
	
	static JSONServiceConfig jsonServiceConfig = new JSONServiceConfigImpl();

	public static String getJson(JSONServiceBean jsonServiceBean,Map<String, Object> inParams) {
		/* 定义TreeMap容器实例，用于装载请求参数 */
		TreeMap<String, String> treeMap = new TreeMap<String, String>();
		//公共参数
		String appKey = jsonServiceBean.getAppKey();
		String appId = jsonServiceBean.getAppId();
		String version = jsonServiceBean.getVersion();
		treeMap.put("appKey", appKey);
		treeMap.put("appId", appId);
		treeMap.put("version", version);

		Map<String, JSONFieldBean> thirdFieldParams = jsonServiceBean.getParams();
		StringBuffer sb = new StringBuffer();
		StringBuffer paramBuffer = new StringBuffer();
		Random random = null;
		if (null != thirdFieldParams) {
			String key = null;
			String value = null;
			String defautVal = null;
			String rule = null;
			String reqSerialNo = null;
			JSONFieldBean jsonFieldBean = null;
			Set<String> reqParamsKey = inParams.keySet();
			Object obj = null;
			for (Entry<String, JSONFieldBean> entry : thirdFieldParams.entrySet()) {
				key = entry.getKey();
				jsonFieldBean = entry.getValue();
				value = jsonFieldBean.getValue();
				defautVal = jsonFieldBean.getDefautVal();
				rule = jsonFieldBean.getRule();
				reqSerialNo = jsonFieldBean.getReqSerialNo();
				int index = 0;
				if (null != value && !value.isEmpty()) {
					/** 根据入参填充 **/
					sb = new StringBuffer();
					for (String key2 : reqParamsKey) {
						if (value.contains(key2)) {
							value = value.replace(":" + key2 + ":",inParams.get(key2).toString());
							obj = inParams.get(key2);

							if (null != obj && !obj.toString().isEmpty()) {
								index++;
							}
						}
					}
					sb.append(value);
					treeMap.put(key, sb.toString());
				}
				if (null != rule && index == 0) {
					/** 字段生成规则 **/
					String ruleExp = null;
					sb = new StringBuffer();
					String[] rules = rule.split("&");
					if (null != rules && rules.length > 0) {
						for (String r : rules) {
							if (r.contains("Date")) {
								ruleExp = r.replaceAll("Date", "")
										.replace("[", "").replace("]", "");
								String ymd = DateUtils.getDateStr(new Date(),
										ruleExp);
								sb.append(ymd);
							} else if (r.contains("Num")) {
								ruleExp = r.replaceAll("Num", "")
										.replace("[", "").replace("]", "");
								random = new Random();
								// 随机生成数字，并添加到字符串
								for (int i = 0; i < Integer.valueOf(ruleExp); i++) {
									sb.append(random.nextInt(10));
								}
							}else if(r.contains("Value")) {
								ruleExp = r.replaceAll("Value", "")
										.replace("[", "").replace("]", "");
								sb.append(ruleExp);
							}
						}
					}
					treeMap.put(key, sb.toString());
				}
				if (null != defautVal && index == 0) {
					/** 默认值 **/
					sb = new StringBuffer();
					treeMap.put(key, defautVal);
					sb.append(defautVal);
				}
				paramBuffer.append(key + "=" + sb.toString() + "&");
			}
		}
		sb = new StringBuffer();
		sb.append("appKey=" + appKey + "&");
		sb.append("appId=" + appId + "&");
		sb.append("version=" + version + "&");
		String paramStr = paramBuffer.toString();
		sb.append(paramStr.substring(0, paramStr.length() - 1));
		return sb.toString();		
	}
	
	public static void main(String[] args) {
		JSONServiceBean jsonServiceBean = jsonServiceConfig.getJSONServiceBean("serviceName");
		Map<String,Object> inParams = null;
		getJson(jsonServiceBean,inParams);
	}

}
