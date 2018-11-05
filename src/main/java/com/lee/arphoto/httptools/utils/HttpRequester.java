package com.lee.arphoto.httptools.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * 请求发送类
 * 
 * @author zw
 */
public class HttpRequester {

	private String defaultContentEncoding;

	public String getDefaultContentEncoding() {
		return defaultContentEncoding;
	}

	public void setDefaultContentEncoding(String defaultContentEncoding) {
		this.defaultContentEncoding = defaultContentEncoding;
	}

	public HttpRequester() {
		this.defaultContentEncoding = Charset.defaultCharset().name();
	}

	public HttpRespons sendNormalMsgGet(String urlString, String params) throws IOException {
		return this.sendNormalMsg(urlString, "GET", new byte[0], params, null);
	}

	public HttpRespons sendNormalMsgPost(String urlString, byte[] data) throws IOException {
		return this.sendNormalMsg(urlString, "POST", data, null, null);
	}

	public HttpRespons sendNormalMsgPost(String urlString, String params) throws IOException {
		return this.sendNormalMsg(urlString, "POST", params.getBytes("UTF-8"), null, null);
	}

	public HttpRespons sendNormalMsgGet(String urlString, String params, Map<String, String> headers)
			throws IOException {
		return this.sendNormalMsg(urlString, "GET", new byte[0], params, headers);
	}

	public HttpRespons sendNormalMsgPost(String urlString, byte[] data, Map<String, String> headers)
			throws IOException {
		return this.sendNormalMsg(urlString, "POST", data, null, headers);
	}

	public HttpRespons sendNormalMsgPost(String urlString, String params, Map<String, String> headers)
			throws IOException {
		return this.sendNormalMsg(urlString, "POST", params.getBytes("UTF-8"), null, headers);
	}

	/**
	 * 发送消息，其内容无需压缩加密
	 * 
	 * @param urlString
	 *            URL
	 * @param method
	 *            GET/POST方法
	 * @param params
	 *            参数
	 * @return
	 * @throws IOException
	 */
	public HttpRespons sendNormalMsg(String urlString, String method, byte[] data, String params,
			Map<String, String> headers) throws IOException {
		boolean https = false;
		if ("https".equals(urlString.split(":")[0])) {
			https = true;
		} else if ("http".equals(urlString.split(":")[0])) {
			https = false;
		}

		if (method.equalsIgnoreCase("GET") && params != null) {
			urlString += ("?" + params);
		}

		if (https) {
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				@Override
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
						throws CertificateException {
				}

				@Override
				public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
						throws CertificateException {
				}
			} };

			try {
				SSLContext sc = SSLContext.getInstance("TLS");
				sc.init(null, trustAllCerts, new SecureRandom());
				HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (KeyManagementException e) {
				e.printStackTrace();
			}
		}

		URL url = new URL(urlString);
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.setRequestMethod(method);
		urlConnection.setDoOutput(true);
		urlConnection.setDoInput(true);
		urlConnection.setUseCaches(false);
		// 设置连接主机超时（单位：毫秒）
		// urlConnection.setConnectTimeout(3000);
		// // 设置从主机读取数据超时（单位：毫秒）
		// urlConnection.setReadTimeout(3000);

		if (null != headers) {
			for (Entry<String, String> entry : headers.entrySet()) {
				urlConnection.setRequestProperty(entry.getKey(), entry.getValue());
			}
		}

		if (method.equalsIgnoreCase("POST")) {
			// urlConnection.setRequestProperty("Transfer-Encoding", "chunked");
			urlConnection.setRequestProperty("Connection", "keep-alive");
			urlConnection.setRequestProperty("Keep-Alive", "timeout=10, max=50");
			// urlConnection.setRequestProperty("Accept-Encoding", "gzip");

			urlConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
			urlConnection.getOutputStream().write(data);

			urlConnection.getOutputStream().flush();
			urlConnection.getOutputStream().close();

		} else if (method.equalsIgnoreCase("GET") && params != null) {
			urlConnection.setRequestProperty("Content-Length", String.valueOf(params.getBytes("UTF-8").length));
			urlConnection.getOutputStream().write(params.getBytes("UTF-8"));
		}

		return this.makeContent(urlString, urlConnection);
	}

	/**
	 * 返回请求处理响应结果
	 * 
	 * @param urlString
	 * @param urlConnection
	 * @param isneedUnzip
	 *            是否需要解压缩和解密，若是PP消息则不需要
	 * @return
	 * @throws IOException
	 */
	private HttpRespons makeContent(String urlString, HttpURLConnection urlConnection) throws IOException {
		HttpRespons httpResponser = new HttpRespons();
		try {
			InputStream in = urlConnection.getInputStream();

			String ecod = urlConnection.getContentEncoding();
			if (ecod == null) {
				ecod = this.defaultContentEncoding;
			}
			httpResponser.urlString = urlString;
			httpResponser.defaultPort = urlConnection.getURL().getDefaultPort();
			httpResponser.file = urlConnection.getURL().getFile();
			httpResponser.host = urlConnection.getURL().getHost();
			httpResponser.path = urlConnection.getURL().getPath();
			httpResponser.port = urlConnection.getURL().getPort();
			httpResponser.protocol = urlConnection.getURL().getProtocol();
			httpResponser.query = urlConnection.getURL().getQuery();
			httpResponser.ref = urlConnection.getURL().getRef();
			httpResponser.userInfo = urlConnection.getURL().getUserInfo();

			// 非压缩正常读取
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
			httpResponser.contentCollection = new Vector<String>();
			StringBuffer temp = new StringBuffer();
			String line = bufferedReader.readLine();
			while (line != null) {
				httpResponser.contentCollection.add(line);
				temp.append(line).append("\r\n");
				line = bufferedReader.readLine();
			}
			bufferedReader.close();

			// 解压缩，解密
			String lineContent = temp.toString();

			// System.out.println("发送内容："+lineContent);
			httpResponser.content = lineContent;
			httpResponser.contentEncoding = ecod;
			httpResponser.code = urlConnection.getResponseCode();
			httpResponser.message = urlConnection.getResponseMessage();
			httpResponser.contentType = urlConnection.getContentType();
			httpResponser.method = urlConnection.getRequestMethod();
			httpResponser.connectTimeout = urlConnection.getConnectTimeout();
			httpResponser.readTimeout = urlConnection.getReadTimeout();
			httpResponser.contentEncoding = urlConnection.getContentEncoding();
			return httpResponser;
		} catch (Exception e) {
			throw e;
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
	}

}
