package boxresin.library.javahttp;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;

/**
 * A class representing HTTP request.
 * @since v1.0.0
 */
public class HttpRequester
{
	// @formatter:off
	@NotNull    private String  url    = "";    // URL to requst
	@NotNull    private String  method = "";    // HTTP method
				private int     connectTimeout; // Timeout when connecting to a web server, in milliseconds
				private int     readTimeout;    // Timeout when reading an HTTP response from a web server, in milliseconds

	private Map<String, String> params  = new TreeMap<>(); // Map that contains POST parameters
	private Map<String, String> headers = new TreeMap<>(); // Map that contains request headers
	// @formatter:on

	/**
	 * @since v1.0.0
	 */
	public HttpRequester()
	{
	}

	/**
	 * Returns the URL to request.
	 *
	 * @return The URL to request
	 * @since v1.0.0
	 */
	@NotNull
	public String getUrl()
	{
		return url;
	}

	/**
	 * Sets the URL to request.
	 *
	 * @param url The URL to request
	 * @since v1.0.0
	 */
	@NotNull
	public HttpRequester setUrl(@NotNull String url)
	{
		this.url = url;
		return this;
	}

	/**
	 * Sets the URL to request.
	 *
	 * @param url    The URL without query
	 * @param params A map that contains URL query parameters into key and value. The key and value
	 *               will be percent encoded as UTF-8.
	 * @throws UnsupportedEncodingException Occurs when UTF-8 is not available.
	 * @since v1.1.0
	 */
	@NotNull
	public HttpRequester setUrl(@NotNull String url, @NotNull Map<String, String> params)
			throws UnsupportedEncodingException
	{
		StringBuilder queryString = new StringBuilder();

		// Convert the params map into a query string.
		for (Map.Entry<String, String> entry : params.entrySet())
		{
			if (queryString.length() != 0)
				queryString.append('&');

			String encodedKey = URLEncoder.encode(entry.getKey(), "UTF-8");
			String encodedValue = URLEncoder.encode(entry.getValue(), "UTF-8");

			queryString.append(encodedKey);
			queryString.append('=');
			queryString.append(encodedValue);
		}

		return setUrl(url + '?' + queryString.toString());
	}

	/**
	 * Returns HTTP method to request.
	 *
	 * @return HTTP method as String type (ex. "POST", "GET" etc)
	 * @since v1.0.0
	 */
	@NotNull
	public String getMethod()
	{
		return method;
	}

	/**
	 * Sets HTTP method.
	 *
	 * @param method HTTP method as String type (ex. "POST", "GET" etc)<br>
	 *               <b>It's not case sensitive, so you can use both "POST" and "post".</b>
	 * @since v1.0.0
	 */
	@NotNull
	public HttpRequester setMethod(@NotNull String method)
	{
		this.method = method.toUpperCase();
		return this;
	}

	/**
	 * Returns connect-timeout.
	 *
	 * @return Connect-timeout, in milliseconds
	 * @since v1.0.0
	 */
	public int getConnectTimeout()
	{
		return connectTimeout;
	}

	/**
	 * Sets timeout when connecting to a web server.
	 *
	 * @param timeout Connect-timeout, in milliseconds
	 * @since v1.0.0
	 */
	@NotNull
	public HttpRequester setConnectTimeout(int timeout)
	{
		this.connectTimeout = timeout;
		return this;
	}

	/**
	 * Returns read-timeout.
	 *
	 * @return Read-timeout, in milliseconds
	 * @since v1.0.0
	 */
	public int getReadTimeout()
	{
		return readTimeout;
	}

	/**
	 * Sets timeout when reading an HTTP response from a web server.
	 *
	 * @param timeout Read-timeout, in milliseconds
	 * @since v1.0.0
	 */
	@NotNull
	public HttpRequester setReadTimeout(int timeout)
	{
		this.readTimeout = timeout;
		return this;
	}

	/**
	 * Adds a parameter for POST method.
	 * If specified HTTP method is not "POST", this parameter would be ignored.
	 *
	 * @since v1.0.0
	 */
	@NotNull
	public HttpRequester addParameter(@NotNull String key, @NotNull String value)
	{
		params.put(key, value);
		return this;
	}

	/**
	 * Adds parameters for POST method.
	 * If specified HTTP method is not "POST", these parameters would be ignored.
	 *
	 * @param params A map that contains parameters into key and value.
	 * @since v1.1.1
	 */
	@NotNull
	public HttpRequester addParameters(@NotNull Map<String, String> params)
	{
		this.params.putAll(params);
		return this;
	}

	/**
	 * Clears all of parameters for POST method.
	 * @since v1.0.0
	 */
	public void clearParameters()
	{
		params.clear();
	}

	/**
	 * Adds a header for request.
	 * @since v1.0.0
	 */
	@NotNull
	public HttpRequester addHeader(@NotNull String key, @NotNull String value)
	{
		headers.put(key, value);
		return this;
	}

	/**
	 * Adds headers for request.
	 * @param headers A map that contains headers into key and value.
	 * @since v1.1.1
	 */
	@NotNull
	public HttpRequester addHeaders(@NotNull Map<String, String> headers)
	{
		this.headers.putAll(headers);
		return this;
	}

	/**
	 * Clears all of headers for request.
	 * @since v1.0.0
	 */
	public void clearHeaders()
	{
		headers.clear();
	}

	/**
	 * Send HTTP request to a web server.
	 *
	 * @throws SocketTimeoutException Occurs when timeout.
	 * @return An HTML response from the web server.
	 * @since v1.0.0
	 */
	@NotNull
	public HttpResponse request() throws SocketTimeoutException, IOException
	{
		// Set options.
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setRequestMethod(method);
		connection.setConnectTimeout(connectTimeout);
		connection.setReadTimeout(readTimeout);

		// Add headers.
		for (Map.Entry<String, String> entry : headers.entrySet())
			connection.setRequestProperty(entry.getKey(), entry.getValue());

		// Only for POST method
		if (method.equals("POST"))
		{
			connection.setDoOutput(true);

			// Add POST parameters.
			boolean isNotFirst = false;
			OutputStream out = connection.getOutputStream();
			for (Map.Entry<String, String> entry : params.entrySet())
			{
				String key = entry.getKey();
				String value = entry.getValue();

				if (isNotFirst)
					out.write("&".getBytes());
				else isNotFirst = true;

				out.write(String.format("%s=%s", key, value).getBytes());
			}
		}

		connection.connect();

		// Read HTTP status.
		int statusCode = connection.getResponseCode();
		String statusMessage = connection.getResponseMessage();

		// Prepare buffer.
		ByteArrayOutputStream bufferStream = new ByteArrayOutputStream(10 * 1024);
		byte[] buffer = new byte[1024];

		// Read response's body little by little in order to be ready for cancelation.
		InputStream in = new BufferedInputStream(connection.getInputStream());
		while (true)
		{
			int length = in.read(buffer);
			if (length == -1)
				break;
			bufferStream.write(buffer, 0, length);
		}

		connection.disconnect();
		return new HttpResponse(statusCode, statusMessage, bufferStream, connection);
	}

	/**
	 * Send HTTP request to a web server.
	 *
	 * @param autoRedirection If it set to true, detect and re-request new URL automatically.
	 * @throws SocketTimeoutException Occurs when timeout.
	 * @return An HTML response from the web server.
	 * @since v1.1.1
	 */
	@NotNull
	public HttpResponse request(boolean autoRedirection) throws SocketTimeoutException, IOException
	{
		HttpResponse response = request();
		@Nullable String newUrl = response.getHeader("Location");

		if (autoRedirection && newUrl != null)
		{
			setUrl(newUrl);
			return request(true);
		}
		else return response;
	}
}
