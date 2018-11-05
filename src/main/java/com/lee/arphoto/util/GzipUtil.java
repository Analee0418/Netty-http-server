package com.lee.arphoto.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

/**
 * gzip、zip压缩与解压缩字符串工具
 */
public class GzipUtil {

	/**
	 * 默认编码
	 */
	public static final String DEFAULT_ENCODING = "UTF-8";

	/**
	 * 使用gzip对字符串进行压缩，并将压缩后的字节数据用Base64进行转码
	 * 
	 * @param params
	 * @return
	 */
	public static String gzip(String params) {
		if (StringUtils.isEmpty(params)) {
			return params;
		}
		ByteArrayOutputStream out = null;
		GZIPOutputStream gzip = null;
		try {
			out = new ByteArrayOutputStream();
			gzip = new GZIPOutputStream(out);
			gzip.write(params.getBytes(DEFAULT_ENCODING));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (gzip != null) {
					gzip.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		System.out.println(">>>>>>>::: " + new String(out.toByteArray()));

		return new String(Base64Utils.encode(out.toByteArray()));
	}

	public static byte[] gzipToByteArray(String params) {
		if (StringUtils.isEmpty(params)) {
			return new byte[] {};
		}
		ByteArrayOutputStream out = null;
		GZIPOutputStream gzip = null;
		try {
			out = new ByteArrayOutputStream();
			gzip = new GZIPOutputStream(out);
			gzip.write(params.getBytes(DEFAULT_ENCODING));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (gzip != null) {
					gzip.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return out.toByteArray();
	}

	public static String upgzipFromByteArray(byte[] arr) {
		if (arr == null || arr.length <= 0) {
			return null;
		}
		ByteArrayInputStream in = null;
		GZIPInputStream ungzip = null;
		ByteArrayOutputStream out = null;
		String ungzipStr = null;
		try {
			in = new ByteArrayInputStream(arr);
			ungzip = new GZIPInputStream(in);
			out = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int offset = -1;
			while ((offset = ungzip.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
			}
			ungzipStr = out.toString(DEFAULT_ENCODING);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (ungzip != null) {
					ungzip.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ungzipStr;
	}

	/**
	 * 使用gzip对压缩过的字符串进行解压缩，先将字符串进行Base64转码，再解压缩
	 * 
	 * @param params
	 * @return
	 */
	public static String ungzip(String params) {
		if (StringUtils.isEmpty(params)) {
			return params;
		}
		ByteArrayInputStream in = null;
		GZIPInputStream ungzip = null;
		ByteArrayOutputStream out = null;
		String ungzipStr = null;
		try {
			in = new ByteArrayInputStream(Base64Utils.decode(params.getBytes()));
			ungzip = new GZIPInputStream(in);
			out = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int offset = -1;
			while ((offset = ungzip.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
			}
			ungzipStr = out.toString(DEFAULT_ENCODING);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (ungzip != null) {
					ungzip.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ungzipStr;
	}

	/**
	 * 使用zip对字符串进行压缩，并将压缩后的字节数据用Base64进行转码
	 * 
	 * @param params
	 * @return
	 */
	public static String zip(String params) {
		if (StringUtils.isEmpty(params)) {
			return params;
		}
		ByteArrayOutputStream out = null;
		ZipOutputStream zip = null;
		try {
			out = new ByteArrayOutputStream();
			zip = new ZipOutputStream(out);
			zip.putNextEntry(new ZipEntry("0"));
			zip.write(params.getBytes(DEFAULT_ENCODING));
			zip.closeEntry();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (zip != null) {
					zip.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new String(Base64Utils.encode(out.toByteArray()));
	}

	public static byte[] zipFromByteString(String params) {
		if (StringUtils.isEmpty(params)) {
			return null;
		}
		ByteArrayOutputStream out = null;
		ZipOutputStream zip = null;
		try {
			out = new ByteArrayOutputStream();
			zip = new ZipOutputStream(out);
			zip.setLevel(9);
			zip.putNextEntry(new ZipEntry("9"));
			zip.write(params.getBytes(DEFAULT_ENCODING));
			zip.closeEntry();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (zip != null) {
					zip.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return out.toByteArray();
	}

	/**
	 * 使用zip对压缩过的字符串进行解压缩，先将字符串进行Base64转码，再解压缩
	 * 
	 * @param params
	 * @return
	 */
	public static String unzip(String params) {
		if (StringUtils.isEmpty(params)) {
			return params;
		}
		ByteArrayInputStream in = null;
		ZipInputStream unzip = null;
		ByteArrayOutputStream out = null;
		String unzipStr = null;
		try {
			in = new ByteArrayInputStream(Base64Utils.decode(params.getBytes()));
			unzip = new ZipInputStream(in);
			unzip.getNextEntry();
			out = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int offset = -1;
			while ((offset = unzip.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
			}
			unzipStr = out.toString(DEFAULT_ENCODING);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (unzip != null) {
					unzip.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return unzipStr;
	}

	public static String unzipFromByteArray(byte[] arr) {
		if (arr == null || arr.length <= 0) {
			return null;
		}
		ByteArrayInputStream in = null;
		ZipInputStream unzip = null;
		ByteArrayOutputStream out = null;
		String unzipStr = null;
		try {
			in = new ByteArrayInputStream(arr);
			unzip = new ZipInputStream(in);
			unzip.getNextEntry();
			out = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int offset = -1;
			while ((offset = unzip.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
			}
			unzipStr = out.toString(DEFAULT_ENCODING);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (unzip != null) {
					unzip.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return unzipStr;
	}

	/**
	 * ZIP压缩
	 * 
	 * @param value
	 * @param offset
	 * @param length
	 * @return
	 */
	public static byte[] compress(byte[] value, int offset, int length) {
		return compress(value, offset, length, Deflater.BEST_COMPRESSION);
	}

	/**
	 * ZIP压缩
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] compress(byte[] value) {
		return compress(value, 0, value.length, Deflater.BEST_COMPRESSION);
	}

	/**
	 * ZIP压缩
	 * 
	 * @param value
	 * @param offset
	 * @param length
	 * @param compressionLevel
	 * @return
	 */
	public static byte[] compress(byte[] value, int offset, int length, int compressionLevel) {
		ByteArrayOutputStream out = new ByteArrayOutputStream(length);
		Deflater compressor = new Deflater();
		try {
			// 将当前压缩级别设置为指定值
			compressor.setLevel(compressionLevel);
			compressor.setInput(value, offset, length);
			// 调用时，指示压缩应当以输入缓冲区的当前内容结尾
			compressor.finish();
			// 压缩
			final byte[] buf = new byte[1024];
			while (!compressor.finished()) {
				// 如果已到达压缩数据输出流的结尾，则返回 true。
				int count = compressor.deflate(buf);
				// 使用压缩数据填充指定缓冲区。
				out.write(buf, 0, count);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (compressor != null) {
				compressor.end(); // 关闭解压缩器并放弃所有未处理的输入
			}
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return out.toByteArray();
	}

	/**
	 * ZIP解压缩
	 * 
	 * @param value
	 * @return
	 * @throws DataFormatException
	 */
	public static String decompress(byte[] value) throws DataFormatException {
		if (value == null || value.length <= 0) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream(value.length);
		Inflater decompressor = new Inflater();
		String res = null;
		try {
			decompressor.setInput(value);
			final byte[] buf = new byte[1024];
			while (!decompressor.finished()) {
				int count = decompressor.inflate(buf);
				out.write(buf, 0, count);
			}
			res = out.toString(DEFAULT_ENCODING);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (decompressor != null) {
				decompressor.end();
			}
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return res;
	}

	/**
	 * ZIP解压缩
	 * 
	 * @param value
	 * @return
	 * @throws DataFormatException
	 */
	public static byte[] decompressBytes(byte[] value) throws DataFormatException {
		if (value == null || value.length <= 0) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream(value.length);
		Inflater decompressor = new Inflater();
		try {
			decompressor.setInput(value);
			final byte[] buf = new byte[1024];
			while (!decompressor.finished()) {
				int count = decompressor.inflate(buf);
				out.write(buf, 0, count);
			}
			return out.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (decompressor != null) {
				decompressor.end();
			}
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void main(String[] args) throws Exception {

		// String s = "aaa\nssss";
		// String[] ss = s.split("\n");

		String str = "action=login&user=pk&pwd=123";
		String gzipStr = gzip(str);
		System.out.println("====压缩后的字符串==" + gzipStr);
		for (int i = 0; i < gzipStr.length(); i++) {
			System.out.println("aaa " + i + " " + (int) gzipStr.charAt(i));
		}
		String ungzipStr = ungzip(gzipStr);
		System.out.println("====解压后的字符串==" + ungzipStr);

	}
}
