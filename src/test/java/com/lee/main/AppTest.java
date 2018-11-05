package com.lee.main;

import java.io.IOException;

import com.lee.arphoto.httptools.utils.HttpRequester;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		assertTrue(true);
	}

	public static void main(String[] args) {
		HttpRequester requester = new HttpRequester();
		try {
			// requester.sendNormalMsgGet("http://localhost:15001/LoginAction",
			// null);

			byte[] bs = new byte[31];
			String name = "name=key";
			String data = "data=" + bs;

			requester.sendNormalMsgPost("http://localhost:15001/LoginAction", name + "&" + data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
