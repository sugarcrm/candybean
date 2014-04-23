package com.sugarcrm.candybean.server.configuration;

import org.apache.commons.lang.StringEscapeUtils;

public class FormFieldData {
	
	private String fieldComments;
	private String fieldValue;
	
	public FormFieldData(){
		super();
	}
	
	public FormFieldData(String fieldComments, String fieldValue) {
		super();
		this.fieldComments = fieldComments;
		this.fieldValue = fieldValue;
	}
	
	public String getFieldComments() {
		return fieldComments;
	}
	public void setFieldComments(String fieldComments) {
		this.fieldComments = fieldComments;
	}
	public String getFieldValue() {
		return fieldValue;
	}
	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	@Override
	public String toString() {
		return "{\"comments\":\""+StringEscapeUtils.escapeJava(fieldComments)+"\"" + ",\"value\":\""+StringEscapeUtils.escapeJava(fieldValue)+"\"" +
				"}";
	}
	
	
}
