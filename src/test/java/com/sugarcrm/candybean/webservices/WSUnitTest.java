package com.sugarcrm.candybean.webservices;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class WSUnitTest {
	@Test
	public void testVerifyBody() {
		// The empty map is valid
		Map<String, Object> emptyMap = new HashMap<>();
		Assert.assertTrue("Empty map should be valid",
				WS.verifyBody(emptyMap));

		// A map with only one entry is valid
		Map<String, Object> simpleMap = new HashMap<>();
		simpleMap.put("Key", "Value");
		Assert.assertTrue("A map with only one entry should be valid",
				WS.verifyBody(simpleMap));

		// A map with multiple entries is valid
		Map<String, Object> multiMap = new HashMap<>();
		multiMap.put("Key1", "Value1");
		multiMap.put("Key2", "Value2");
		multiMap.put("Key3", "Value3");
		Assert.assertTrue("A map with multiple entries should be valid",
				WS.verifyBody(multiMap));

		// A nested map is valid
		Map<String, Object> nestedMap = new HashMap<>();
		nestedMap.put("Map", multiMap);
		Map<String, Object> veryNestedMap = new HashMap<>();
		veryNestedMap.put("NestedMap", simpleMap);
		nestedMap.put("Map2", veryNestedMap);
		Assert.assertTrue("A nested map should be valid",
				WS.verifyBody(nestedMap));

		// A map with a non String/Map<String,Object> value is invalid
		Map<String, Object> invalidObjectMap = new HashMap<>();
		invalidObjectMap.put("Key1", "Test");
		invalidObjectMap.put("Key2", "Value2");
		invalidObjectMap.put("Key1", 1);
		invalidObjectMap.put("Key5", "Value");
		Assert.assertFalse("A map with a non String/Map<String,Object> should be invalid",
				WS.verifyBody(invalidObjectMap));

		// A nested map with a non String/Map<String,Object> value is invalid
		Map<String, Object> nestedInvalidObjectMap = new HashMap<>();
		nestedInvalidObjectMap.put("Map1", multiMap);
		nestedInvalidObjectMap.put("Map2", nestedMap);
		nestedInvalidObjectMap.put("Map3", invalidObjectMap);
		nestedInvalidObjectMap.put("Map4", simpleMap);
		Assert.assertFalse("A nested map with a non String/Map<String,Object> should be invalid",
				WS.verifyBody(nestedInvalidObjectMap));
	}
}
