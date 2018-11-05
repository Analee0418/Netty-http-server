package com.lee.arphoto.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件相关工具类
 */
public class FileUtil {

	/**
	 * 创建目录
	 * 
	 * @param dirname
	 * @return
	 */
	public static File createDir(String dirname) {
		File dirFile = new File(dirname);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		return dirFile;
	}

	/**
	 * 删除目录
	 * 
	 * @param dirname
	 */
	public static void deleteDir(String dirname) {
		File dirFile = createDir(dirname);
		deleteDir(dirFile);
	}

	/**
	 * 删除目录
	 * 
	 * @param dirFile
	 * @return
	 */
	public static boolean deleteDir(File dirFile) {
		if (dirFile == null) {
			return false;
		}
		if (!dirFile.isDirectory()) {
			return false;
		}
		for (File file : dirFile.listFiles()) {
			if (file.isDirectory()) {
				deleteDir(file);
			} else {
				try {
					file.delete();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		try {
			return dirFile.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 读取文件内容
	 * 
	 * @param fileName
	 * @param encoding
	 * @return file content
	 */
	public static String readStringFile(String fileName, String encoding) {
		StringBuffer sb = new StringBuffer();
		try {
			FileInputStream fis = new FileInputStream(fileName);
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis, encoding));
			sb = new StringBuffer();
			while (reader.ready()) {
				String line = reader.readLine();
				sb.append(line);
				sb.append("\r\n");
			}
			reader.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 读取文件内容
	 */
	public static byte[] readBytesFile(String fileName) {
		byte[] result = null;

		File file = new File(fileName);
		if (file.exists()) {

			ByteArrayOutputStream outputStream = null;
			try {
				outputStream = new ByteArrayOutputStream();

				FileInputStream fileInputStream = new FileInputStream(file);
				DataInputStream dataInputStream = new DataInputStream(fileInputStream);

				byte[] itemBuf = new byte[1024];
				while (true) {
					int length = dataInputStream.read(itemBuf, 0, itemBuf.length);

					outputStream.write(itemBuf);
					if (length < 0) {
						break;
					}
				}

				result = outputStream.toByteArray();

				dataInputStream.close();
				fileInputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 * 文件是否存在
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean hasExist(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}

	/**
	 * 读取文件内容
	 * 
	 * @param fileName
	 * @return file content
	 */
	public static String readStringFileByUTF(String fileName) {
		return readStringFile(fileName, "UTF-8");
	}

	/**
	 * 读取文件内容
	 * 
	 * @param fileName
	 * @return file content
	 */
	public static String readStringFileByGBK(String fileName) {
		return readStringFile(fileName, "GBK");
	}

	/**
	 * 读取文件内容
	 * 
	 * @param fileName
	 * @return file content
	 */
	public static String readStringFileByISO(String fileName) {
		return readStringFile(fileName, "ISO-8859-1");
	}

	/**
	 * 将内容写入文件里
	 * 
	 * @param fileContent
	 * @param fileName
	 * @param encoding
	 * @return add access or fail
	 */
	public static boolean writeStringFile(String fileContent, String fileName, String encoding) {
		try {
			createDir(getFileNamePath(fileName));
			File file = new File(fileName);
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			byte[] b = fileContent.getBytes(encoding);
			fileOutputStream.write(b);
			fileOutputStream.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将内容写入文件里
	 * 
	 * @param bytes
	 * @param fileName
	 * @return add access or fail
	 */
	public static File writeBytesFile(byte[] bytes, String fileName) {
		try {
			createDir(getFileNamePath(fileName));
			File file = new File(fileName);
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(bytes);
			fileOutputStream.close();
			return file;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 得到文件路径
	 * 
	 * @param fileName
	 * @return file real path
	 */
	public static String getFileNamePath(String fileName) {
		int pos = fileName.lastIndexOf("\\");
		int pos2 = fileName.lastIndexOf("/");
		if (pos == -1 && pos2 == -1) {
			return "";
		} else {
			if (pos2 > pos) {
				return fileName.substring(0, pos2);
			} else {
				return fileName.substring(0, pos);
			}
		}
	}

	public static void main(String[] args) {
		File file = new File("adf.jsp");
		System.out.println(getFileExtName(file));

		String content = FileUtil.readStringFileByUTF("src/main/resources/bannedWords.txt");
		String[] split = content.split("\r\n");
		List<String> words = new ArrayList<String>();
		for (String word : split) {
			if (!words.contains(word)) {
				words.add(word);
			} else {
				System.err.println("exist " + word);
			}
		}

		System.out.println(split.length + ", " + words.size());

		StringBuffer sb = new StringBuffer();
		int index = 0;
		for (String word : words) {
			sb.append(word);
			index++;
			if (index < words.size()) {
				sb.append("\r\n");
			}
		}
		FileUtil.writeStringFile(sb.toString(), "src/main/resources/bannedWordsNew.txt", "utf-8");
	}

	public static String filterEmoji(String source) {
		if (source != null) {
			Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
					Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
			Matcher emojiMatcher = emoji.matcher(source);
			if (emojiMatcher.find()) {
				source = emojiMatcher.replaceAll("*");
				return source;
			}
			return source;
		}
		return source;
	}

	@SuppressWarnings({ "resource" })
	public static File forTransfer(File f1, File f2) throws Exception {
		int length = 2097152;
		FileInputStream in = new FileInputStream(f1);
		FileOutputStream out = new FileOutputStream(f2);

		FileChannel inC = in.getChannel();
		FileChannel outC = out.getChannel();
		int i = 0;
		while (true) {
			if (inC.position() == inC.size()) {
				inC.close();
				outC.close();

				return f2;
			}
			if ((inC.size() - inC.position()) < 20971520)
				length = (int) (inC.size() - inC.position());
			else
				length = 20971520;
			inC.transferTo(inC.position(), length, outC);
			inC.position(inC.position() + length);
			i++;

			if (i >= 100) {
				return null;
			}
		}
	}

	public static String getFileExtName(File f) {
		String fileName = f.getName();
		String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
		return prefix;
	}
}
