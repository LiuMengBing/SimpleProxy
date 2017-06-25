package org.simpleproxy.dataparse.json.handlepropertyfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.simpleproxy.dataparse.json.JsonUtil;
/**
 * 功能：通过属性文件解析JSON报文
 * @author lmb 
 * @version 1.0
 * @date 2017-6-5
 */
public class ParseJSONByPropertyFile {

	/**
	 * 根据json报文和结点全路径获取相应的值
	 * @param respStr json报文
	 * @param nodePath 要获取的字段在json中的节点全路径（用">"分割）
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String,Object>> json2MapbyHandler(String respStr,String nodePath){
		
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		Map map = JsonUtil.parseJSON2Map(respStr);
		String[] strArray = nodePath.split(">");
		String key = null;
		Object value = null;
		for (int i = 0; i < strArray.length; i++) {
			if (map.get(strArray[i]) instanceof List) {
				if (i < strArray.length - 1 && null != map.get(strArray[i]) && "" != map.get(strArray[i])) {
					Map<String,Object> mapList = map;
					if (((List<Map<String,Object>>)mapList.get(strArray[i])).size() > 0) {
						for (int j = 0; j < ((List<Map<String,Object>>)mapList.get(strArray[i])).size(); j++) {
							Map<String,Object> mapj = new HashMap<String,Object>();
							map = ((List<Map<String,Object>>)mapList.get(strArray[i])).get(j);
							mapj.put(strArray[i],map.get(strArray[i + 1]));
							resultList.add(mapj);
						}
					}
				}
				break;
			}else{
				if (i < strArray.length - 1 && null != map.get(strArray[i]) && "" != map.get(strArray[i])) {
					map = JsonUtil.parseJSON2Map(map.get(strArray[i]).toString());
				}else{
					Map<String,Object> resultMap = new HashMap<String,Object>();
					resultMap.put(strArray[i],map.get(strArray[i]));
					resultList.add(resultMap);
				}
			}
		}
		return resultList;		
	}
	
	public static void main(String[] args) {
//		String str="{\"ROOT\":{\"HEADER\":{\"ROUTING\":{\"ROUTE_KEY\":\"\",\"ROUTE_VALUE\":\"\"},\"DB_ID\":\"EUR\",\"ENV_ID\":\"\",\"KEEP_LIVE\":\"\",\"PROVINCE_GROUP\":\":PROVINCE_GROUP:\",\"POOL_ID\":\"11\",\"TENANT_ID\":\"EUR\",\"LANG\":\"en_US\"},\"BODY\":{\"OUT_DATA\":{\"US_GPRS_USED\":0,\"GPRS_USED\":0,\"VOICE_TOTAL\":2000,\"VOICE_USED\":0,\"SMS_TOTAL\":1000,\"SMS_USED\":0,\"GPRS_TOTAL\":5242880,\"GPRS_FREE_TOTAL\":0,\"CMWAP_TOTAL\":0,\"CMWAP_FREE_TOTAL\":0,\"MMS_TOTAL\":0,\"MMS_USED\":0,\"MEAL_GPRS_TOTAL\":0,\"MEAL_GPRS_FREE_TOTAL\":0,\"ADD_GPRS_TOTAL\":0,\"ADD_GPRS_FREE_TOTAL\":0,\"VPMN_TOTAL\":0,\"VPMN_USED\":0,\"VPMN_REMAIN\":0,\"BRAND_NAME\":\"Europe 1C2N post-paid product\",\"FREE_INFO\":[{\"BUSI_CODE\":\"EUR_CUEasy ￡ 36(UK-CHINA)\",\"FAV_TYPE\":\"ECH0\",\"PRIORITY\":\"0\", \"PROD_PRCID\":\"10000000017607\",\"PROD_PRC_NAME\":\"CUEasy ￡ 36(UK-CHINA)\",\"TOTAL\":\"2000\",\"UNIT\":\"3\",\"UNIT_NAME\":\"Minute\",\"USED\":\"0\"},]},\"RETURN_CODE\":\"0\",\"RETURN_MSG\":\"OK\",\"USER_MSG\":\"OK\",\"DETAIL_MSG\":\"OK\",\"PROMPT_MSG\":\"OK\",\"RUN_IP\":\"192.168.50.112\"}}}";
		String str = "{\"ROOT\":{\"HEADER\":{\"ROUTING\":{\"ROUTE_KEY\":\"\",\"ROUTE_VALUE\":\"\"},\"DB_ID\":\"USA\",\"ENV_ID\":\"\",\"KEEP_LIVE\":\"\",\"PROVINCE_GROUP\":\"\",\"POOL_ID\":\"11\",\"TENANT_ID\":\"USA\",\"LANG\":\"en_US\"},\"BODY\":{\"RETURN_CODE\":\"10111109000000003\",\"RETURN_MSG\":\"Failed to get user info!\",\"USER_MSG\":\"取用户信息错误!\",\"DETAIL_MSG\":\"error\",\"PROMPT_MSG\":\"取用户信息错误!\",\"RUN_IP\":\"192.168.50.112\",\"OUT_DATA\":{\"FREE_INFO\"= [{\"BUSI_CODE\"= \"EUR_CUEasy ￡ 36(UK-CHINA)\",\"FAV_TYPE\"= \"ECH0\",\"PRIORITY\"= \"0\",\"PROD_PRCID\"= \"10000000017607\",\"PROD_PRC_NAME\"= \"CUEasy ￡ 36(UK-CHINA)\",\"TOTAL\"= \"2000\",\"UNIT\"= \"3\",\"UNIT_NAME\"= \"Minute\",\"USED\"= \"0\"},{\"BUSI_CODE\"= \"USA_CUEasy ￡ 36(UK-CHINA)\",\"FAV_TYPE\"= \"ECH0\",\"PRIORITY\"= \"0\",\"PROD_PRCID\"= \"10000000017607\",\"PROD_PRC_NAME\"= \"CUEasy ￡ 36(UK-CHINA)\",\"TOTAL\"= \"1000\",\"UNIT\"= \"1\",\"UNIT_NAME\"= \"piece\",\"USED\"= \"0\"}]}}}}}";
		String nodePath = "ROOT>BODY>OUT_DATA>FREE_INFO>BUSI_CODE";
		System.out.println(json2MapbyHandler(str,nodePath));
		/*
		[{FREE_INFO=EUR_CUEasy ￡ 36(UK-CHINA)}, {FREE_INFO=USA_CUEasy ￡ 36(UK-CHINA)}]
		*/
	}


}
