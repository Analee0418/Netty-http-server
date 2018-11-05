package com.lee.arphoto.exception;

/**
 * 错误消息自定义异常
 * 
 * @author xiaofei.lee(13651027213@163.com)
 *
 */
public class AppErrorException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5569095730494804428L;

	private ErrorCode code;
	private String[] args;

	public AppErrorException(ErrorCode code, String... args) {
		super();
		this.code = code;
		this.args = args;
	}

	public ErrorCode getCode() {
		return code;
	}

	public String[] getArgs() {
		return args;
	}

	public String getArgsToString() {
		if (null == this.args) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (String str : args) {
			sb.append(str).append(",");
		}
		return sb.toString();
	}

}
