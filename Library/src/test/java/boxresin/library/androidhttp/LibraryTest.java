package boxresin.library.androidhttp;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;

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
}