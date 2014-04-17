package com.sugarcrm.candybean.server.configuration;

import java.util.ArrayList;

import org.apache.commons.lang.StringEscapeUtils;
import org.eclipse.jetty.util.ajax.JSON;

public class FormData {
	
	private ArrayList<FormFieldData> formFields = new ArrayList<FormFieldData>();
	
	private String header = "";

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public ArrayList<FormFieldData> getFormFields() {
		return formFields;
	}

	public void setFormFields(ArrayList<FormFieldData> formFields) {
		this.formFields = formFields;
	}

	@Override
	public String toString() {
		return "{\"header\":\""+StringEscapeUtils.escapeJava(header)+"\"" + ",\"formFields\":"+JSON.DEFAULT.toJSON(formFields)+
				"}";
	}
	
	

}
