package com.lee.arphoto.exception;

public enum ErrorCode {
	Success(200, "成功"),

	ErrorParameter(1001, "参数错误"),

	ErrorServer(1002, "服务器错误"),

	ErrorNoLogin(1003, "登录状态失效，请重新登录"),

	ErrorRepeateUserNameRegist(1004, "该账号已经注册"),

	ErrorPassword(1005, "密码错误"),

	ErrorFileUploadGetOnly(1006, "抱歉，文件上传协议必须使用post请求"),

	ErrorFileUploadVideoInvalid(1007, "必须有一个视频");

	private int code;
	private String msg;

	private ErrorCode(int _code, String _msg) {
		this.code = _code;
		this.msg = _msg;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

}
