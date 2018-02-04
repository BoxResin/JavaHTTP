package boxresin.library.javahttp;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

/**
 * A class representing HTTP response.
 * You can get information in response(ex. HTTP status code, body of a message etc) via this class.
 *
 * @since v1.0.0
 */
public class HttpResponse
{
				private int statusCode;
	@NotNull    private String statusMessage;
	@NotNull    private ByteArrayOutputStream bodyStream;
	@NotNull    private HttpURLConnection connection;

	/**
	 * Preventing from instantiation of HttpResponse with constructor.
	 * @since v1.0.0
	 */
	HttpResponse(int statusCode, @NotNull String statusMessage,
	             @NotNull ByteArrayOutputStream bodyStream, @NotNull HttpURLConnection connection)
	{
		this.statusCode = statusCode;
		this.statusMessage = statusMessage;
		this.bodyStream = bodyStream;
		this.connection = connection;
	}

	/**
	 * @return HTTP status code as int type (ex. 404)
	 * @since v1.0.0
	 */
	public int getStatusCode()
	{
		return statusCode;
	}

	/**
	 * @return A description for HTTP status code as String type (ex. "Not Found")
	 * @since v1.0.0
	 */
	@NotNull
	public String getStatusMessage()
	{
		return statusMessage;
	}

	/**
	 * Returns a header for response message by specified key. <b>The key is case-sensitive.</b>
	 *
	 * @return It will be null if there's no such header
	 * @since v1.0.0
	 */
	@Nullable
	public String getHeader(String key)
	{
		return connection.getHeaderField(key);
	}

	/**
	 * @return All headers for response message. <b>NOTE: It's unmodifiable</b>
	 * @since v1.1.1
	 */
	@NotNull
	public Map<String, List<String>> getHeaders()
	{
		return connection.getHeaderFields();
	}

	/**
	 * Returns content for response message. It can be an HTML document or JSON-formatted text. <br>
	 * <b>NOTE: It detects content's encoding automatically.</b> If there is no encoding
	 * information, default is UTF-8.
	 *
	 * @return Body for response message as String type
	 * @since v1.0.0
	 */
	@NotNull
	public String getBody()
	{
		try
		{
			@Nullable String encoding = getBodyEncoding();
			if (encoding == null)
				encoding = "UTF-8";
			return bodyStream.toString(encoding);
		}
		catch (Exception e)
		{
			return bodyStream.toString();
		}
	}

	/**
	 * Returns content for response message. It can be an HTML document or JSON-formatted text.
	 *
	 * @param encoding Name of charset (ex. "UTF-8")
	 * @return Body for response message as String type with specified encoding
	 * @since v1.0.0
	 */
	@NotNull
	public String getBody(@NotNull String encoding) throws UnsupportedEncodingException
	{
		return bodyStream.toString(encoding);
	}

	/**
	 * Returns content for response message. It can be an HTML document or JSON-formatted text.
	 *
	 * @return Body for response message as byte array
	 * @since v1.0.0
	 */
	@NotNull
	public byte[] getBodyAsByteArray()
	{
		return bodyStream.toByteArray();
	}

	/**
	 * Returns encoding of content in the response message. It will be null if there's no encoding
	 *         information in the response header. <br><b>NOTE: It always returns as upper case.</b>
	 *
	 * @return content's encoding
	 */
	@Nullable
	public String getBodyEncoding()
	{
		try
		{
			String contentType = getHeader("Content-Type");
			String[] params = contentType.split(";");
			for (String param : params)
			{
				param = param.trim();
				if (param.startsWith("charset="))
					return param.replace("charset=", "").toUpperCase();
			}
		}
		catch (Exception ignored)
		{
		}
		return null;
	}
}
