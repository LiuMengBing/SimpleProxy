package org.simpleproxy.dataparse.datacode;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * 功能：对字符串使用MD5编码/加密
 * @author lmb 
 * @version 1.0
 * @date 2017-6-15
 */
public class MD5Util{
	
	private final static String[] hexDigits = {"0", "1", "2", "3", "4",
        "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
	
	private static String encodeByMD5(String originstr) {
		if (originstr != null) {
			try {
				//创建具有指定算法名称的信息摘要
				MessageDigest md = MessageDigest.getInstance("MD5");
				//使用指定的字节数组对摘要进行最后的更新，然后完成摘要计算
				byte[] results = md.digest(originstr.getBytes());
				//将得到的字节数组编程字符窜返回
				String resultString = byteArrayToHexString(results);
				return resultString.toUpperCase();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}
	
	private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }
	
	private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }
	
	//将inputstr加密
	public static String createPassword(String inputstr) {
		return encodeByMD5(inputstr);
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		String password = MD5Util.createPassword("123456");
		System.out.println("对123456用MD5加密后：" + password);
	}
	
}
