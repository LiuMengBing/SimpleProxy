package org.simpleproxy.dataparse;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Vector;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

/**
 * 功能：String处理工具类
 * @author lmb 
 * @version 1.0
 * @date 2017-06-01
 */
public class StringUtil {

	private static final char[] QUOTE_ENCODE = "&quot;".toCharArray();
	private static final char[] AMP_ENCODE = "&amp;".toCharArray();
	private static final char[] LT_ENCODE = "&lt;".toCharArray();
	private static final char[] GT_ENCODE = "&gt;".toCharArray();
	private static MessageDigest digest = null;
	private static final int fillchar = 61;
	private static final String cvt = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
	private static Random randGen = new Random();

	private static char[] numbersAndLetters = "0123456789abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
			.toCharArray();

	private static final char[] zeroArray = "0000000000000000000000000000000000000000000000000000000000000000"
			.toCharArray();
	
	private static final char chars[] = { '0', '1', '2', '3', '4', '5', '6',
		'7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	private static StringUtil instance;

	/**
	 * 获取StringUtil实例对象
	 * @return
	 */
	public static synchronized StringUtil getInstance() {
		if (instance == null) {
			instance = new StringUtil();
		}
		return instance;
	}

	/**
	 * 将一个byte转换为十六进制数
	 * @param b
	 * @return
	 */
	public static String byte2hex(byte b) {
		char hex[] = new char[2];
		hex[0] = chars[(new Byte(b).intValue() & 0xf0) >> 4];
		hex[1] = chars[new Byte(b).intValue() & 0xf];
		return new String(hex);
	}
	
	/**
	 * 将byte[]转换为十六进制数
	 * @param b
	 * @return
	 */
	public static String bytes2hex(byte[] b) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			sb.append(byte2hex(b[i]));
			sb.append(" ");
		}
		return sb.toString();
	}
	
	/**
	 * 将字符串转化成特定长度的byte[],如果value的长度小于idx,则右补零。比如
	 * getText(5,"1"),结果为{49,0,0,0,0}; 如果value的长度大于idx,则截取掉一部分。比如
	 * getText(2,"11111"),结果为{49,49};
	 *
	 * @param idx 转化后byte[]的长度
	 * @param value 要转化的字符串
	 * @return byte[]
	 */
	public static byte[] getText(int idx, String value) {
		byte[] b1 = new byte[idx];
		int i = 0;
		if (value != null || !value.equals("")) {
			byte[] b2 = value.getBytes();
			while (i < b2.length && i < idx) {
				b1[i] = b2[i];
				i++;
			}
		}
		while (i < b1.length) {
			b1[i] = 0;
			i++;
		}
		return b1;
	}

	/**
	 * long类型转化成8个字节
	 * @param i 要转化的长整形
	 * @return byte[]
	 */
	public static byte[] longToBytes8(long i) {
		byte mybytes[] = new byte[8];
		mybytes[7] = (byte) (int) ((long) 255 & i);
		mybytes[6] = (byte) (int) (((long) 65280 & i) >> 8);
		mybytes[5] = (byte) (int) (((long) 0xff0000 & i) >> 16);
		mybytes[4] = (byte) (int) (((long) 0xff000000 & i) >> 24);
		int high = (int) (i >> 32);
		mybytes[3] = (byte) (0xff & high);
		mybytes[2] = (byte) ((0xff00 & high) >> 8);
		mybytes[1] = (byte) ((0xff0000 & high) >> 16);
		mybytes[0] = (byte) ((0xff000000 & high) >> 24);
		return mybytes;
	}

	/**
	 * int转化成4个字节的数组
	 * @param i 要转化的整形变量
	 * @return byte[]
	 */
	public static byte[] intToBytes4(int i) {
		byte mybytes[] = new byte[4];
		mybytes[3] = (byte) (0xff & i);
		mybytes[2] = (byte) ((0xff00 & i) >> 8);
		mybytes[1] = (byte) ((0xff0000 & i) >> 16);
		mybytes[0] = (byte) ((0xff000000 & i) >> 24);
		return mybytes;
	}

	/**
	 * 长度不够时，在字符串左端或右端填补addChar字符
	 * @param value
	 * @param length
	 * @param type
	 * @param addChar
	 * @return
	 */
	public static String addChar(String value, int length, String type, char addChar) {
		if("left".equals(type) || "LEFT".equals(type)) {
			return addLeftChar(value,length,addChar);
		} else {
			return addRightChar(value,length,addChar);
		}
	}
	/**
	 * 长度不够时，在字符串右端填补addChar字符
	 * @param value
	 * @param length
	 * @param addChar
	 * @return
	 */
	public static String addRightChar(String value, int length, char addChar) {
		if (StringUtils.isBlank(value)) {
			value = "";
		}
		if (value.length() > length) {
			return value.substring(0, length);
		}
		char[] c = new char[length];
		System.arraycopy(value.toCharArray(), 0, c, 0, value.length());
		for (int i = value.length(); i < c.length; i++) {
			c[i] = addChar;
		}
		return new String(c);
	}

	
	/**
	 * 长度不够时，在字符串左端填补addChar字符
	 * @param str
	 * @param length
	 * @param addChar
	 * @return
	 */
	public static String addLeftChar(String str, int length, char addChar) {
		int old = str.length();
		if (length > old) {
			char[] c = new char[length];
			char[] x = str.toCharArray();
			if (x.length > length) {
				throw new IllegalArgumentException(
						"Numeric value is larger than intended length: " + str
								+ " LEN " + length);
			}
			int lim = c.length - x.length;
			for (int i = 0; i < lim; i++) {
				c[i] = addChar;
			}
			System.arraycopy(x, 0, c, lim, x.length);
			return new String(c);
		}
		return str.substring(0, length);
	}
	
	/**
	 * 长度不够时，在字符串右端填补空格
	 * @param s
	 * @param length
	 * @return
	 */
	public static String addRightSpace(String value, int length){
		return addRightChar(value, length, ' ');
	}
	
	/**
	 * 长度不够时，在字符串左端填补空格
	 * @param value
	 * @param length
	 * @return
	 */
	public static String addLeftSpace(String value, int length){
		return addLeftChar(value, length, ' ');
	}
	
	/**
	 * 长度不够时，在字符串左端填补 '0'
	 * @param value
	 * @param length
	 * @return
	 */
	public static String addLeftZero(String value, int length) {
		return addLeftChar(value, length, '0');
	}

	/**
	 * 长度不够时，在字符串右端填补 '0'
	 * @param value
	 * @param length
	 * @return
	 */
	public static String addRightZero(String value, int length) {
		return addRightChar(value, length, '0');
	}
	
	/**
	 * 判断字符串是否为null或""
	 * @param 需要判空的字符串
	 * @return
	 */

	public static boolean isNullStr(String str) {
		return str == null || str.trim().length() == 0;
	}
	
	/**
	 * 将十六进制字符串转换为byte数组
	 * @param hexString
	 * @return
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}
	
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}
	/**
	 * 字符数组转换为字符串
	 * @param byteArr
	 * @return
	 */
	public static String byteArr2Str(byte[] byteArr) {
		return new String(byteArr);
	}
	/**
	 * 根据指定字符集将字节数组转换为字符串，如果指定字符集不存在则使用系统默认字符集进行转换
	 * @param byteArr
	 * @param charSet
	 * @return
	 */
	public static String byteArr2Str(byte[] byteArr,String charSet) {
		try {
			return new String(byteArr,charSet);
		} catch (UnsupportedEncodingException e) {
			return byteArr2Str(byteArr);
		}
	}
	/**
	 * 根据指定的字符集将字符串转换成字节数组，如果字符集不存在则使用系统默认字符集进行转换
	 * @param src 要转换的字符串
	 * @param charSet 转换使用的字符集
	 * @return
	 */
	public static byte[] string2Bytes(String src, String charSet) {
		try {
			return src.getBytes(charSet);
		} catch (UnsupportedEncodingException e) {
			return string2Bytes(src);
		}
	}
	/**
	 * 使用系统默认字符集进将字符串转换成字节数组
	 * @param src
	 */
	public static byte[] string2Bytes(String src) {
		return src.getBytes();
	}

	/**
	 * 获取随机数
	 * @return
	 * @throws GeneratorException
	 */
	public static String serialNumber(int number){
		String serialStr = "";
		try{
			long seed = (long)Math.pow(10.0, (double)number);
			Random random = new Random();
			long randomLong = random.nextLong();
			long serialNumber = Math.abs(randomLong % seed) ;
			serialStr = String.valueOf(serialNumber);
		}catch(Exception e){}
		int num = number - serialStr.length();
		for(int i=1;i<=num;i++){
			serialStr = serialStr + "0";
		}
		return serialStr;
	}

	/**
	 * 转义字符处理函数replaceString(String, "&lt;", "<")
	 * @param strData                    如：root>body>username
	 * @param regex 要替换的字符串                       >
	 * @param replacement 要替换成的字符串       :
	 * @return  root:body:username
	 */
	public static String replaceString(String str, String regex, String replacement) {
        if (str == null){
            return null;
        }
        int index;
        index = str.indexOf(regex);
        String strNew = "";
        if (index >= 0){
            while (index >= 0){
                strNew += str.substring(0, index) + replacement;
                str = str.substring(index + regex.length());
                index = str.indexOf(regex);
            }
            strNew += str;
            return strNew;
        }
        return str;
    }

	
	/**
	 * 根据特殊符号拆分字符串
	 * @param source 待拆分的字符串
	 * @param sign 特殊符号
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Enumeration split(String source, String sign) {
		Vector ar = new Vector();
		int start = 0;
		String st = source;
		while (true) {
			int ipos = st.indexOf(sign);
			if (ipos < 0) {
				ar.addElement(st.substring(0));
				break;
			}
			ar.addElement(st.substring(0, ipos));
			st = st.substring(ipos + 1);
		}

		return ar.elements();
	}

	/**
	 * 判断某个字符串中是否存在另外一个字符串
	 * @param source 主字符串
	 * @param desc 需要判断是否存在的字符串
	 * @return
	 */
	public static boolean inStr(String source, String desc) {
		return (source.indexOf(desc) >= 0);
	}

	/**
	 * 根据编码格式对字符串进行重新编码
	 * @param source 需要编码的字符串
	 * @param enCharset 编码格式（将字符串编码为字节序列）
	 * @param deCharset 解码格式（将字节序列解码为字符串）
	 * @return
	 */
	public static String decodeGB(String source, String enCharset,String deCharset) {
		try {
			return new String(source.getBytes(enCharset), deCharset);
		} catch (Exception E) {
		}
		return source;
	}

	public static String replace(String source, char str1, String str2) {
		if (source == null) {
			return source;
		}
		String desc = "";
		for (int i = 0; i < source.length(); ++i) {
			if (source.charAt(i) == str1)
				desc = desc + str2;
			else {
				desc = desc + String.valueOf(source.charAt(i));
			}
		}
		return desc;
	}

	public static String replace(String source, String str1, String str2) {
		if (source == null) {
			return source;
		}
		String desc = "";
		int i = 0;
		while (i < source.length()) {
			if (source.startsWith(str1, i)) {
				desc = desc + str2;
				i += str1.length();
			} else {
				desc = desc + String.valueOf(source.charAt(i));
				++i;
			}
		}
		return desc;
	}

	/**
	 * 去除掉空格
	 * @param inObj
	 * @return
	 */
	public static String killNull(Object inObj) {
		if (inObj == null) {
			return "";
		}
		return inObj.toString().trim();
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Vector stringToVecor(String sourceString, String strInterval) {
		Vector returnStr = new Vector();
		if ((sourceString == "") || (strInterval == "")) {
			returnStr.addElement(sourceString);
			return returnStr;
		}
		int found_str = sourceString.indexOf(strInterval);
		while (found_str >= 0) {
			returnStr.addElement(sourceString.substring(0, found_str));
			sourceString = sourceString.substring(found_str
					+ strInterval.length());
			found_str = sourceString.indexOf(strInterval);
		}
		if (found_str < 0)
			returnStr.addElement(sourceString);
		return returnStr;
	}

	/**
	 * 根据指定的摘要算法对字符串进行加密
	 * @param password 需要加密的字符串
	 * @param algorithm 摘要算法
	 * @return
	 */
	public static String encodePassword(String password, String algorithm) {
		byte[] unencodedPassword = password.getBytes();
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(algorithm);
		} catch (Exception e) {

			return password;
		}
		md.reset();
		md.update(unencodedPassword);
		byte[] encodedPassword = md.digest();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < encodedPassword.length; ++i) {
			if ((encodedPassword[i] & 0xFF) < 16) {
				buf.append("0");
			}

			buf.append(Long.toString(encodedPassword[i] & 0xFF, 16));
		}
		return buf.toString();
	}

	/**
	 * 对字符串进行Base64加密
	 * @param str
	 * @return
	 */
	public static String encodeString(String str) {
		return new String(Base64.encodeBase64(str.getBytes()));
	}

	/**
	 * 对字符串进行Base64解密
	 * @param str
	 * @return
	 */
	public static String decodeString(String str) {
		return new String(Base64.decodeBase64(str.getBytes()));
	}

	public static final String replaceIgnoreCase(String line, String oldString,
			String newString) {
		if (line == null)
			return null;
		String lcLine = line.toLowerCase();
		String lcOldString = oldString.toLowerCase();
		int i = 0;
		if ((i = lcLine.indexOf(lcOldString, i)) >= 0) {
			char[] line2 = line.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;

			int j ;
			for (  j = i; (i = lcLine.indexOf(lcOldString, i)) > 0; j = i) {
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
			}
			buf.append(line2, j, line2.length - j);
			return buf.toString();
		}
		return line;
	}

	public static final String replaceIgnoreCase(String line, String oldString,
			String newString, int[] count) {
		if (line == null)
			return null;
		String lcLine = line.toLowerCase();
		String lcOldString = oldString.toLowerCase();
		int i = 0;
		if ((i = lcLine.indexOf(lcOldString, i)) >= 0) {
			int counter = 1;
			char[] line2 = line.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j;
			for ( j= i; (i = lcLine.indexOf(lcOldString, i)) > 0; j = i) {
				++counter;
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
			}

			buf.append(line2, j, line2.length - j);
			count[0] = counter;
			return buf.toString();
		}
		return line;
	}

	public static final String replace(String line, String oldString,
			String newString, int[] count) {
		if (line == null)
			return null;
		int i = 0;
		if ((i = line.indexOf(oldString, i)) >= 0) {
			int counter = 1;
			char[] line2 = line.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j ;
			for (  j = i; (i = line.indexOf(oldString, i)) > 0; j = i) {
				++counter;
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
			}

			buf.append(line2, j, line2.length - j);
			count[0] = counter;
			return buf.toString();
		}
		return line;
	}

	public static final String stripTags(String in) {
		if (in == null)
			return null;
		int i = 0;
		int last = 0;
		char[] input = in.toCharArray();
		int len = input.length;
		StringBuffer out = new StringBuffer((int) (len * 1.3D));
		for (; i < len; ++i) {
			char ch = input[i];
			if (ch <= '>') {
				if (ch == '<')
					if ((i + 3 < len) && (input[(i + 1)] == 'b')
							&& (input[(i + 2)] == 'r')
							&& (input[(i + 3)] == '>')) {
						i += 3;
					} else {
						if (i > last)
							out.append(input, last, i - last);
						last = i + 1;
					}
				else if (ch == '>')
					last = i + 1;
			}
		}
		if (last == 0)
			return in;
		if (i > last)
			out.append(input, last, i - last);
		return out.toString();
	}

	public static final String escapeHTMLTags(String in) {
		if (in == null)
			return null;
		int i = 0;
		int last = 0;
		char[] input = in.toCharArray();
		int len = input.length;
		StringBuffer out = new StringBuffer((int) (len * 1.3D));
		for (; i < len; ++i) {
			char ch = input[i];
			if (ch <= '>') {
				if (ch == '<') {
					if (i > last)
						out.append(input, last, i - last);
					last = i + 1;
					out.append(LT_ENCODE);
				} else if (ch == '>') {
					if (i > last)
						out.append(input, last, i - last);
					last = i + 1;
					out.append(GT_ENCODE);
				}
			}
		}
		if (last == 0)
			return in;
		if (i > last)
			out.append(input, last, i - last);
		return out.toString();
	}

	public static final synchronized String hash(String data) {
		if (digest == null)
			try {
				digest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
			}
		try {
			digest.update(data.getBytes("utf-8"));
		} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
		}
		return encodeHex(digest.digest());
	}

	public static final String encodeHex(byte[] bytes) {
		StringBuffer buf = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; ++i) {
			if ((bytes[i] & 0xFF) < 16)
				buf.append("0");
			buf.append(Long.toString(bytes[i] & 0xFF, 16));
		}

		return buf.toString();
	}

	public static final byte[] decodeHex(String hex) {
		char[] chars = hex.toCharArray();
		byte[] bytes = new byte[chars.length / 2];
		int byteCount = 0;
		for (int i = 0; i < chars.length; i += 2) {
			int newByte = 0;
			newByte |= hexCharToByte(chars[i]);
			newByte <<= 4;
			newByte |= hexCharToByte(chars[(i + 1)]);
			bytes[byteCount] = (byte) newByte;
			++byteCount;
		}

		return bytes;
	}

	private static final byte hexCharToByte(char ch) {
		switch (ch) {
		case '0':
			return 0;
		case '1':
			return 1;
		case '2':
			return 2;
		case '3':
			return 3;
		case '4':
			return 4;
		case '5':
			return 5;
		case '6':
			return 6;
		case '7':
			return 7;
		case '8':
			return 8;
		case '9':
			return 9;
		case 'a':
			return 10;
		case 'b':
			return 11;
		case 'c':
			return 12;
		case 'd':
			return 13;
		case 'e':
			return 14;
		case 'f':
			return 15;
		case ':':
		case ';':
		case '<':
		case '=':
		case '>':
		case '?':
		case '@':
		case 'A':
		case 'B':
		case 'C':
		case 'D':
		case 'E':
		case 'F':
		case 'G':
		case 'H':
		case 'I':
		case 'J':
		case 'K':
		case 'L':
		case 'M':
		case 'N':
		case 'O':
		case 'P':
		case 'Q':
		case 'R':
		case 'S':
		case 'T':
		case 'U':
		case 'V':
		case 'W':
		case 'X':
		case 'Y':
		case 'Z':
		case '[':
		case '\\':
		case ']':
		case '^':
		case '_':
		case '`':
		}

		return 0;
	}

	public static String encodeBase64(String data) {
		byte[] bytes = (byte[]) null;
		try {
			bytes = data.getBytes("ISO-8859-1");
		} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
		}
		return encodeBase64(bytes);
	}

	public static String encodeBase64(byte[] data) {
		int len = data.length;
		StringBuffer ret = new StringBuffer((len / 3 + 1) * 4);
		for (int i = 0; i < len; ++i) {
			int c = data[i] >> 2 & 0x3F;
			ret.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
					.charAt(c));
			c = data[i] << 4 & 0x3F;
			if (++i < len)
				c |= data[i] >> 4 & 0xF;
			ret.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
					.charAt(c));
			if (i < len) {
				c = data[i] << 2 & 0x3F;
				if (++i < len)
					c |= data[i] >> 6 & 0x3;
				ret.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
						.charAt(c));
			} else {
				++i;
				ret.append('=');
			}
			if (i < len) {
				c = data[i] & 0x3F;
				ret.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
						.charAt(c));
			} else {
				ret.append('=');
			}
		}
		return ret.toString();
	}

	public static String decodeBase64(String data) {
		byte[] bytes = (byte[]) null;
		try {
			bytes = data.getBytes("ISO-8859-1");
		} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
		}
		return decodeBase64(bytes);
	}

	public static String decodeBase64(byte[] data) {
		int len = data.length;
		StringBuffer ret = new StringBuffer(len * 3 / 4);
		for (int i = 0; i < len; ++i) {
			int c = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
					.indexOf(data[i]);
			++i;
			int c1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
					.indexOf(data[i]);
			c = c << 2 | c1 >> 4 & 0x3;
			ret.append((char) c);
			if (++i < len) {
				c = data[i];
				if (61 == c)
					break;
				c = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
						.indexOf(c);
				c1 = c1 << 4 & 0xF0 | c >> 2 & 0xF;
				ret.append((char) c1);
			}
			if (++i >= len)
				continue;
			c1 = data[i];
			if (61 == c1)
				break;
			c1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
					.indexOf(c1);
			c = c << 6 & 0xC0 | c1;
			ret.append((char) c);
		}
		return ret.toString();
	}

	public static final String[] toLowerCaseWordArray(String text) {
		if ((text == null) || (text.length() == 0))
			return new String[0];
		ArrayList wordList = new ArrayList();
		BreakIterator boundary = BreakIterator.getWordInstance();
		boundary.setText(text);
		int start = 0;
		for (int end = boundary.next(); end != -1; end = boundary.next()) {
			String tmp = text.substring(start, end).trim();
			tmp = replace(tmp, "+", "");
			tmp = replace(tmp, "/", "");
			tmp = replace(tmp, "\\", "");
			tmp = replace(tmp, "#", "");
			tmp = replace(tmp, "*", "");
			tmp = replace(tmp, ")", "");
			tmp = replace(tmp, "(", "");
			tmp = replace(tmp, "&", "");
			if (tmp.length() > 0)
				wordList.add(tmp);
			start = end;
		}

		return ((String[]) wordList.toArray(new String[wordList.size()]));
	}

	public static final String randomString(int length) {
		if (length < 1)
			return null;
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; ++i) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
		}
		return new String(randBuffer);
	}

	public static final String chopAtWord(String string, int length) {
		if ((string == null) || (string.length() == 0))
			return string;
		char[] charArray = string.toCharArray();
		int sLength = string.length();
		if (length < sLength)
			sLength = length;
		for (int i = 0; i < sLength - 1; ++i) {
			if ((charArray[i] == '\r') && (charArray[(i + 1)] == '\n'))
				return string.substring(0, i + 1);
			if (charArray[i] == '\n') {
				return string.substring(0, i);
			}
		}
		if (charArray[(sLength - 1)] == '\n')
			return string.substring(0, sLength - 1);
		if (string.length() <= length)
			return string;
		for (int i = length - 1; i > 0; --i) {
			if (charArray[i] == ' ')
				return string.substring(0, i).trim();
		}
		return string.substring(0, length);
	}

	public static final String chopAtWordSubstring(String string, int length) {
		if ((string == null) || (string.length() == 0))
			return string;
		char[] charArray = string.toCharArray();
		int sLength = string.length();
		if (length < sLength)
			sLength = length;
		for (int i = 0; i < sLength - 1; ++i) {
			if ((charArray[i] == '\r') && (charArray[(i + 1)] == '\n'))
				return string.substring(0, i + 1);
			if (charArray[i] == '\n') {
				return string.substring(0, i);
			}
		}
		if (charArray[(sLength - 1)] == '\n')
			return string.substring(0, sLength - 1);
		if (string.length() <= length) {
			return string;
		}
		return string.substring(0, length) + "...";
	}

	public static String chopAtWordsAround(String input, String[] wordList,
			int numChars) {
		if ((input == null) || ("".equals(input.trim())) || (wordList == null)
				|| (wordList.length == 0) || (numChars == 0))
			return null;
		String lc = input.toLowerCase();
		for (int i = 0; i < wordList.length; ++i) {
			int pos = lc.indexOf(wordList[i]);
			if (pos > -1) {
				int beginIdx = pos - numChars;
				if (beginIdx < 0)
					beginIdx = 0;
				int endIdx = pos + numChars;
				if (endIdx > input.length() - 1) {
					endIdx = input.length() - 1;
				}
				char[] chars = input.toCharArray();
				do {
					--beginIdx;
					if ((beginIdx <= 0) || (chars[beginIdx] == ' ')
							|| (chars[beginIdx] == '\n'))
						break;
				} while (chars[beginIdx] != '\r');
				for (; (endIdx < input.length()) && (chars[endIdx] != ' ')
						&& (chars[endIdx] != '\n') && (chars[endIdx] != '\r'); ++endIdx)
					;
				return input.substring(beginIdx, endIdx);
			}
		}
		return input.substring(0,
				(input.length() >= 200) ? 200 : input.length());
	}

	public static String wordWrap(String input, int width, Locale locale) {
		if (input == null)
			return "";
		if (width < 5)
			return input;
		if (width >= input.length())
			return input;
		StringBuffer buf = new StringBuffer(input);
		boolean endOfLine = false;
		int lineStart = 0;
		for (int i = 0; i < buf.length(); ++i) {
			if (buf.charAt(i) == '\n') {
				lineStart = i + 1;
				endOfLine = true;
			}
			if (i > lineStart + width - 1) {
				if (!(endOfLine)) {
					int limit = i - lineStart - 1;
					BreakIterator breaks = BreakIterator
							.getLineInstance(locale);
					breaks.setText(buf.substring(lineStart, i));
					int end = breaks.last();
					if ((end == limit + 1)
							&& (!(Character.isWhitespace(buf.charAt(lineStart
									+ end)))))
						end = breaks.preceding(end - 1);
					if ((end != -1) && (end == limit + 1)) {
						buf.replace(lineStart + end, lineStart + end + 1, "\n");
						lineStart += end;
					} else if ((end != -1) && (end != 0)) {
						buf.insert(lineStart + end, '\n');
						lineStart = lineStart + end + 1;
					} else {
						buf.insert(i, '\n');
						lineStart = i + 1;
					}
				} else {
					buf.insert(i, '\n');
					lineStart = i + 1;
					endOfLine = false;
				}
			}
		}
		return buf.toString();
	}

	public static final String highlightWords(String string, String[] words,
			String startHighlight, String endHighlight) {
		if ((string == null) || (words == null) || (startHighlight == null)
				|| (endHighlight == null))
			return null;
		StringBuffer regexp = new StringBuffer();
		for (int x = 0; x < words.length; ++x) {
			regexp.append(words[x]);
			if (x != words.length - 1) {
				regexp.append("|");
			}
		}
		regexp.insert(0, "s/\\b(");
		regexp.append(")\\b/");
		regexp.append(startHighlight);
		regexp.append("$1");
		regexp.append(endHighlight);
		regexp.append("/igm");
		return null;
	}

	public static final String escapeForXML(String string) {
		if (string == null)
			return null;
		int i = 0;
		int last = 0;
		char[] input = string.toCharArray();
		int len = input.length;
		StringBuffer out = new StringBuffer((int) (len * 1.3D));
		for (; i < len; ++i) {
			char ch = input[i];
			if (ch <= '>') {
				if (ch == '<') {
					if (i > last)
						out.append(input, last, i - last);
					last = i + 1;
					out.append(LT_ENCODE);
				} else if (ch == '&') {
					if (i > last)
						out.append(input, last, i - last);
					last = i + 1;
					out.append(AMP_ENCODE);
				} else if (ch == '"') {
					if (i > last)
						out.append(input, last, i - last);
					last = i + 1;
					out.append(QUOTE_ENCODE);
				} else if ((ch != '\n') && (ch != '\r') && (ch != '\t')
						&& (ch < ' ')) {
					if (i > last)
						out.append(input, last, i - last);
					last = i + 1;
				}
			}
		}
		if (last == 0)
			return string;
		if (i > last)
			out.append(input, last, i - last);
		return out.toString();
	}

	public static final String unescapeFromXML(String string) {
		string = replace(string, "&lt;", "<");
		string = replace(string, "&gt;", ">");
		string = replace(string, "&quot;", "\"");
		return replace(string, "&amp;", "&");
	}

	public static final String zeroPadString(String string, int length) {
		if ((string == null) || (string.length() > length)) {
			return string;
		}
		StringBuffer buf = new StringBuffer(length);
		buf.append(zeroArray, 0, length - string.length()).append(string);
		return buf.toString();
	}

	public static final String dateToMillis(Date date) {
		return zeroPadString(Long.toString(date.getTime()), 15);
	}

	public static String getMimeType(String strExt) {
		strExt = strExt.toLowerCase();
		String strMimeType = "application/octet-stream";

		if (("htm".equals(strExt)) || ("html".equals(strExt)))
			strMimeType = "text/html";
		else if ("txt".equals(strExt))
			strMimeType = "text/plain";
		else if ("rtf ".equals(strExt))
			strMimeType = "application/rtf";
		else if ("pdf".equals(strExt))
			strMimeType = "application/pdf";
		else if ("doc".equals(strExt))
			strMimeType = "application/msword";
		else if (("ppt".equals(strExt)) || ("ppz".equals(strExt))
				|| ("pps".equals(strExt)) || ("pot".equals(strExt))) {
			strMimeType = "application/mspowerpoint ";
		} else if ("gtar".equals(strExt))
			strMimeType = "application/x-gtar";
		else if ("tar".equals(strExt))
			strMimeType = "application/x-tar";
		else if ("zip".equals(strExt)) {
			strMimeType = "application/zip";
		} else if ("gif".equals(strExt))
			strMimeType = "application/rtf";
		else if ("png".equals(strExt))
			strMimeType = "application/rtf";
		else if (("jpeg".equals(strExt)) || ("jpg".equals(strExt))
				|| ("jpe".equals(strExt)))
			strMimeType = "image/jpeg";
		else if (("tiff".equals(strExt)) || ("tif".equals(strExt))) {
			strMimeType = "application/tiff";
		}
		return strMimeType;
	}

	public static List stringToList(String str, String split) {
		List r = new ArrayList();
		if ((str != null) && (!(str.trim().equals("")))) {
			str = leftTrim(str, split);
			int pre = 0;
			int index = str.indexOf(split, pre);
			while (index >= 0) {
				String freg = str.substring(pre, index);
				if ((freg != null) && (!(freg.trim().equals("")))) {
					r.add(freg);
				}
				pre = index + split.length();
				index = str.indexOf(split, pre);
			}
			String left = str.substring(pre);
			if ((left != null) && (!(left.trim().equals("")))) {
				r.add(left);
			}
		}
		return r;
	}

	private static String trim(String s1, String s2) {
		s1 = leftTrim(s1, s2);
		return rightTrim(s1, s2);
	}

	private static String leftTrim(String s1, String s2) {
		while (s1.startsWith(s2)) {
			s1 = s1.substring(s2.length());
		}
		return s1;
	}

	private static String rightTrim(String s1, String s2) {
		while (s1.endsWith(s2)) {
			s1 = s1.substring(0, s1.length() - s2.length());
		}
		return s1;
	}

	public static String listToString(List list, String link) {
		StringBuffer sb = new StringBuffer();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			sb.append(iterator.next()).append(link);
		}
		String rt = sb.toString();
		if (rt.length() > link.length()) {
			rt = rt.substring(0, rt.length() - link.length());
		}
		return rt;
	}

	public static String round(String numberStr, int decimalIndex) {
		String desStr = numberStr.substring(0, numberStr.lastIndexOf(".") + 1
				+ decimalIndex + 1);

		BigDecimal deSource = new BigDecimal(desStr);
		String newStr = String.valueOf(deSource.setScale(decimalIndex, 4)
				.doubleValue());

		return new String(newStr);
	}

	public static String unicode2Char(String str) {
		int index = 0;
		String result = "";
		while (index < str.length() - 1) {
			if ((str.charAt(index) == '%') && (str.charAt(index + 1) == 'u')) {
				result = result
						+ (char) Integer.parseInt(
								str.substring(index + 2, index + 6), 16);
				index += 6;
			} else {
				result = result + str.charAt(index);
				++index;
			}
		}
		if (index <= str.length() - 1) {
			result = result + str.charAt(str.length() - 1);
		}
		return result.replaceAll("%20", " ");
	}

	public static String getPointStr(String str, int length) {
		if ((str == null) || ("".equals(str))) {
			return "";
		}

		if (length <= 0) {
			return str;
		}
		if (getStrLength(str) > length) {
			str = getLeftStr(str, length - 2);
		}
		return str;
	}

	public static String getLeftStr(String str, int length) {
		if ((str == null) || ("".equals(str))) {
			return "";
		}
		int index = 0;
		int strLength = str.length();

		if (length < 0) {
			length = 0;
		}

		char[] charArray = str.toCharArray();

		for (; index < length; ++index) {
			if (index >= strLength) {
				break;
			}
			if (((charArray[index] >= 12288) && (charArray[index] < 40959))
					|| (charArray[index] >= 63744)) {
				--length;
			}

			if (charArray[index] != '&')
				continue;
			if ((strLength > index + 3) && (charArray[(index + 1)] == 'l')
					&& (charArray[(index + 2)] == 't')
					&& (charArray[(index + 3)] == ';')) {
				length += 3;
				index += 3;
			}

			if ((strLength > index + 4) && (charArray[(index + 1)] == '#')
					&& (charArray[(index + 2)] == '4')
					&& (charArray[(index + 3)] == '6')
					&& (charArray[(index + 4)] == ';')) {
				length += 4;
				index += 4;
			}

			if ((strLength > index + 5) && (charArray[(index + 1)] == 'n')
					&& (charArray[(index + 2)] == 'b')
					&& (charArray[(index + 3)] == 's')
					&& (charArray[(index + 4)] == 'p')
					&& (charArray[(index + 5)] == ';')) {
				length += 5;
				index += 5;
			}

			if ((strLength > index + 5) && (charArray[(index + 1)] == 'q')
					&& (charArray[(index + 2)] == 'u')
					&& (charArray[(index + 3)] == 'o')
					&& (charArray[(index + 4)] == 't')
					&& (charArray[(index + 5)] == ';')) {
				length += 5;
				index += 5;
			}

			if ((strLength > index + 6) && (charArray[(index + 1)] == 'a')
					&& (charArray[(index + 2)] == 'c')
					&& (charArray[(index + 3)] == 'u')
					&& (charArray[(index + 4)] == 't')
					&& (charArray[(index + 5)] == 'e')
					&& (charArray[(index + 6)] == ';')) {
				length += 6;
				index += 6;
			}

			if ((strLength > index + 6) && (charArray[(index + 1)] == 'c')
					&& (charArray[(index + 2)] == 'e')
					&& (charArray[(index + 3)] == 'd')
					&& (charArray[(index + 4)] == 'i')
					&& (charArray[(index + 5)] == 'l')
					&& (charArray[(index + 6)] == ';')) {
				length += 6;
				index += 6;
			}

		}

		String returnStr = str.substring(0, index);

		returnStr = returnStr + "..";
		return returnStr;
	}

	public static int getStrLength(String str) {
		if ((str == null) || ("".equals(str))) {
			return 0;
		}
		char[] charArray = str.toCharArray();
		int length = 0;
		int strLength = str.length();

		for (int i = 0; i < charArray.length; ++i) {
			if (((charArray[i] >= 13312) && (charArray[i] < 40959))
					|| (charArray[i] >= 63744)) {
				length += 2;
			} else if (charArray[i] == '&') {
				if ((strLength > i + 3) && (charArray[(i + 1)] == 'l')
						&& (charArray[(i + 2)] == 't')
						&& (charArray[(i + 3)] == ';')) {
					++length;
					i += 3;
				}

				if ((strLength > i + 4) && (charArray[(i + 1)] == '#')
						&& (charArray[(i + 2)] == '4')
						&& (charArray[(i + 3)] == '6')
						&& (charArray[(i + 4)] == ';')) {
					++length;
					i += 4;
				}

				if ((strLength > i + 5) && (charArray[(i + 1)] == 'n')
						&& (charArray[(i + 2)] == 'b')
						&& (charArray[(i + 3)] == 's')
						&& (charArray[(i + 4)] == 'p')
						&& (charArray[(i + 5)] == ';')) {
					++length;
					i += 5;
				}

				if ((strLength > i + 5) && (charArray[(i + 1)] == 'q')
						&& (charArray[(i + 2)] == 'u')
						&& (charArray[(i + 3)] == 'o')
						&& (charArray[(i + 4)] == 't')
						&& (charArray[(i + 5)] == ';')) {
					++length;
					i += 5;
				}

				if ((strLength > i + 6) && (charArray[(i + 1)] == 'a')
						&& (charArray[(i + 2)] == 'c')
						&& (charArray[(i + 3)] == 'u')
						&& (charArray[(i + 4)] == 't')
						&& (charArray[(i + 5)] == 'e')
						&& (charArray[(i + 6)] == ';')) {
					++length;
				}

				if ((strLength > i + 6) && (charArray[(i + 1)] == 'c')
						&& (charArray[(i + 2)] == 'e')
						&& (charArray[(i + 3)] == 'd')
						&& (charArray[(i + 4)] == 'i')
						&& (charArray[(i + 5)] == 'l')
						&& (charArray[(i + 6)] == ';')) {
					++length;
					i += 6;
				}
			} else {
				++length;
			}
		}
		return length;
	}

	public static String distict(String[] strA) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < strA.length; ++i) {
			sb.append(strA[i]);
		}
		String s = sb.toString();

		if (strA == null) {
			return null;
		}
		StringBuffer reStr = new StringBuffer();
		for (int i = 0; i < strA.length; ++i) {
			int j = 0;
			for (; j < strA.length; ++j) {
				if ((i != j) && (strA[i].equals(strA[j]))) {
					break;
				}
			}
			if (j == strA.length) {
				reStr.append(strA[i]);
				reStr.append(",");
			}
		}
		if (reStr.toString().equals(",")) {
			return "";
		}

		return reStr.toString();
	}

	public static String unescape(String src) {
		if (src == null)
			return null;

		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length());
		int lastPos = 0;
		int pos = 0;

		while (lastPos < src.length()) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					char ch = (char) Integer.parseInt(
							src.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				} else {
					char ch = (char) Integer.parseInt(
							src.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			} else if (pos == -1) {
				tmp.append(src.substring(lastPos));
				lastPos = src.length();
			} else {
				tmp.append(src.substring(lastPos, pos));
				lastPos = pos;
			}
		}

		return tmp.toString();
	}

//	public static String getPinYin(String src) {
//		char[] t1 = null;
//		t1 = src.toCharArray();
//
//		String[] t2 = new String[t1.length];
//
//		HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
//		t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
//		t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
//		t3.setVCharType(HanyuPinyinVCharType.WITH_V);
//		String t4 = "";
//		int t0 = t1.length;
//		try {
//			for (int i = 0; i < t0; ++i) {
//				if (Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
//					t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
//					t4 = t4 + t2[0];
//				} else {
//					t4 = t4 + Character.toString(t1[i]);
//				}
//			}
//		} catch (BadHanyuPinyinOutputFormatCombination e) {
//			e.printStackTrace();
//		}
//		return t4;
//	}

	public static String getRandomByWeight(List<String> aip,
			List<Integer> aweight) {
		List result = new ArrayList();
		int total = 0;
		for (int i = 0; i < aweight.size(); ++i) {
			int weight = ((Integer) aweight.get(i)).intValue();
			total += weight;
			if (i > 0) {
				weight += ((Integer) result.get(i - 1)).intValue();
			}
			result.add(Integer.valueOf(weight));
		}

		Random random = new Random();
		int value = random.nextInt(total + 1);
		if (value == 0) {
			return ((String) aip.get(0));
		}
		for (int i = 0; i < result.size(); ++i) {
			int val = ((Integer) result.get(i)).intValue();
			if (value <= val) {
				return ((String) aip.get(i));
			}
		}
		return ((String) aip.get(0));
	}

	public static Integer getRandomByRand(Integer rand) {
		Random random = new Random();
		return Integer.valueOf(Math.abs(random.nextInt()) % rand.intValue());
	}

	public static Integer toInteger(String s) {
		return toInteger(s, null);
	}

	public static Integer toInteger(String s, Integer defaultValue) {
		if ((s == null) || ("".equals(s.trim())))
			return defaultValue;
		try {
			return Integer.valueOf(Integer.parseInt(s.trim()));
		} catch (NumberFormatException nfe) {
		}
		return defaultValue;
	}

	public static void main(String[] args) {
		String[] ips = { "ip1", "ip2" };

		for (int i = 0; i < 100; ++i)
			System.out.println(getRandomByRand(Integer.valueOf(ips.length)));
		
		System.out.println(replace("root>body>username",">",":"));
	}

	public static String transforStr(String str) {
		return str.replaceAll("'", "''");
	}
}