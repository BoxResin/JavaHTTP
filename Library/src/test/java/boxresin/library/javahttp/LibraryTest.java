package boxresin.library.javahttp;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URLEncoder;
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

		// Test a request with some POST parameter.
		response = new HttpRequester()
				.setUrl("http://localhost/test/test.php")
				.setMethod("POST")
				.addParameter("hello", "anything")
				.request();

		Assert.assertEquals("Hello World!\nWorld!\n", response.getBody());

		// Test a complex requset.
		response = new HttpRequester()
				.setUrl("http://localhost/test/test.php")
				.setMethod("POST")
				.addParameter("hello", "haha")
				.addHeader("apiKey", "boxresin")
				.request();

		Assert.assertEquals("Hello World!\napiKey is detected.\nWorld!\n", response.getBody());
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

	@Test
	public void testSetURL()
	{
		Map<String, String> params = new HashMap<>();
		params.put("hello", "world");
		params.put("test", "it");

		HttpRequester requester = new HttpRequester();
		requester.setUrl("http://www.google.com", params);
		Assert.assertEquals("http://www.google.com?test=it&hello=world", requester.getUrl());

		params = new HashMap<>();
		params.put("hangul", "한글 테스트");
		params.put("second", "두 번째");

		requester.setUrl("http://www.google.com", params);
		Assert.assertEquals("http://www.google.com?hangul=%ED%95%9C%EA%B8%80+%ED%85%8C%EC%8A%A4%ED%8A%B8&" +
				"second=%EB%91%90+%EB%B2%88%EC%A7%B8", requester.getUrl());
	}

	@Test
	public void testURLCodec()
	{
		System.out.println(URLEncoder.encode("한글"));
		for (Map.Entry<String, Charset> charset : Charset.availableCharsets().entrySet())
		{
			try
			{
				if ("%ED%95%9C%EA%B8%80".equals(URLEncoder.encode("한글", charset.getKey())))
					return;
			}
			catch (Exception e)
			{
			}
		}
		Assert.assertTrue(false);
	}
}
