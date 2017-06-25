package org.simpleproxy.dataparse.json;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 功能：json处理工具类
 * @author lmb 
 * @version 1.0
 * @date 2017-06-01
 */
public class JsonUtil {
   
    public static List<Map<String, Object>> parseJSON2List(String jsonStr){
        JSONArray jsonArr = JSONArray.fromObject(jsonStr);
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        Iterator<JSONObject> it = jsonArr.iterator();
        while(it.hasNext()){
            JSONObject json2 = it.next();
            list.add(parseJSON2Map(json2.toString()));
        }
        return list;
    }
    
    public static Map<String, Object> parseJSON2MapNoNull(String jsonStr){
        Map<String, Object> map = new HashMap<String, Object>();
        //最外层解析
        JSONObject json = JSONObject.fromObject(jsonStr);
        for(Object k : json.keySet()){
            Object v = json.get(k); 
            //如果内层还是数组的话，继续解析
            if(v instanceof JSONArray){
                List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
                Iterator<JSONObject> it = ((JSONArray)v).iterator();
                while(it.hasNext()){
                    JSONObject json2 = it.next();
                    list.add(parseJSON2Map(json2.toString()));
                }
                map.put(k.toString(), list);
            } else {
            	if (null != v && !"null".equals(v) && !"null".equals(v.toString())) {
					map.put(k.toString(), v);
				}
            }
        }
        return map;
    }
    
   
    public static Map<String, Object> parseJSON2Map(String jsonStr){
        Map<String, Object> map = new HashMap<String, Object>();
        //最外层解析
        JSONObject json = JSONObject.fromObject(jsonStr);
        for(Object k : json.keySet()){
            Object v = json.get(k); 
            //如果内层还是数组的话，继续解析
            if(v instanceof JSONArray){
                List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
                Iterator<JSONObject> it = ((JSONArray)v).iterator();
                while(it.hasNext()){
                    JSONObject json2 = it.next();
                    list.add(parseJSON2Map(json2.toString()));
                }
                map.put(k.toString(), list);
            } else {
                map.put(k.toString(), v);
            }
        }
        return map;
    }
    
    
    public static Map<String, String> parseJSON2Leve1Map(String jsonStr){
        Map<String, String> map = new HashMap<String, String>();
        //最外层解析
        JSONObject json = JSONObject.fromObject(jsonStr);
        for(Object k : json.keySet()){
            Object v = json.get(k); 
           
        }
        return map;
    }
    
   
    public static List<Map<String, Object>> getListByUrl(String url){
        try {
            //通过HTTP获取JSON数据
            InputStream in = new URL(url).openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line;
            while((line=reader.readLine())!=null){
                sb.append(line);
            }
            return parseJSON2List(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
   
    public static Map<String, Object> getMapByUrl(String url){
        try {
            //通过HTTP获取JSON数据
            InputStream in = new URL(url).openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line;
            while((line=reader.readLine())!=null){
                sb.append(line);
            }
            return parseJSON2Map(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
	 * 根据json报文和结点获取相应的值(适用于所有格式的json)
	 * @param respStr json报文
	 * @param NodePath 要获取的字段在json中的节点全路径
	 * @param separator 节点间的分隔符
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Map<String,Object>> json2MapbyHandler(String respStr,String NodePath,String separator){
		
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		Map map = parseJSON2Map(respStr);
		String[] strArray = NodePath.split(separator);
		String key = null;
		Object value = null;
		for (int i = 0; i < strArray.length; i++) {
			if (map.get(strArray[i]) instanceof List) {
				if (i < strArray.length - 1 && null != map.get(strArray[i]) && "" != map.get(strArray[i])) {
					Map<String,Object> mapList = map;
					if (((List<Map<String,Object>>)mapList.get(strArray[i])).size() > 0) {
						for (int j = 0; j < ((List<Map<String,Object>>)mapList.get(strArray[i])).size(); j++) {
							Map mapj = new HashMap();
							map = ((List<Map<String,Object>>)mapList.get(strArray[i])).get(j);
							mapj.put(strArray[i],map.get(strArray[i + 1]));//strArray[i] || listNode
							resultList.add(mapj);
						}
					}
				}
				break;
			}else{
				if (i < strArray.length - 1 && null != map.get(strArray[i]) && "" != map.get(strArray[i])) {
					map = parseJSON2Map(map.get(strArray[i]).toString());
				}else{
					Map<String,Object> resultMap = new HashMap<String,Object>();
					resultMap.put(strArray[i],map.get(strArray[i]));//strArray[i] || listNode
					resultList.add(resultMap);
				}
			}
		}
		System.out.println("resultList >> " + resultList);
		return resultList;		
	}
    
    
    public static void main(String[] args) {		
		//单层级的json
		String jsonStr1 = "{\"ContractRoot\":\"9002\",\"RspDesc\":\"TransactionID超出长度约束\",\"RspType\":\"9\",\"RspTime\":\"20170527144416\",\"TransactionID\":\"74201705281800202798\"}";
    	Map map = parseJSON2Map(jsonStr1);
		System.out.println(map);
//		{TransactionID=74201705281800202798, RspType=9, RspTime=20170527144416, RspDesc=TransactionID超出长度约束, ContractRoot=9002}
        
		
//		String str="{\"ROOT\":{\"HEADER\":{\"ROUTING\":{\"ROUTE_KEY\":\"\",\"ROUTE_VALUE\":\"\"},\"DB_ID\":\"EUR\",\"ENV_ID\":\"\",\"KEEP_LIVE\":\"\",\"PROVINCE_GROUP\":\":PROVINCE_GROUP:\",\"POOL_ID\":\"11\",\"TENANT_ID\":\"EUR\",\"LANG\":\"en_US\"},\"BODY\":{\"OUT_DATA\":{\"US_GPRS_USED\":0,\"GPRS_USED\":0,\"VOICE_TOTAL\":2000,\"VOICE_USED\":0,\"SMS_TOTAL\":1000,\"SMS_USED\":0,\"GPRS_TOTAL\":5242880,\"GPRS_FREE_TOTAL\":0,\"CMWAP_TOTAL\":0,\"CMWAP_FREE_TOTAL\":0,\"MMS_TOTAL\":0,\"MMS_USED\":0,\"MEAL_GPRS_TOTAL\":0,\"MEAL_GPRS_FREE_TOTAL\":0,\"ADD_GPRS_TOTAL\":0,\"ADD_GPRS_FREE_TOTAL\":0,\"VPMN_TOTAL\":0,\"VPMN_USED\":0,\"VPMN_REMAIN\":0,\"BRAND_NAME\":\"Europe 1C2N post-paid product\",\"FREE_INFO\":[{\"BUSI_CODE\":\"EUR_CUEasy ￡ 36(UK-CHINA)\",\"FAV_TYPE\":\"ECH0\",\"PRIORITY\":\"0\", \"PROD_PRCID\":\"10000000017607\",\"PROD_PRC_NAME\":\"CUEasy ￡ 36(UK-CHINA)\",\"TOTAL\":\"2000\",\"UNIT\":\"3\",\"UNIT_NAME\":\"Minute\",\"USED\":\"0\"},]},\"RETURN_CODE\":\"0\",\"RETURN_MSG\":\"OK\",\"USER_MSG\":\"OK\",\"DETAIL_MSG\":\"OK\",\"PROMPT_MSG\":\"OK\",\"RUN_IP\":\"192.168.50.112\"}}}";
		String str = "{\"ROOT\":{\"HEADER\":{\"ROUTING\":{\"ROUTE_KEY\":\"\",\"ROUTE_VALUE\":\"\"},\"DB_ID\":\"USA\",\"ENV_ID\":\"\",\"KEEP_LIVE\":\"\",\"PROVINCE_GROUP\":\"\",\"POOL_ID\":\"11\",\"TENANT_ID\":\"USA\",\"LANG\":\"en_US\"},\"BODY\":{\"RETURN_CODE\":\"10111109000000003\",\"RETURN_MSG\":\"Failed to get user info!\",\"USER_MSG\":\"取用户信息错误!\",\"DETAIL_MSG\":\"error\",\"PROMPT_MSG\":\"取用户信息错误!\",\"RUN_IP\":\"192.168.50.112\",\"OUT_DATA\":{\"FREE_INFO\"= [{\"BUSI_CODE\"= \"EUR_CUEasy ￡ 36(UK-CHINA)\",\"FAV_TYPE\"= \"ECH0\",\"PRIORITY\"= \"0\",\"PROD_PRCID\"= \"10000000017607\",\"PROD_PRC_NAME\"= \"CUEasy ￡ 36(UK-CHINA)\",\"TOTAL\"= \"2000\",\"UNIT\"= \"3\",\"UNIT_NAME\"= \"Minute\",\"USED\"= \"0\"},{\"BUSI_CODE\"= \"USA_CUEasy ￡ 36(UK-CHINA)\",\"FAV_TYPE\"= \"ECH0\",\"PRIORITY\"= \"0\",\"PROD_PRCID\"= \"10000000017607\",\"PROD_PRC_NAME\"= \"CUEasy ￡ 36(UK-CHINA)\",\"TOTAL\"= \"1000\",\"UNIT\"= \"1\",\"UNIT_NAME\"= \"piece\",\"USED\"= \"0\"}]}}}}}";
		String listNode = "ROOT>BODY>OUT_DATA>FREE_INFO>BUSI_CODE";
		List<Map<String,Object>> resultList = json2MapbyHandler(str,listNode,">");
		System.out.println(resultList);//[{FREE_INFO=EUR_CUEasy ￡ 36(UK-CHINA)}, {FREE_INFO=USA_CUEasy ￡ 36(UK-CHINA)}]
        
    }
}