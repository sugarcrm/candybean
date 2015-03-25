/**
 * Candybean is a next generation automation and testing framework suite.
 * It is a collection of components that foster test automation, execution
 * configuration, data abstraction, results illustration, tag-based execution,
 * top-down and bottom-up batches, mobile variants, test translation across
 * languages, plain-language testing, and web service testing.
 * Copyright (C) 2013 SugarCRM, Inc. <candybean@sugarcrm.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sugarcrm.candybean.webservices;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


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

/**
 * Helps make web-service calls and supports several operations like POST and GET.
 * 
 */
public class WS {

	protected static Logger log = Logger.getLogger(WS.class.getSimpleName());
	public enum OP { DELETE, GET, POST, PUT }

	/**
	 * Send an DELETE, GET, POST, or PUT http request
	 * @param op The type of http request
	 * @param uri The http endpoint
	 * @param headers Map of header key value pairs
	 * @param body String representation of the request body
	 * @return Key Value pairs of the response
	 * @throws Exception When http request failed
	 * @deprecated Use {@link #request(OP,String,Map,String,ContentType)}
	 */
	@Deprecated
	public static Map<String, Object> request(OP op, String uri, Map<String, String> headers, String body) throws Exception {
		return request(op, uri, headers, body, ContentType.DEFAULT_TEXT);
	}

	/**
	 * Send an DELETE, GET, POST, or PUT http request
	 * @param op The type of http request
	 * @param uri The http endpoint
	 * @param payload Map of key value pairs for the body
	 * @param postHeaders List of Maps of header key value pairs
	 * @param body String representation of the request body (ignored)
	 * @return Key Value pairs of the response
	 * @throws Exception When http request failed
	 * @deprecated This is a work around for old compatibility, use {@link #request(OP,String,Map,String,ContentType)} or {@link #request(OP,String,Map,Map)}
	 */
	@Deprecated
	public static Map<String, Object> request(OP op, String uri, Map<String, String> payload, String body, ArrayList<HashMap<String, String>> postHeaders) throws Exception {
		HashMap<String, String> headers = new HashMap<>();
		for (HashMap<String, String> map : postHeaders) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				headers.put(entry.getKey(), entry.getValue());
			}
		}
		if (body == null) {
			return request(op, uri, headers, payload);
		} else {
			return request(op,uri, headers, body, ContentType.DEFAULT_TEXT);
		}
	}

    /**
	 * Send an DELETE, GET, POST, or PUT http request using a JSON body
	 * @param op The type of http request
	 * @param uri The http endpoint
	 * @param headers Map of header key value pairs
	 * @param body Map of Key Value pairs
	 * @return Key Value pairs of the response
	 * @throws Exception When http request failed
	 */
	public static Map<String, Object> request(OP op, String uri, Map<String, String> headers, Map<String,String> body) throws Exception {
		return request(op,uri,headers,body,ContentType.APPLICATION_JSON);
	}

	/**
	 * Send an DELETE, GET, POST, or PUT http request, intended for multipart data and JSON requests
	 * @param op The type of http request
	 * @param uri The http endpoint
	 * @param headers Map of header key value pairs
	 * @param body Map of Key Value pairs
	 * @param contentType The intended content type of the body
	 * @return Key Value pairs of the response
	 * @throws Exception When http request failed
	 */
	public static Map<String, Object> request(OP op, String uri, Map<String, String> headers, Map<String,String> body, ContentType contentType) throws Exception {
		Map<String, Object> mapParse;
		switch (op) {
			case DELETE:
				mapParse = handleRequest(new HttpDelete(uri), headers);
				break;
			case GET:
				mapParse = handleRequest(new HttpGet(uri), headers);
				break;
			case POST:
				HttpPost post = new HttpPost(uri);
				if (body != null) {
					if (contentType == ContentType.MULTIPART_FORM_DATA) {
						MultipartEntityBuilder builder = MultipartEntityBuilder.create();
						for (Map.Entry<String, String> entry : body.entrySet())
							builder.addTextBody(entry.getKey(), entry.getValue());
						post.setEntity(builder.build());
					} else {
						JSONObject jsonBody = new JSONObject(body);
						StringEntity strBody = new StringEntity(jsonBody.toJSONString(), ContentType.APPLICATION_JSON);
						post.setEntity(strBody);
					}
				}
				mapParse = handleRequest(post, headers);
				break;
			case PUT:
				HttpPut put = new HttpPut(uri);
				if (body != null) {
					if (contentType == ContentType.MULTIPART_FORM_DATA) {
                        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                        for (Map.Entry<String,String> entry: body.entrySet())
                            builder.addTextBody(entry.getKey(), entry.getValue());
                        put.setEntity(builder.build());
					} else {
						JSONObject jsonBody = new JSONObject(body);
						StringEntity strBody = new StringEntity(jsonBody.toJSONString(), ContentType.APPLICATION_JSON);
						put.setEntity(strBody);
					}
				}
				mapParse = handleRequest(put, headers);
				break;
			default:
				throw new Exception("WS:OP type not recognized.");
		}
		return mapParse;
	}

	/**
	 * Send an DELETE, GET, POST, or PUT http request
	 * @param op The type of http request
	 * @param uri The http endpoint
	 * @param headers Map of header key value pairs
	 * @param body String representation of the request body
	 * @param contentType The content type of the body
	 * @return Key Value pairs of the response
	 * @throws Exception When http request failed
	 */
	public static Map<String, Object> request(OP op, String uri, Map<String, String> headers, String body, ContentType contentType) throws Exception {
	    Map<String, Object> mapParse;
		switch (op) {
			case DELETE:
	            mapParse = handleRequest(new HttpDelete(uri), headers);
				break;
			case GET:
	            mapParse = handleRequest(new HttpGet(uri), headers);
				break;
			case POST:
				HttpPost post = new HttpPost(uri);
				if (body != null) {
					post.setEntity(new StringEntity(body, contentType));
				}
	            mapParse = handleRequest(post, headers);
				break;
			case PUT:
				HttpPut put = new HttpPut(uri);
				if (body != null) {
					put.setEntity(new StringEntity(body, contentType));
				}
	            mapParse = handleRequest(put, headers);
				break;
			default:
				throw new Exception("WS:OP type not recognized.");
		}
		return mapParse;
	}

	/**
	 * Protected method to handle sending and receiving an http request
	 * @param request The Http request that is being send
	 * @param headers Map of Key Value header pairs
	 * @return Key Value pairs of the response
	 */
	protected static Map<String, Object> handleRequest(HttpUriRequest request, Map<String, String> headers) {

		for (Map.Entry<String, String> header : headers.entrySet()) {
			request.addHeader(new BasicHeader(header.getKey(), header.getValue()));
		}

		HttpResponse response;
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()){
			response = httpClient.execute(request);

            // -1 is not a valid response code, so we use it as a tempory value to check if
            // getting the status code failed. We check the status inside the if statement to
            // ensure that response is not null when we check.
			int code = -1;
			if (response == null || (code = response.getStatusLine().getStatusCode()) != 200) {
				throw -1 == code ?
						new RuntimeException("Http Request failed: Response null"):
						new RuntimeException("Failed : HTTP error code : " + code);
			}

			JSONObject parse = (JSONObject) JSONValue.parse(new InputStreamReader(response.getEntity().getContent()));

			@SuppressWarnings("unchecked")
			Map<String, Object> mapParse = (Map<String, Object>) parse;
			return mapParse;

		} catch (IOException e) {
			log.severe(e.getMessage());
			return new HashMap<>();
		}
	}
}
