package com.sugarcrm.candybean.server.configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.util.ajax.JSON;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.exceptions.CandybeanException;

@SuppressWarnings("serial")
public class LoadConfigurationServlet extends HttpServlet {

    @Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("application/json");   
    	PrintWriter out = response.getWriter();
    	try {
    		FormData data = getFormData();
    		String jsonRepresentation = JSON.DEFAULT.toJSON(data.getFormFields());
			out.print(jsonRepresentation);
		} catch (CandybeanException e) {
			throw new IOException(e);
		}
    	out.flush();
	}
    
    private FormData getFormData() throws CandybeanException, IOException{
		Candybean candybean = Candybean.getInstance();
		File configFile = candybean.config.configFile;
		BufferedReader br = new BufferedReader(new FileReader(configFile));
		String line;
		FormFieldData fieldData = new FormFieldData();
		FormData formData = new FormData();
		while ((line = br.readLine()) != null) {
			if(line.isEmpty()){
				//Do nothing
			} else {
				readComments(line, br, fieldData);
				formData.getFormFields().add(
						new FormFieldData(fieldData.getFieldComments(),
								fieldData.getFieldValue()));
			}
		}
		br.close();
		return formData;
    }
	private void readComments(String previousLine, BufferedReader br, FormFieldData fieldData) throws IOException {
		String line;
		String comments;
		if(previousLine.isEmpty()){
			comments = "";
		}else if(previousLine.startsWith("#")){
			comments = previousLine;
		}else {
			comments = "";
			fieldData.setFieldValue(previousLine);
			fieldData.setFieldComments(comments);
		}
		while ((line = br.readLine()) != null) {
			if(line.startsWith("#")){
				comments = comments.concat(line);
			}else if(line.isEmpty()){
				break;
			}else {
				fieldData.setFieldValue(line);
				fieldData.setFieldComments(comments);
			}
		}
	}
	
}
