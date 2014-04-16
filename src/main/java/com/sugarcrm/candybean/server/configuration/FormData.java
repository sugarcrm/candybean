package com.sugarcrm.candybean.server.configuration;

import java.util.ArrayList;

public class FormData {
	
	private ArrayList<FormFieldData> formFields = new ArrayList<FormFieldData>();

	public ArrayList<FormFieldData> getFormFields() {
		return formFields;
	}

	public void setFormFields(ArrayList<FormFieldData> formFields) {
		this.formFields = formFields;
	}

}
