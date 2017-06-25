package org.simpleproxy.random;

import java.net.InetAddress;

/**
 * UUID.HEX十六进制生成器：IP地址+JVM+当地时间+计数器
 * @param args
 * @author lmb
 * @date 2017-6-5
 */
public class UUIDHexGenerator {

    private String sep = "";
    private static final int IP;
    private static short counter = (short) 0;
    private static final int JVM = (int)(System.currentTimeMillis() >>> 8);
    private static UUIDHexGenerator uuidgen = new UUIDHexGenerator();

    //获得本机的IP地址
    static {
       int ipadd;
       try {
        ipadd = toInt(InetAddress.getLocalHost().getAddress());
       } catch (Exception e) {
        ipadd = 0;
       }
       IP = ipadd;
    }

    /**
     * 获取UUIDHexGenerator实例对象
     * @return
     */
    public static UUIDHexGenerator getInstance() {
       return uuidgen;
    }

    /**
     * 将byte类型变量转化为Int类型
     * @param bytes
     * @return
     */
    public static int toInt(byte[] bytes) {
       int result = 0;
       for (int i = 0; i < 4; i++) {
        result = (result << 8) - Byte.MIN_VALUE + (int) bytes[i];
       }
       return result;
    }

    /**
     * 将int类型变量转化为string类型
     * @param intval
     * @return
     */
    protected String format(int intval) {
       String formatted = Integer.toHexString(intval);
       StringBuffer buf = new StringBuffer("00000000");
       buf.replace(8 - formatted.length(), 8, formatted);
       return buf.toString();
    }

    /**
     * 将byte类型变量转化为string类型
     * @param shortval
     * @return
     */
    protected String format(short shortval) {
       String formatted = Integer.toHexString(shortval);
       StringBuffer buf = new StringBuffer("0000");
       buf.replace(4 - formatted.length(), 4, formatted);
       return buf.toString();
    }

    protected int getJVM() {
       return JVM;
    }

    /**
     * 自增計數器
     * @return
     */
    protected synchronized short getCount() {
       if (counter < 0) {
        counter = 0;
       }
       return counter++;
    }

    protected int getIP() {
       return IP;
    }

    protected short getHiTime() {
       return (short) (System.currentTimeMillis() >>> 32);
    }

    protected int getLoTime() {
       return (int) System.currentTimeMillis();
    }

    /**
     * UUID.HEX十六进制生成器：IP地址+JVM+当地时间+计数器
     * @return
     */
    public String generate() {
       return new StringBuffer(36).append(format(getIP())).append(sep).append(
         format(getJVM())).append(sep).append(format(getHiTime()))
         .append(sep).append(format(getLoTime())).append(sep).append(
           format(getCount())).toString();
    }

    public static void main(String[] str) {
       UUIDHexGenerator uuidGenerator = new UUIDHexGenerator();
       for (int i = 0; i <= 10; i++) {
        System.out.println(uuidGenerator.generate());
       }
       System.out.println(uuidGenerator.generate().length());
    }

}
