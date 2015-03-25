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

import org.apache.http.entity.ContentType;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

//TODO: Mock these calls instead of actually making them
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
    public void testGetRequest() {
        try {
            response = WS.request(WS.OP.GET, uri + "/get", headers, "", ContentType.DEFAULT_TEXT);
        } catch (Exception e) {
            Assert.fail();
        }
        Assert.assertTrue(response != null);
        Assert.assertEquals(uri + "/get", response.get("url"));
    }

    @Test
    public void testPostRequest() {
        try {
            response = WS.request(WS.OP.POST, uri + "/post", headers, "Hello World", ContentType.APPLICATION_JSON);
        } catch (Exception e) {
            Assert.fail();
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
            Assert.fail();
        }
        Assert.assertTrue(response != null);
        Assert.assertEquals("value", ((JSONObject) response.get("json")).get("key"));
        Assert.assertEquals("value", ((Map) response.get("json")).get("key"));
        Assert.assertEquals("value2", ((JSONObject) response.get("json")).get("key2"));

    }

    @Test
    public void testPostRequestJSONMap() {
        Map<String,String> jsonData = new HashMap<>();
        jsonData.put("key1","value1");
        jsonData.put("key2","value2");
        jsonData.put("key3","value3");

        try {
            headers.put("Content-Type", "application/json");
            response = WS.request(WS.OP.POST, uri + "/post", headers, jsonData, ContentType.APPLICATION_JSON);
        } catch (Exception e) {
            Assert.fail();
        }
        Assert.assertTrue(response != null);
        Assert.assertEquals("value1", ((JSONObject) response.get("json")).get("key1"));
        Assert.assertEquals("value2", ((Map) response.get("json")).get("key2"));
        Assert.assertEquals("value3", ((JSONObject) response.get("json")).get("key3"));

    }

    @Test
    public void testPostRequestsFormData() {
        String formData = "test=asdf";
        try {
            headers.put("Content-Type", "application/x-www-form-urlencoded");
            response = WS.request(WS.OP.POST, uri + "/post", headers, formData, ContentType.APPLICATION_FORM_URLENCODED);
        } catch (Exception e) {
            Assert.fail();
        }
        Assert.assertTrue(response != null);
        Assert.assertEquals("asdf", ((Map) response.get("form")).get("test"));
    }

    @Test
    public void testPostRequestsMultiPartFormData() {
        Map<String,String> data = new HashMap<>();
        data.put("key1","value1");
        data.put("key2","value2");
        data.put("key3","value3");

        try {
            response = WS.request(WS.OP.POST, uri + "/post", headers, data, ContentType.MULTIPART_FORM_DATA);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
        Assert.assertTrue(response != null);
        Assert.assertEquals("value1", ((Map) response.get("form")).get("key1"));
        Assert.assertEquals("value2", ((Map)response.get("form")).get("key2"));
        Assert.assertEquals("value3", ((Map)response.get("form")).get("key3"));
    }
}
