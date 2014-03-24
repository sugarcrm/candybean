package com.sugarcrm.candybean.utilities.reporting;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FailedTests {
	
	private Map<String,TestFailure> failures = new HashMap<String,TestFailure>();

	public Map<String, TestFailure> getFailures() {
		return failures;
	}

	public void setFailures(Map<String, TestFailure> failures) {
		this.failures = failures;
	}

}
