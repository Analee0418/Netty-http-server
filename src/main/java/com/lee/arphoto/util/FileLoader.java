package com.lee.arphoto.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;

/**
 * 
 */
public class FileLoader {

	public static final Log logger = LogFactory.getLog(FileLoader.class);

	private static final String PREFIX_FILENAME = "../super-config/";

	public static Object loadFile(String fileName, Class<?> jsonclass) {
		String jsonStr = loadJsonStr(PREFIX_FILENAME + fileName);
		if (StringUtils.isEmpty(jsonStr)) {
			return null;
		}
		return new Gson().fromJson(jsonStr, jsonclass);
	}

	public static String loadJsonStr(String fileName) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "utf-8"));
			StringBuffer sb = new StringBuffer();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
