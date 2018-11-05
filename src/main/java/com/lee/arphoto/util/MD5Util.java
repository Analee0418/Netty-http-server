package com.lee.arphoto.util;

import java.security.MessageDigest;

/**
 * MD5工具
 */
public class MD5Util {

	/**
	 * 生成md5
	 * 
	 * @param message
	 * @return
	 */
	public static String getMD5(String message) {
		String md5str = "";
		try {
			// 1 创建一个提供信息摘要算法的对象，初始化为md5算法对象
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 2 将消息变成byte数组
			byte[] input = message.getBytes();
			// 3 计算后获得字节数组,这就是那128位了
			byte[] buff = md.digest(input);
			// 4 把数组每一字节（一个字节占八位）换成16进制连成md5字符串
			md5str = bytesToHex(buff);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return md5str;
	}

	/**
	 * 生成md5
	 * 
	 * @param input
	 * @return
	 */
	public static String getMD5(byte[] input) {
		String md5str = "";
		try {
			// 1 创建一个提供信息摘要算法的对象，初始化为md5算法对象
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 3 计算后获得字节数组,这就是那128位了
			byte[] buff = md.digest(input);
			// 4 把数组每一字节（一个字节占八位）换成16进制连成md5字符串
			md5str = bytesToHex(buff);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return md5str;
	}

	/**
	 * 二进制转十六进制
	 * 
	 * @param bytes
	 * @return
	 */
	private static String bytesToHex(byte[] bytes) {
		StringBuffer md5str = new StringBuffer();
		// 把数组每一字节换成16进制连成md5字符串
		int digital;
		for (int i = 0; i < bytes.length; i++) {
			digital = bytes[i];

			if (digital < 0) {
				digital += 256;
			}
			if (digital < 16) {
				md5str.append("0");
			}
			md5str.append(Integer.toHexString(digital));
		}
		return md5str.toString().toUpperCase();
	}

	// main测试类
	public static void main(String[] args) {
		String result000 = getMD5(
				"b6da5900312eaaae2b3f78b9073106f0amount=1.00&extra=201408036987&orderid=100001_10001_1407811507_9347&serverid=0&ts=1407811565&uid=10001");
		String result111 = getMD5(
				"b6da5900312eaaae2b3f78b9073106f0amount=1.00&extra=201408036987&orderid=100001_10001_1407811507_9347&ts=1407811565&uid=10001&serverid=0");
		System.err.println("==" + result000);
		System.err.println("==" + result111);
	}
}
