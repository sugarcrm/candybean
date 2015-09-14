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
import org.apache.http.entity.ContentType;
import org.json.simple.JSONObject;
import org.junit.*;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

public class WSSystemTest {
	String uri = "http://httpbin.org";
	Map<String, String> headers = new HashMap<>();
	Map<String, Object> response;

	@Before
	public void setUp() {
		headers.put("Accept", "application/json");
	}

	@After
	public void cleanUp() {
		response = null;
		headers.clear();
	}

	@Test
	public void testHeaders() {
		headers.put("Test-Header-Key", "Test-Header-Value");
		try {
			response = WS.request(WS.OP.GET, uri + "/headers", headers, "", ContentType.DEFAULT_TEXT);
		} catch (Exception e) {
			Assert.fail(e.toString());
		}
		Assert.assertTrue(response != null);
		Assert.assertEquals("application/json", ((JSONObject) response.get("headers")).get("Accept"));
		Assert.assertEquals("Test-Header-Value", ((JSONObject) response.get("headers")).get("Test-Header-Key"));
	}

	@Test
	public void testHTTPError() {
		ExpectedException exception = ExpectedException.none();
		try {
			exception.expect(CandybeanException.class);
			response = WS.request(WS.OP.POST, uri + "/get", headers, "", ContentType.DEFAULT_TEXT);
			Assert.fail();
		} catch (CandybeanException e) {
			Assert.assertEquals("HTTP request received HTTP code: 405",
					e.getMessage().split("\n")[0]);
		}
	}

	@Test
	@Ignore("This test should pass, but takes a full minute to do so because it" +
			"waits for the response to time out.")
	public void testResponseError() {
		ExpectedException exception = ExpectedException.none();
		try {
			exception.expect(CandybeanException.class);
			// Send to an IP address that does not exist
			response = WS.request(WS.OP.POST, "http://240.0.0.0", headers, "", ContentType.DEFAULT_TEXT);
			Assert.fail();
		} catch (CandybeanException e) {
			Assert.assertEquals("Connect to 240.0.0.0:80 [/240.0.0.0] failed: Operation timed out", e.getMessage());
		}
	}

	@Test
	public void testGetRequest() {
		try {
			response = WS.request(WS.OP.GET, uri + "/get", headers, "", ContentType.DEFAULT_TEXT);
		} catch (Exception e) {
			Assert.fail(e.toString());
		}
		Assert.assertTrue(response != null);
		Assert.assertEquals(uri + "/get", response.get("url"));
	}

	@Test
	public void testDeleteRequest() {
		try {
			response = WS.request(WS.OP.DELETE, uri + "/delete", headers, "", ContentType.DEFAULT_TEXT);
		} catch (Exception e) {
			Assert.fail(e.toString());
		}
		Assert.assertTrue(response != null);
		Assert.assertEquals(uri + "/delete", response.get("url"));
	}

	@Test
	public void testPutRequest() {
		try {
			response = WS.request(WS.OP.PUT, uri + "/put", headers, "Hello World", ContentType.APPLICATION_JSON);
		} catch (Exception e) {
			Assert.fail(e.toString());
		}
		Assert.assertTrue(response != null);
		// The field data will contain what ever we sent it
		Assert.assertEquals("Hello World", response.get("data"));
	}

	@Test
	public void testPutRequestJSONStr() {
		String jsonData = "{\"key\":\"value\", \"key2\":\"value2\"}";
		try {
			headers.put("Content-Type", "application/json");
			response = WS.request(WS.OP.PUT, uri + "/put", headers, jsonData, ContentType.APPLICATION_JSON);
		} catch (Exception e) {
			Assert.fail(e.toString());
		}
		Assert.assertTrue(response != null);
		Assert.assertEquals("value", ((JSONObject) response.get("json")).get("key"));
		Assert.assertEquals("value", ((Map) response.get("json")).get("key"));
		Assert.assertEquals("value2", ((JSONObject) response.get("json")).get("key2"));

	}

	@Test
	public void testPutRequestJSONMap() {
		Map<String, Object> jsonData = new HashMap<>();
		jsonData.put("key1", "value1");
		jsonData.put("key2", "value2");
		jsonData.put("key3", "value3");

		try {
			headers.put("Content-Type", "application/json");
			response = WS.request(WS.OP.PUT, uri + "/put", headers, jsonData, ContentType.APPLICATION_JSON);
		} catch (Exception e) {
			Assert.fail(e.toString());
		}
		Assert.assertTrue(response != null);
		Assert.assertEquals("value1", ((JSONObject) response.get("json")).get("key1"));
		Assert.assertEquals("value2", ((Map) response.get("json")).get("key2"));
		Assert.assertEquals("value3", ((JSONObject) response.get("json")).get("key3"));

	}

	@Test
	public void testPutRequestsFormData() {
		String formData = "test=asdf";
		try {
			headers.put("Content-Type", "application/x-www-form-urlencoded");
			response = WS.request(WS.OP.PUT, uri + "/put", headers, formData, ContentType.APPLICATION_FORM_URLENCODED);
		} catch (Exception e) {
			Assert.fail(e.toString());
		}
		Assert.assertTrue(response != null);
		Assert.assertEquals("asdf", ((Map) response.get("form")).get("test"));
	}

	@Test
	public void testPutRequestsMultiPartFormData() {
		Map<String, Object> data = new HashMap<>();
		data.put("key1", "value1");
		data.put("key2", "value2");
		data.put("key3", "value3");

		try {
			response = WS.request(WS.OP.PUT, uri + "/put", headers, data, ContentType.MULTIPART_FORM_DATA);
		} catch (Exception e) {
			Assert.fail(e.toString());
		}
		Assert.assertTrue(response != null);
		Assert.assertEquals("value1", ((Map) response.get("form")).get("key1"));
		Assert.assertEquals("value2", ((Map) response.get("form")).get("key2"));
		Assert.assertEquals("value3", ((Map) response.get("form")).get("key3"));
	}

	@Test
	public void testPostRequest() {
		try {
			response = WS.request(WS.OP.POST, uri + "/post", headers, "Hello World", ContentType.APPLICATION_JSON);
		} catch (Exception e) {
			Assert.fail(e.toString());
		}
		Assert.assertTrue(response != null);
		// The field data will contain what ever we sent it
		Assert.assertEquals("Hello World", response.get("data"));
	}

	@Test
	public void testPostRequestJSONStr() {
		String jsonData = "{\"key\":\"value\", \"key2\":\"value2\"}";
		try {
			headers.put("Content-Type", "application/json");
			response = WS.request(WS.OP.POST, uri + "/post", headers, jsonData, ContentType.APPLICATION_JSON);
		} catch (Exception e) {
			Assert.fail(e.toString());
		}
		Assert.assertTrue(response != null);
		Assert.assertEquals("value", ((JSONObject) response.get("json")).get("key"));
		Assert.assertEquals("value", ((Map) response.get("json")).get("key"));
		Assert.assertEquals("value2", ((JSONObject) response.get("json")).get("key2"));
	}

	@Test
	public void testPostRequestJSONMap() {
		Map<String, Object> jsonData = new HashMap<>();
		jsonData.put("key1", "value1");
		jsonData.put("key2", "value2");
		jsonData.put("key3", "value3");
		try {
			headers.put("Content-Type", "application/json");
			response = WS.request(WS.OP.POST, uri + "/post", headers, jsonData, ContentType.APPLICATION_JSON);
		} catch (Exception e) {
			Assert.fail(e.toString());
		}
		Assert.assertTrue(response != null);
		Assert.assertEquals("value1", ((JSONObject) response.get("json")).get("key1"));
		Assert.assertEquals("value2", ((Map) response.get("json")).get("key2"));
		Assert.assertEquals("value3", ((JSONObject) response.get("json")).get("key3"));
	}

	@Test
	public void testPostRequestNestedJSON() {
		Map<String, Object> jsonData = new HashMap<>();
		Map<String, Object> nestedJSONData1 = new HashMap<>();
		Map<String, Object> nestedJSONData2= new HashMap<>();
		jsonData.put("key1", "value1");
		nestedJSONData1.put("key4", "value4");
		nestedJSONData1.put("key5", "value5");
		nestedJSONData2.put("key6", "value6");
		jsonData.put("key2", nestedJSONData1);
		jsonData.put("key3", nestedJSONData2);
		try {
			headers.put("Content-Type", "application/json");
			response = WS.request(WS.OP.POST, uri + "/post", headers, jsonData, ContentType.APPLICATION_JSON);
		} catch (Exception e) {
			Assert.fail(e.toString());
		}
		Assert.assertTrue(response != null);
		Assert.assertEquals("value1", ((JSONObject) response.get("json")).get("key1"));
		Assert.assertEquals("value4", ((Map) ((Map) response.get("json")).get("key2")).get("key4"));
		Assert.assertEquals("value5", ((JSONObject) ((JSONObject) response.get("json")).get("key2")).get("key5"));
		Assert.assertEquals("value6", ((Map) ((JSONObject) response.get("json")).get("key3")).get("key6"));
	}

	@Test
	public void testPostRequestsFormData() {
		String formData = "test=asdf";
		try {
			headers.put("Content-Type", "application/x-www-form-urlencoded");
			response = WS.request(WS.OP.POST, uri + "/post", headers, formData, ContentType.APPLICATION_FORM_URLENCODED);
		} catch (Exception e) {
			Assert.fail(e.toString());
		}
		Assert.assertTrue(response != null);
		Assert.assertEquals("asdf", ((Map) response.get("form")).get("test"));
	}

	@Test
	public void testPostRequestsMultiPartFormData() {
		Map<String, Object> data = new HashMap<>();
		data.put("key1", "value1");
		data.put("key2", "value2");
		data.put("key3", "value3");

		try {
			response = WS.request(WS.OP.POST, uri + "/post", headers, data, ContentType.MULTIPART_FORM_DATA);
		} catch (Exception e) {
			Assert.fail(e.toString());
		}
		Assert.assertTrue(response != null);
		Assert.assertEquals("value1", ((Map) response.get("form")).get("key1"));
		Assert.assertEquals("value2", ((Map) response.get("form")).get("key2"));
		Assert.assertEquals("value3", ((Map) response.get("form")).get("key3"));
	}

}
