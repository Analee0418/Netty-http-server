package com.lee.arphoto.util;

import java.io.UnsupportedEncodingException;

import org.springframework.util.StringUtils;

/**
 * XXTEA加密/解密
 * 
 * @auther zw
 */
public class XXTEA {

	/** 密钥 **/
	private static final String key = "ke&Bx>I3lOn0amqu";

	/**
	 * 十六进制表示符
	 */
	private static final String HEX_STR = "0123456789ABCDEF";

	/**
	 * 先压缩后加密
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] zipEncryptToBytes(byte[] data) {
		if (StringUtils.isEmpty(data)) {
			return null;
		}
		try {
			return encryptToBytes(GzipUtil.compress(data), key.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 加密
	 * 
	 * @param data
	 * @param key
	 * @return
	 */
	private static byte[] encryptToBytes(byte[] data, byte[] key) {
		if (data == null || data.length <= 0) {
			return null;
		}
		return encrypt(data, key);
	}

	/**
	 * 加密
	 * 
	 * @param data
	 * @param key
	 * @return
	 */
	private static byte[] encrypt(byte[] data, byte[] key) {
		if (data == null || data.length <= 0) {
			return null;
		}
		return toByteArray(encrypt(toIntArray(data, true), toIntArray(key, false)), false);
	}

	/**
	 * 解密之后解压缩
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] unzipDecryptToBytes(byte[] data) {
		if (null == data || 0 >= data.length) {
			return null;
		}

		try {
			byte[] res = decrypt(data, key.getBytes("UTF-8"));
			return GzipUtil.decompressBytes(res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密
	 * 
	 * @param data
	 * @param key
	 * @return
	 */
	private static byte[] decrypt(byte[] data, byte[] key) {
		if (data == null || data.length <= 0) {
			return data;
		}

		byte[] re = toByteArray(decrypt(toIntArray(data, false), toIntArray(key, false)), true);
		return re;
	}

	/**
	 * 加密
	 * 
	 * @param v
	 * @param k
	 * @return
	 */
	private static int[] encrypt(int[] v, int[] k) {
		if (v == null || v.length <= 0) {
			return null;
		}
		int n = v.length;
		int y;
		int p;
		int rounds = 6 + 52 / n;
		int sum = 0;
		int z = v[n - 1];
		int delta = 0x9E3779B9;
		do {
			sum += delta;
			int e = (sum >>> 2) & 3;
			for (p = 0; p < n - 1; p++) {
				y = v[p + 1];
				z = v[p] += (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
			}
			y = v[0];
			z = v[n - 1] += (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
		} while (--rounds > 0);

		return v;
	}

	/**
	 * 解密
	 * 
	 * @param v
	 * @param k
	 * @return
	 */
	private static int[] decrypt(int[] v, int[] k) {
		if (v == null || v.length <= 0) {
			return null;
		}
		int n = v.length;
		int z = v[n - 1], y = v[0], delta = 0x9E3779B9, sum, e;
		int p;
		int rounds = 6 + 52 / n;
		sum = rounds * delta;
		y = v[0];
		do {
			e = (sum >>> 2) & 3;
			for (p = n - 1; p > 0; p--) {
				z = v[p - 1];
				y = v[p] -= (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
			}
			z = v[n - 1];
			y = v[0] -= (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
		} while ((sum -= delta) != 0);
		return v;
	}

	/**
	 * byte数组转为int数组
	 * 
	 * @param data
	 * @param includeLength
	 * @return
	 */
	private static int[] toIntArray(byte[] data, boolean includeLength) {
		if (data == null || data.length <= 0) {
			return null;
		}
		int n = (((data.length & 3) == 0) ? (data.length >>> 2) : ((data.length >>> 2) + 1));
		int[] result;
		if (includeLength) {
			result = new int[n + 1];
			result[n] = data.length;
		} else {
			result = new int[n];
		}
		n = data.length;
		for (int i = 0; i < n; i++) {
			result[i >>> 2] |= (0x000000ff & data[i]) << ((i & 3) << 3);
		}
		return result;
	}

	/**
	 * int数组转为byte数组
	 * 
	 * @param data
	 * @param includeLength
	 * @return
	 */
	private static byte[] toByteArray(int[] data, boolean includeLength) {
		if (data == null || data.length <= 0) {
			return null;
		}
		int n = data.length << 2;
		if (includeLength) {
			int m = data[data.length - 1];
			if (m > n) {
				return null;
			} else {
				n = m;
			}
		}
		byte[] result = new byte[n];

		for (int i = 0; i < n; i++) {
			result[i] = (byte) ((data[i >>> 2] >>> ((i & 3) << 3)) & 0xff);
		}
		return result;
	}

	/**
	 * 将十六进制字符串转为字节数组
	 * 
	 * @param hexString
	 * @return
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (StringUtils.isEmpty(hexString)) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * 字符转字节
	 * 
	 * @param c
	 * @return
	 */
	private static byte charToByte(char c) {
		return (byte) HEX_STR.indexOf(c);
	}

	public static String bytes2HexString(byte[] b) {
		String ret = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			ret += hex.toUpperCase();
		}
		return ret;
	}

	public static void main(String[] args) throws Exception {

		// String kkk =
		// "c85306f04b76af487a222b41e0b06395acc7f795934e963ac91a9bdaccdb037dd2a1cbf4828d8877";

		// String lll = zipEncryptToString(kkk);
		//
		// System.out.println("==" + lll);

		// System.out.println("解密解压后===" + unzipDecryptToString(kkk));

		byte[] s = new byte[1024];

		byte[] cs = zipEncryptToBytes(s);

		System.out.println(s.length + " : " + cs.length);

		s = null;
		s = unzipDecryptToBytes(cs);

		System.out.println(s);

	}
}
