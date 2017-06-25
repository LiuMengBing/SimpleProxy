package org.simpleproxy.dataparse;

import java.text.DecimalFormat;
import java.util.Calendar;

import org.apache.commons.lang.StringUtils;
/**
 * 功能：数学计算工具类
 * @author lmb 
 * @version 1.0
 * @date 2017-06-01
 */
public class MathUtil {

	public static String ZERO ="0";
	public static String MBPATTERN = "0.00";//双精度.
	private static DecimalFormat mbdf = new DecimalFormat(MBPATTERN);
	/**
	 * 计算double型字串的和（根据pattern指定的精度保留对应小数位）
	 * @param dbStr1
	 * @param dbStr2
	 * @param pattern 
	 * 			支持DecimalFormat所支持的pattern.  如传0.00 则表示返回的数据字串需要保留2位小时 , 0.0则是一位.
	 * 			如pattern参数为null为返回双精度
	 * @return
	 */
	public static String sumDoubleStr(String dbStr1,String dbStr2,String pattern){
	   double db1 = Double.parseDouble( (StringUtils.isNotEmpty(dbStr1) ? dbStr1 :"0"));
	   double db2 = Double.parseDouble( (StringUtils.isNotEmpty(dbStr2) ? dbStr2 :"0")); 
	   //log.info("in sumDoubleStr:"+db1+","+db2);
	   DecimalFormat df ;
	   if(pattern!=null &&  !"".equals(pattern.trim()) )
			df= new DecimalFormat(pattern);
	   else{
		    df = mbdf;
	   }
		return df.format(db1 +db2);
	}
	
	public static void main(String[] args) {
		System.out.println(sumDoubleStr("189.20","34.289","0.00"));//223.49
		System.out.println(Calendar.getInstance());
	}

}
