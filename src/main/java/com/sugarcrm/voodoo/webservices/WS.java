package com.sugarcrm.voodoo.webservices;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.Header;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.json.JSONObject;
import org.json.simple.JSONValue;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Conrad Warmbold
 *
 */
public class WS {
	
	public enum TYPE { JSON, XML };
	public enum OP { DELETE, GET, POST, PUT };

	public static String getValue(TYPE type, String body, String key) throws Exception {
		String value = null;
		switch (type) {
			case JSON:
				JSONObject jsonObj = (JSONObject)JSONValue.parse(body);
				//TODO
				break;
			case XML:
				SAXParserFactory factory = SAXParserFactory.newInstance();
			    factory.setValidating(true);
			    SAXParser saxParser = factory.newSAXParser();
		        saxParser.parse(new ByteArrayInputStream(body.getBytes()), new DefaultHandler());
		        //TODO
		    	break;
			default:
				throw new Exception("WS:TYPE not recognized.");
		}
		return value;
	}
	
	public static String request(OP op, URI uri, Map<String, String> headers, String body) throws Exception {
		HttpClient httpClient = new DefaultHttpClient();
		HttpUriRequest request = null;
		switch (op) {
			case DELETE:
				request = new HttpDelete(uri);
				break;
			case GET:
				request = new HttpGet(uri);
				break;
			case POST:
				HttpPost post = new HttpPost(uri);
				if (body != null) post.setEntity(new StringEntity(body));
				request = post;
				break;
			case PUT:
				HttpPut put = new HttpPut(uri);
				if (body != null) put.setEntity(new StringEntity(body));
				request = put;
				break;
			default:
				throw new Exception("WS:OP type recognized.");
		}
		Header[] hdrs = new Header[headers.size()];
		int i = 0;
		for (Map.Entry<String, String> header : headers.entrySet()) {
			hdrs[i++] = new BasicHeader(header.getKey(), header.getValue());
		}
		request.setHeaders(hdrs);
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
        return httpClient.execute(request, responseHandler);
	}
	
	public static void main(String[] args) {
		try {
//			System.out.println("=======decode=======");
//			String s = "[0,{\"1\":{\"2\":{\"3\":{\"4\":[5,{\"6\":7}]}}}}]";
//			Object obj = JSONValue.parse(s);
//			JSONArray arr = (JSONArray) obj;
//			System.out.println(arr.get(1));
//			System.out.println(arr.has("2"));
//			System.out.println(arr.get("2"));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
