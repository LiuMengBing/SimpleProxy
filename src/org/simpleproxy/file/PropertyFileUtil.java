package org.simpleproxy.file;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

public class PropertyFileUtil {
   
	private HashMap<String, String> map = null;

    private HashMap<String, String> reverseMap = null;

    private String fileDir = "";  

    /**
     * 功能描述：获取fileDir文件的内容，将其以key-value的形式解析到一个map中
     * @param fileDir 文件名
     */
    public PropertyFileUtil(String fileDir) {
        this.fileDir = fileDir;
        map = new HashMap<String, String>();
        try {
            String key = "";
            String value = "";
            Properties props = new Properties();
            //得到fileDir文件的输入流并将其load到我们生成的Properties对象中
            props.load(PropertyFileUtil.class.getClassLoader()
                    .getResourceAsStream(fileDir));
            //遍历这个Properties对象，将对象内容以key-value的形式解析到map中
            Enumeration enumFile = props.keys();
            for (int i = 0; i < props.size(); i++) {
                key = (String) enumFile.nextElement();
                value = props.getProperty(key);
                map.put(key, value);
            }
        } catch (IOException e) {
            System.out.println("I/O error occurs when reading the file");
            e.printStackTrace();
        }
    }

    /**
     * 功能描述：获取fileDir文件的内容，将其以value-key的反转形式解析到一个map中
     * @param fileDir 文件名
     */
    public void reverse() {
        reverseMap = new HashMap<String, String>();
        try {
            String key = "";
            String value = "";
            Properties props = new Properties();
            props.load(PropertyFileUtil.class.getClassLoader()
                    .getResourceAsStream(fileDir));
            Enumeration enumFile = props.keys();
            for (int i = 0; i < props.size(); i++) {
                key = (String) enumFile.nextElement();
                value = props.getProperty(key);
                reverseMap.put(value, key);//map.put(key, value)
            }
        } catch (IOException e) {
            System.out.println("I/O error occurs when reading the file");
            e.printStackTrace();
        }
    }

    /**
     * 功能描述：通过key获取到该key对应的value值
     * @param key
     * @return value
     */
    public String getValueByKey(String key) {
        return (String) map.get(key);
    }

    /**
     * 功能描述：获取map集合的所有key值
     * @return
     */
    public ArrayList getKeyList() {
        Set keySet = map.keySet();
        Object[] keyArray = keySet.toArray();
        ArrayList<String> keyList = new ArrayList<String>();
        for (int i = 0; i < keyArray.length; i++) {
            String tempKey = (String) keyArray[i];
            keyList.add(tempKey);
        }
        return keyList;
    }

    /**
     * 功能描述：获取反转map中key对应的value值（也就是正常情况下value对应的key）
     * @param key
     * @return
     */
    public String getReverseValueByKey(String key) {
        if (reverseMap==null){
            reverse();
            return (String) reverseMap.get(key);
        }else{
            return (String) reverseMap.get(key);
        }
    }

    /**
     * test
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("________test start!______");
        PropertyFileUtil propertyUtils = new PropertyFileUtil("system.properties");
        for (int i = 0; i < propertyUtils.getKeyList().size(); i++) {
            System.out.println("   " + propertyUtils.getKeyList().get(i) + "="
                    + propertyUtils.getValueByKey((String) propertyUtils.getKeyList().get(i)));
        }
        System.out.println(propertyUtils.getValueByKey("username").trim());
        System.out.println("_________test end!_______");
    }
}
