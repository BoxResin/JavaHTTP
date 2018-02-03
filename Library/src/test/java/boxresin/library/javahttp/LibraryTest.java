package boxresin.library.javahttp;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Minsuk Eom on 2017-04-27.
 */
public class LibraryTest
{
	@Test
	public void testHttpRequester() throws IOException
	{
		// Test a normal request.
		HttpResponse response = new HttpRequester()
				.setUrl("http://localhost/test/test.php")
				.setMethod("get")
				.request();

		Assert.assertEquals(response.getBody(), "Hello World!\n");

		// Test a request with some header.
		response = new HttpRequester()
				.setUrl("http://localhost/test/test.php")
				.setMethod("GET")
				.addHeader("apiKey", "boxresin")
				.request();

		Assert.assertEquals("Hello World!\napiKey is detected.\n", response.getBody());

		// Test a request with some POST parameters.
		response = new HttpRequester()
				.setUrl("http://localhost/test/test.php")
				.setMethod("POST")
				.addParameter("hello", "anything")
				.addParameter("key", "one")
				.addParameter("key2", "two")
				.request();

		Assert.assertEquals("Hello World!\nWorld!\nha!\n\uD55C\uAE00\n", response.getBody());

		// Test a request with some POST parameters.
		Map<String, String> params  = new HashMap<>();
		params.put("hello", "anything");
		params.put("key", "one");
		params.put("key2", "two");

		response = new HttpRequester()
				.setUrl("http://localhost/test/test.php")
				.setMethod("POST")
				.addParameters(params)
				.request();

		Assert.assertEquals("Hello World!\nWorld!\nha!\n\uD55C\uAE00\n", response.getBody());

		// Test a complex requset.
		response = new HttpRequester()
				.setUrl("http://localhost/test/test.php")
				.setMethod("POST")
				.addParameter("hello", "haha")
				.addHeader("apiKey", "boxresin")
				.request();

		Assert.assertEquals("Hello World!\napiKey is detected.\nWorld!\n", response.getBody());
	}

	/**
	 * URL encoding test
	 */
	@Test
	public void testSetUrl() throws UnsupportedEncodingException
	{
		// hangul query
		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put("q", "\uD55C\uAE00 test");
		paramsMap.put("from", "https://www.naver.com");

		HttpRequester requester = new HttpRequester()
				.setUrl("https://www.google.com", paramsMap);
		Assert.assertEquals("https://www.google.com?q=%ED%95%9C%EA%B8%80+test&from=https%3A%2F%2Fwww.naver.com", requester.getUrl());

		// crazy query
		paramsMap.clear();
		paramsMap.put("foo", "&^*$= my crazy query% |!!");

		requester.setUrl("https://www.daum.net", paramsMap);
		Assert.assertEquals("https://www.daum.net?foo=%26%5E*%24%3D+my+crazy+query%25+%7C%21%21", requester.getUrl());
	}

	@Test
	public void testHttpResponse() throws IOException
	{
		// Test the encoding detection process.
		HttpResponse response = new HttpRequester()
				.setUrl("http://localhost/test/euc.php")
				.setMethod("get")
				.request();

		Assert.assertEquals("EUC-KR", response.getBodyEncoding());
		System.out.println(new String(response.getBodyAsByteArray(), Charset.forName(response.getBodyEncoding())));

		response = new HttpRequester()
				.setUrl("http://localhost/test/gbk.php")
				.setMethod("get")
				.request();

		Assert.assertEquals("GBK", response.getBodyEncoding());
		System.out.println(new String(response.getBodyAsByteArray(), Charset.forName(response.getBodyEncoding())));

		Assert.assertEquals(new String(response.getBodyAsByteArray(), Charset.forName(response.getBodyEncoding())), response.getBody());
	}
}
