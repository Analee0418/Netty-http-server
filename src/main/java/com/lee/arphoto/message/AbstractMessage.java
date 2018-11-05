package com.lee.arphoto.message;

import java.nio.charset.Charset;

import com.google.gson.Gson;

public class AbstractMessage {
	public String getJson() {
		return new Gson().toJson(this);
	}

	public byte[] getBytes() {
		String jsonStr = this.getJson();
		return jsonStr.getBytes(Charset.forName("UTF-8"));
	}
}
