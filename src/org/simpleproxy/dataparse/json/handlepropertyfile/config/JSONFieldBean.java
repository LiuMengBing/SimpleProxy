package org.simpleproxy.dataparse.json.handlepropertyfile.config;

/**
 * 功能：JSON配置文件service下的字段javabean
 * @author lmb 
 * @version 1.0
 * @date 2017-6-5
 */
public class JSONFieldBean {

	/** service下的字段名称*/
	private String name;
	/** service下的字段值*/
	private String value;
	/** service下的字段组装规则*/
	private String rule;
	/** service下的字段默认值*/
	private String defautVal;
	/**service下的请求流水号字段**/
	private String reqSerialNo;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getRule() {
		return rule;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
	public String getDefautVal() {
		return defautVal;
	}
	public void setDefautVal(String defautVal) {
		this.defautVal = defautVal;
	}
	public String getReqSerialNo() {
		return reqSerialNo;
	}
	public void setReqSerialNo(String reqSerialNo) {
		this.reqSerialNo = reqSerialNo;
	}
	
}
