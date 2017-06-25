package org.simpleproxy.dataparse.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;
 
/**
 * 功能：
 * 1、将单/多层级的Json字符串解析为Map格式
 * 2、将Map格式的数据封装成Json
 * 
 * 避免字符串中有特殊字符而出现的错误 
 * @author lmb 
 * @version 1.0
 * @date 2017-06-01
 */
public class JSONUtils {
 
    private final static String regex = "\"([^\\\" ]+?)\":";
 
    /**
     * 一个方法解析多层json数据  json + 正则 + 递归
     * @param jsonStr
     * @return 
     */
    public static Object jsonParse(final String jsonStr) {
        if (jsonStr == null) throw new NullPointerException("JsonString shouldn't be null");
        try {
            if (isJsonObject(jsonStr)) {
                final Pattern pattern = Pattern.compile(regex);
                final Matcher matcher = pattern.matcher(jsonStr);
                final Map<String, Object> map = new HashMap<String, Object>();
                final JSONObject jsonObject = new JSONObject(jsonStr);//不能解析类似jsonStr4的多层级json集合
//                System.out.println("jsonStr = " + jsonStr);
//                System.out.println("jsonObject = " + jsonObject);
                try {
                    for (; matcher.find(); ) {
                        String groupName = matcher.group(1);
                        Object obj = jsonObject.opt(groupName);
                        if (isJsonObject(obj+"") || isJsonArray(obj+"")) {
                            matcher.region(matcher.end() + (obj+"").replace("\\", "").length(), matcher.regionEnd());
                            map.put(groupName, jsonParse(obj+""));
                        } else {
                            map.put(groupName, obj+"");
                        }
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
                return map;
            } else if (isJsonArray(jsonStr)) {
                List<Object> list = new ArrayList<Object>();
                try {
                    JSONArray jsonArray = new JSONArray(jsonStr);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Object object = jsonArray.opt(i);
                        list.add(jsonParse(object+""));
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
                return list;
            } 
        } catch (Exception e) {
            // TODO: handle exception
        }
        return jsonStr;
    }
     
    /**
     * To determine whether a string is JsonObject {@link org.json.JSONObject}
     * @param jsonStr {@link java.lang.String}
     * @return boolean
     */
    private static boolean isJsonObject(final String jsonStr) {
        if (jsonStr == null) return false;
        return Pattern.matches("^\\{.*\\}$", jsonStr.trim());
    }
     
    /**
     * To determine whether a string is JsonArray {@link org.json.JSONArray};
     * @param jsonStr {@link java.lang.String}
     * @return boolean
     */
    private static boolean isJsonArray(final String jsonStr) {
        if (jsonStr == null) return false;
        return Pattern.matches("^\\[.*\\]$", jsonStr.trim());
    }
     
    /**
     * 将对象封装为json字符串 (json + 递归)
     * @param obj 参数应为{@link java.util.Map} 或者 {@link java.util.List}
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Object jsonEnclose(Object obj) {
        try {
            if (obj instanceof Map) {   //如果是Map则转换为JsonObject
                Map<String, Object> map = (Map<String, Object>)obj;
                Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
                JSONStringer jsonStringer = (JSONStringer) new JSONStringer().object();
                while (iterator.hasNext()) {
                    Entry<String, Object> entry = iterator.next();
                    jsonStringer.key(entry.getKey()).value(jsonEnclose(entry.getValue()));
                }
                JSONObject jsonObject = new JSONObject(new JSONTokener(jsonStringer.endObject().toString()));
                return jsonObject;
            } else if (obj instanceof List) {  //如果是List则转换为JsonArray
                List<Object> list = (List<Object>)obj;
                JSONStringer jsonStringer = (JSONStringer) new JSONStringer().array();
                for (int i = 0; i < list.size(); i++) {
                    jsonStringer.value(jsonEnclose(list.get(i)));
                }
                JSONArray jsonArray = new JSONArray(new JSONTokener(jsonStringer.endArray().toString()));
                return jsonArray;
            } else {
                return obj;
            }
        } catch (Exception e) {
            // TODO: handle exception
            return e.getMessage();
        }
    }
    
    
    @SuppressWarnings("unchecked")
	public static void main(String[] args) {
    	/*
    	 * 解析时使用示例
    	 */
    	//多层级json【已解决】
    	String jsonStr1 = "{\"ContractRoot\":{\"tcpCont\":{\"ActionCode\":\"1\",\"Response\":{\"RspCode\":\"9002\",\"RspDesc\":\"TransactionID超出长度约束\",\"RspType\":\"9\"},\"RspTime\":\"20170527144416\",\"TransactionID\":\"74201705281800202798\"}}}";
    	//单层级json【已解决】
    	String jsonStr2 = "{\"ContractRoot\":\"9002\",\"RspDesc\":\"TransactionID超出长度约束\",\"RspType\":\"9\",\"RspTime\":\"20170527144416\",\"TransactionID\":\"74201705281800202798\"}";
    	//String串【已解决 单层级json】
    	String jsonStr3 = "\"ContractRoot\":\"9002\",\"RspDesc\":\"TransactionID超出长度约束\",\"RspType\":\"9\",\"RspTime\":\"20170527144416\",\"TransactionID\":\"74201705281800202798\"";
       	//不能解决:org.json.JSONObject.JSONObject不能处理类似jsonStr5，第二个节点为多层级
    	String jsonStr5 = "{\"ContractRoot\":{\"TcpCont\":{\"ActionCode\":\"1\",\"Response\":{\"RspCode\":\"0\",\"RspDesc\":\"成功\",\"RspType\":\"0\"},\"RspTime\":\"20170602092900\",\"TransactionID\":\"74201706020910232832\"},\"SvcCont\":{\"UNI_BSS_BODY\":{\"SCHEME_CHG_RSP\":{\"RESP_DESC\":\"成功\",\"RESP_CODE\":\"0000\",\"RSP_INFO\":{\"PROVINCE_ORDER_ID\":\"20170602246444211\"}}}}}}";
    	Object object = jsonParse(jsonStr5);
//    	Object object = jsonParse("{" + jsonStr3 + "}");
    	System.out.println(object);
    	Map resultMap = new HashMap();
    	boolean flag = true;
    	while(flag){
    		flag = false;
            if (object instanceof String) {
               System.out.println("string　= " + object.toString());
            } else if (object instanceof Map) {
            	HashMap<String, Object> map = (HashMap<String, Object>)object;
            	System.out.println("2map = " + map);
            	System.out.println("3******");
                Iterator<Entry<String, Object>>  iterator = map.entrySet().iterator();
                while (iterator.hasNext()) {
                    Entry<String, Object> entry = iterator.next();
                    if (entry.getValue() instanceof Map) {
                    	 System.out.println(entry.getKey() + " 1= " + entry.getValue().toString());
						 object = entry.getValue();
                    	 flag = true;
                    } else {
                    	 System.out.println(entry.getKey() + " 2= " + entry.getValue().toString());
                    	 resultMap.put(entry.getKey(),entry.getValue().toString());
                    }
                }
            } else if (object instanceof List) {//list未测试
            	System.out.println("list = " + object.toString());
            }
    	}
    	 System.out.println("resultMap : " + resultMap);
    	 
    	/*
    	 * 封装时使用示例
    	 */
    	Map<String, Object> map = new HashMap<String, Object>();
        map.put("returnCode", "0");
        map.put("returnMsg", "成功");
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>(); 
        Map<String, Object> listMap = new HashMap<String, Object>();
        listMap.put("test", "测试");
        listMap.put("fuck", "fuck");
        list.add(listMap);
        list.add(listMap);
        map.put("returnStatus", list);
        System.out.println("map : " + map);
        System.out.println("json : " + jsonEnclose(map).toString());
    	//封装结果
    	//{"returnCode":"0","returnStatus":[{"test":"测试","fuck":"fuck"},{"test":"测试","fuck":"fuck"}],"returnMsg":"成功"}    	 
	}
     
}
