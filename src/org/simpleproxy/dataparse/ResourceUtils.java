package org.simpleproxy.dataparse;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Properties;
/**
 * 功能：资源读取工具类
 * @author lmb 
 * @version 1.0
 * @date 2017-06-01
 */
public class ResourceUtils {
	/**
	 * 
	 * 功能说明:根据资源路径取得URL
	 * @param:  resource
	 * @return: URL  
	 * @author:刘明
	 *  
	 */
	public static URL getResourceURL(String resource) throws IOException {
		URL url = null;
		ClassLoader loader = ResourceUtils.class.getClassLoader();
		if (loader != null) {
			url = loader.getResource(resource);
		}
		if (url == null) {
			url = ClassLoader.getSystemResource(resource);
		}
		if (url == null) {
			throw new IOException("Could not find resource " + resource);
		}
		return url;
	}

	/**
	 * 
	 * 功能说明:根据资源路径取得URL
	 * @param:  ClassLoader
	 * @param:  resource
	 * @return: URL  
	 * @author:刘明
	 *  
	 */
	public static URL getResourceURL(ClassLoader loader, String resource)
			throws IOException {
		URL url = null;
		if (loader != null) {
			url = loader.getResource(resource);
		}
		if (url == null) {
			url = ClassLoader.getSystemResource(resource);
		}
		if (url == null) {
			throw new IOException("Could not find resource " + resource);
		}
		return url;
	}

	/**
	 * 
	 * 功能说明:根据资源路径取得输入流
	 * @param:  resource
	 * @return: InputStream  
	 * @author:刘明
	 *  
	 */
	public static InputStream getResourceAsStream(String resource)
			throws IOException {
		InputStream in = null;
		ClassLoader loader = ResourceUtils.class.getClassLoader();
		if (loader != null) {
			in = loader.getResourceAsStream(resource);
		}
		if (in == null) {
			in = ClassLoader.getSystemResourceAsStream(resource);
		}
		if (in == null) {
			throw new IOException("Could not find resource " + resource);
		}
		return in;
	}

	/**
	 * 
	 * 功能说明:根据资源路径取得输入流
	 * @param:  ClassLoader
	 * @param:  resource
	 * @return: Properties  
	 * @author:刘明
	 * @throws Exception 
	 *  
	 */
	public static InputStream getResourceAsStream(ClassLoader loader, String resource) throws Exception {
		InputStream in = null;
		if (loader != null) {
			in = loader.getResourceAsStream(resource);
		}
		if (in == null) {
			in = ClassLoader.getSystemResourceAsStream(resource);
		}
		if (in == null) {
			throw new Exception("Could not find resource "+ resource);
		}
		return in;
	}
	/**
	 * 
	 * 功能说明:根据资源路径取得Properties
	 * @param:  resource
	 * @return: InputStream  
	 * @author:刘明
	 * @throws Exception 
	 *  
	 */
	public static Properties getResourceAsProperties(String resource) throws Exception {
		Properties props = new Properties();
		InputStream in = null;
		String propfile = resource;
		try {
			in = getResourceAsStream(propfile);
			props.load(in);
			return props;
		} catch (IOException e) {
			throw new Exception(e);
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					throw new Exception(e);
				}
		}
	}

	/**
	 * 
	 * 功能说明:根据资源路径取得Properties
	 * @param:  ClassLoader
	 * @param:  resource
	 * @return: Properties  
	 * @author:刘明
	 * @throws Exception 
	 *  
	 */
	public static Properties getResourceAsProperties(ClassLoader loader, String resource) throws Exception {
		Properties props = new Properties();
		InputStream in = null;
		String propfile = resource;
		try {
			in = getResourceAsStream(loader, propfile);
			props.load(in);
			return props;
		} catch (IOException e) {
			throw new Exception(e);
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					throw new Exception(e);
				}
		}
	}

	/**
	 * 
	 * 功能说明:根据资源路径取得输入流
	 * @param:  resource
	 * @return: InputStreamReader  
	 * @author:刘明
	 * @throws Exception 
	 *  
	 */
	public static InputStreamReader getResourceAsReader(String resource) throws Exception {
		try {
			return new InputStreamReader(getResourceAsStream(resource), "UTF-8");
		}  catch (Exception e) {
			throw new Exception(e);
		}
	}

	/**
	 * 
	 * 功能说明:根据资源路径取得输入流
	 * @param:  resource
	 * @return: Reader  
	 * @author:刘明
	 * @throws Exception 
	 *  
	 */
	public static Reader getResourceAsReader(ClassLoader loader, String resource) throws Exception {
		try {
			return new InputStreamReader(getResourceAsStream(loader, resource),
					"UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new Exception(e);
		}
	}

	/**
	 * 
	 * 功能说明:根据资源路径取得文件流
	 * @param:  resource
	 * @return: File  
	 * @author:刘明
	 *  
	 */
	public static File getResourceAsFile(String resource) throws IOException {
		return new File(getResourceURL(resource).getFile());
	}

	/**
	 * 
	 * 功能说明:根据资源路径取得文件流
	 * @param:  ClassLoader
	 * @param:  resource
	 * @return: File  
	 * @author:刘明
	 *  
	 */
	public static File getResourceAsFile(ClassLoader loader, String resource)
			throws IOException {
		return new File(getResourceURL(loader, resource).getFile());
	}
	
	/**
	 * 获取log4j配置文件的路径
	 * @return
	 */
	public static String getLog4jPath(){
		String classPath = ResourceUtils.class.getClassLoader().getResource("/").getPath();
		  String rootPath  = "";//项目根目录
		  String log4jXmlPath ="";
		  if("\\".equals(File.separator)){//windows
		   rootPath  = classPath.substring(1,classPath.indexOf("/WEB-INF/classes"));
		   rootPath = rootPath.replace("/", "\\");
		   log4jXmlPath  =  rootPath + "\\WEB-INF\\classes\\log4j.xml";
		  }
		  if("/".equals(File.separator)){//linux
		   rootPath  = classPath.substring(0,classPath.indexOf("/WEB-INF/classes"));
		   rootPath = rootPath.replace("\\", "/");
		   log4jXmlPath =  rootPath + "/WEB-INF/classes/log4j.xml";
		  }
		  return log4jXmlPath;
	}
	
	/**
	 * 根据文件名获取文件路径
	 * @param fileName
	 * @return
	 */
	public static String getPath(String fileName){
		String classPath = ResourceUtils.class.getClassLoader().getResource("/").getPath();
		  String rootPath  = "";//项目根目录
		  String filePath ="";
		  if("\\".equals(File.separator)){//windows
		   rootPath  = classPath.substring(1,classPath.indexOf("/WEB-INF/classes"));
		   rootPath = rootPath.replace("/", "\\");
		   filePath  =  rootPath + "\\WEB-INF\\classes\\"+fileName;
		  }
		  if("/".equals(File.separator)){//linux
		   rootPath  = classPath.substring(0,classPath.indexOf("/WEB-INF/classes"));
		   rootPath = rootPath.replace("\\", "/");
		   filePath =  rootPath + "/WEB-INF/classes/"+fileName;
		  }
		  return filePath;
	}
	
}