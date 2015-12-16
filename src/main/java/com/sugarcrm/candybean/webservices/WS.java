/**
 * Candybean is a next generation automation and testing framework suite.
 * It is a collection of components that foster test automation, execution
 * configuration, data abstraction, results illustration, tag-based execution,
 * top-down and bottom-up batches, mobile variants, test translation across
 * languages, plain-language testing, and web service testing.
 * Copyright (C) 2013 SugarCRM, Inc. <candybean@sugarcrm.com>
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sugarcrm.candybean.webservices;

import com.sugarcrm.candybean.exceptions.CandybeanException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Helps make web-service calls and supports DELETE, GET, POST, and PUT
 */
public class WS {

	public enum OP {DELETE, GET, POST, PUT}

	// The list of HTTP return codes indicating success
	private static final Set<Integer> ACCEPTABLE_RETURN_CODE_SET = new HashSet<>(Arrays.asList(
			new Integer[]{200, 201, 202}));

	/**
	 * Send a DELETE, GET, POST, or PUT http request
	 *
	 * @param op      The type of http request
	 * @param uri     The http endpoint
	 * @param headers Map of header key value pairs
	 * @param body    String representation of the request body
	 * @return Key Value pairs of the response
	 * @throws Exception When http request failed
	 * @deprecated Use {@link #request(OP, String, Map, String, ContentType)}
	 */
	@Deprecated
	public static Map<String, Object> request(OP op, String uri, Map<String, String> headers, String body) throws Exception {
		return request(op, uri, headers, body, ContentType.DEFAULT_TEXT);
	}

	/**
	 * Send a DELETE, GET, POST, or PUT http request
	 *
	 * @param op          The type of http request
	 * @param uri         The http endpoint
	 * @param payload     Map of key value pairs for the body
	 * @param postHeaders List of Maps of header key value pairs
	 * @param body        String representation of the request body (ignored)
	 * @return Key Value pairs of the response
	 * @throws Exception When http request failed
	 * @deprecated This is a work around for old compatibility, use {@link #request(OP, String, Map, String, ContentType)}
	 * or {@link #request(OP, String, Map, Map)}
	 */
	@Deprecated
	public static Map<String, Object> request(OP op, String uri, Map<String, String> payload, String body, ArrayList<HashMap<String, String>> postHeaders) throws Exception {
		HashMap<String, String> headers = new HashMap<>();
		for (HashMap<String, String> map : postHeaders) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				headers.put(entry.getKey(), entry.getValue());
			}
		}
		HashMap<String, Object> newPayload = new HashMap<>();
		for (Map.Entry<String, String> entry : payload.entrySet()) {
			newPayload.put(entry.getKey(), entry.getValue());
		}

		if (body == null) {
			return request(op, uri, headers, newPayload);
		} else {
			return request(op, uri, headers, body, ContentType.DEFAULT_TEXT);
		}
	}

	/**
	 * Send a DELETE, GET, POST, or PUT http request using a JSON body
	 *
	 * @param op      The type of http request
	 * @param uri     The http endpoint
	 * @param headers Map of header key value pairs
	 * @param body    Map of Key Value pairs
	 * @return Key Value pairs of the response
	 * @throws Exception If HTTP request failed
	 */
	public static Map<String, Object> request(OP op, String uri, Map<String, String> headers, Map<String, Object> body) throws Exception {
		return request(op, uri, headers, body, ContentType.APPLICATION_JSON);
	}

	/**
	 * Send a DELETE, GET, POST, or PUT http request, intended for multipart data and JSON requests
	 *
	 * @param op          The type of http request
	 * @param uri         The http endpoint
	 * @param headers     Map of header key value pairs
	 * @param body        A recursive map representing a JSON, where Object is one of String|Map&lt;String,Object&gt;
	 * @param contentType The intended content type of the body
	 * @return Key Value pairs of the response
	 * @throws Exception If HTTP request failed
	 */
	public static Map<String, Object> request(OP op, String uri, Map<String, String> headers, Map<String, Object> body, ContentType contentType) throws Exception {
		if (!verifyBody(body)) {
			throw new CandybeanException("Body is not representable as JSON");
		}
		switch (op) {
			case DELETE:
				return handleRequest(new HttpDelete(uri), headers);
			case GET:
				return handleRequest(new HttpGet(uri), headers);
			case POST:
				HttpPost post = new HttpPost(uri);
				addBodyToRequest(post, body, contentType);
				return handleRequest(post, headers);
			case PUT:
				HttpPut put = new HttpPut(uri);
				addBodyToRequest(put, body, contentType);
				return handleRequest(put, headers);
			default:
				/*
				 * JLS 13.4.26: Adding or reordering constants in an enum type will not break compatibility with
				 * pre-existing binaries.
				 * Thus we include a default in the case that a future version of the enum has a case which is not
				 * one of the above
				 */
				throw new Exception("Unrecognized OP type... Perhaps your binaries are the wrong version?");
		}
	}

	/**
	 * Send a DELETE, GET, POST, or PUT http request
	 *
	 * @param op          The type of http request
	 * @param uri         The http endpoint
	 * @param headers     Map of header key value pairs
	 * @param body        String representation of the request body
	 * @param contentType The content type of the body
	 * @return Key Value pairs of the response
	 * @throws CandybeanException When http request failed
	 */
	public static Map<String, Object> request(OP op, String uri, Map<String, String> headers, String body, ContentType contentType) throws CandybeanException {
		switch (op) {
			case DELETE:
				return handleRequest(new HttpDelete(uri), headers);
			case GET:
				return handleRequest(new HttpGet(uri), headers);
			case POST:
				HttpPost post = new HttpPost(uri);
				if (body != null) {
					post.setEntity(new StringEntity(body, contentType));
				}
				return handleRequest(post, headers);
			case PUT:
				HttpPut put = new HttpPut(uri);
				if (body != null) {
					put.setEntity(new StringEntity(body, contentType));
				}
				return handleRequest(put, headers);
			default:
				/*
				 * JLS 13.4.26: Adding or reordering constants in an enum type will not break compatibility with
				 * pre-existing binaries.
				 * Thus we include a default in the case that a future version of the enum has a case which is not
				 * one of the above
				 */
				throw new CandybeanException("Unrecognized OP type... Perhaps your binaries are the wrong version?");
		}
	}

	/**
	 * Protected method to handle sending and receiving an http request
	 *
	 * @param request The Http request that is being send
	 * @param headers Map of Key Value header pairs
	 * @return Key Value pairs of the response
	 * @throws CandybeanException If the response is null or not an acceptable HTTP code
	 */
	protected static Map<String, Object> handleRequest(HttpUriRequest request, Map<String, String> headers) throws CandybeanException {
		// Add the request headers and execute the request
		for (Map.Entry<String, String> header : headers.entrySet()) {
			request.addHeader(new BasicHeader(header.getKey(), header.getValue()));
		}
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		try {
			HttpResponse response = httpClient.execute(request);

			// Check for invalid responses or error return codes
			if (response == null) {
				throw new CandybeanException("Http Request failed: Response null");
			}

			// Cast the response into a Map and return
			JSONObject parse = (JSONObject) JSONValue.parse(new InputStreamReader(response.getEntity().getContent()));
			@SuppressWarnings("unchecked")
			Map<String, Object> mapParse = (Map<String, Object>) parse;

			int code = response.getStatusLine().getStatusCode();
			if (!ACCEPTABLE_RETURN_CODE_SET.contains(code)) {
				throw new CandybeanException("HTTP request received HTTP code: " + code + "\n"
						+ "Response: " + response.toString());
			} else if (mapParse == null) {
				throw new CandybeanException("Could not format response\n"
						+ "Response: " + response.toString());
			}

			return mapParse;
		} catch (IOException | IllegalStateException e) {
			// Cast the other possible exceptions as a CandybeanException
			throw new CandybeanException(e);
		}
	}

	/**
	 * Private helper method to abstract creating a POST/PUT request.
	 * Side Effect: Adds the body to the request
	 *
	 * @param request     A PUT or POST request
	 * @param body        Map of Key Value pairs
	 * @param contentType The intended content type of the body
	 */
	protected static void addBodyToRequest(HttpEntityEnclosingRequestBase request, Map<String, Object> body, ContentType contentType) {
		if (body != null) {
			if (contentType == ContentType.MULTIPART_FORM_DATA) {
				MultipartEntityBuilder builder = MultipartEntityBuilder.create();
				for (Map.Entry<String, Object> entry : body.entrySet()) {
					builder.addTextBody(entry.getKey(), (String) entry.getValue());
				}
				request.setEntity(builder.build());
			} else {
				JSONObject jsonBody = new JSONObject(body);
				StringEntity strBody = new StringEntity(jsonBody.toJSONString(), ContentType.APPLICATION_JSON);
				request.setEntity(strBody);
			}
		}
	}

	/**
	 * Verifies that a body is correctly formed
	 *
	 * @param body The body to verify
	 * @return True if the body is correctly formed, else False;
	 */
	@SuppressWarnings("unchecked")
	protected static boolean verifyBody(Map<String, Object> body) {
		for (Map.Entry<String, Object> entry : body.entrySet()) {
			if (entry.getValue() instanceof Map) {
				if (!verifyBody((Map<String, Object>) entry.getValue())) {
					return false;
				}
			} else if (!(entry.getValue() instanceof String)) {
				return false;
			}
		}
		return true;
	}
}
