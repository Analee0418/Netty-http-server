package com.lee.arphoto.message;

import java.util.ArrayList;
import java.util.List;

import com.lee.arphoto.exception.ErrorCode;

public class MessageAppError extends AbstractMessage {
	private ErrorCode code;
	private List<String> params;
	private String codeMessage;

	public MessageAppError(ErrorCode code, String... params) {
		super();
		this.code = code;
		this.params = new ArrayList<>();
		for (String str : params) {
			this.params.add(str);
		}
		this.codeMessage = code.getMsg();
	}

	public MessageAppError(String systemErrorMessage) {
		this.code = ErrorCode.ErrorServer;
		this.params = new ArrayList<>();
		this.codeMessage = systemErrorMessage;
	}

	public ErrorCode getCode() {
		return code;
	}

	public void setCode(ErrorCode code) {
		this.code = code;
	}

	public List<String> getParams() {
		return params;
	}

	public void setParams(List<String> params) {
		this.params = params;
	}

	public String getCodeMessage() {
		return codeMessage;
	}

	public void setCodeMessage(String codeMessage) {
		this.codeMessage = codeMessage;
	}

	// @Override
	// public String getJson() {
	// JsonObject obj = new JsonObject();
	//
	// StringBuilder sb = new StringBuilder();
	// if (code.getMsg().indexOf("#") != -1) {
	// String[] msgTemplate = code.getMsg().split("#");
	//
	// int i = 0;
	// for (String str : msgTemplate) {
	// sb.append(str);
	// if (null != this.params && i < this.params.size()) {
	// sb.append(this.params.get(i));
	// }
	// i++;
	// }
	// } else {
	// sb.append(code.getMsg());
	// }
	//
	// obj.addProperty("ErrorMessage", sb.toString());
	// return obj.toString();
	// }

}
