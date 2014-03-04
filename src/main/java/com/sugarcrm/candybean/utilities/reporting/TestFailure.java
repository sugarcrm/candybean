package com.sugarcrm.candybean.utilities.reporting;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TestFailure {

	private String pathToVideo;
	
	private String className;
	
	private String methodName;
	
	private String failMessage;
	
	private String testHeader;
	
	private String trace;
	
	public String getPathToVideo() {
		return pathToVideo;
	}

	public void setPathToVideo(String pathToVideo) {
		this.pathToVideo = pathToVideo;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getFailMessage() {
		return failMessage;
	}

	public void setFailMessage(String failMessage) {
		this.failMessage = failMessage;
	}

	public String getTestHeader() {
		return testHeader;
	}

	public void setTestHeader(String testHeader) {
		this.testHeader = testHeader;
	}

	public String getTrace() {
		return trace;
	}

	public void setTrace(String trace) {
		this.trace = trace;
	}
	
	

}
