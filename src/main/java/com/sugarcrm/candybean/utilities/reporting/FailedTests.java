package com.sugarcrm.candybean.utilities.reporting;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FailedTests {
	
	private List<TestFailure> failures = new ArrayList<TestFailure>();

	public List<TestFailure> getFailures() {
		return failures;
	}

	public void setFailures(List<TestFailure> failures) {
		this.failures = failures;
	}

}
