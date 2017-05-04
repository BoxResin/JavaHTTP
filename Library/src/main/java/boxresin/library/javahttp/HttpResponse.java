package boxresin.library.javahttp;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

/**
 * A class representing HTTP response.
 * You can get information in response(ex. HTTP status code, body of a message etc) via this class.
 * @since v1.0.0
 */
public class HttpResponse
{
	private int statusCode;
	private String statusMessage;
	private ByteArrayOutputStream bodyStream;
	private HttpURLConnection connection;

	/**
	 * Preventing from instantiation of HttpResponse with constructor
	 */
	HttpResponse(int statusCode, String statusMessage, ByteArrayOutputStream bodyStream, HttpURLConnection connection)
	{
		this.statusCode = statusCode;
		this.statusMessage = statusMessage;
		this.bodyStream = bodyStream;
		this.connection = connection;
	}

	/**
	 * Returns HTTP status code of a response message.
	 * @return HTTP status code as int type (ex. 404)
	 * @since v1.0.0
	 */
	public int getStatusCode()
	{
		return statusCode;
	}

	/**
	 * Returns a description of HTTP status code.
	 * @return Description of HTTP status code as String type (ex. "Not Found")
	 * @since v1.0.0
	 */
	public String getStatusMessage()
	{
		return statusMessage;
	}

	/**
	 * Returns a header of a response message by specified key. <b>The key is case-sensitive.</b>
	 * @return A header of a response message. It will be null if there's no such header.
	 * @since v1.0.0
	 */
	public String getHeader(String key)
	{
		return connection.getHeaderField(key);
	}

	/**
	 * Returns content of a response message. It can be an HTML document or JSON-formatted data. <br>
	 * <b>NOTE: It detects content encoding automatically.</b>
	 * @return Body of a response message as String type
	 * @since v1.0.0
	 */
	public String getBody()
	{
		try
		{
			String encoding = getBodyEncoding();
			return bodyStream.toString(encoding);
		}
		catch (Exception e)
		{
			return bodyStream.toString();
		}
	}

	/**
	 * Returns content of a response message. It can be an HTML document or JSON-formatted data.
	 * @param encoding Name of charset (ex. "UTF-8")
	 * @return Body of a response message as String type with specified encoding
	 * @since v1.0.0
	 */
	public String getBody(String encoding) throws UnsupportedEncodingException
	{
		return bodyStream.toString(encoding);
	}

	/**
	 * Returns content of a response message. It can be an HTML document or JSON-formatted data.
	 * @return Body of a response message as byte array.
	 * @since v1.0.0
	 */
	public byte[] getBodyAsByteArray()
	{
		return bodyStream.toByteArray();
	}

	/**
	 * Returns encoding of content in a response message. It will be null if there's no encoding
	 *         information in the response header. <br><b>NOTE: It always returns upper case.</b>
	 * @return content's encoding
	 */
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
