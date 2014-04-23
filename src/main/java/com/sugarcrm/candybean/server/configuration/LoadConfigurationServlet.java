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
    		String jsonRepresentation = JSON.DEFAULT.toJSON(data);
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
		FormData formData = new FormData();
		formData.setHeader(readHeader(br));
		@SuppressWarnings("unused")
		String line;
		br.mark(0);
		while ((line = br.readLine()) != null) {
			try {
				br.reset();
				formData.getFormFields().add(new FormFieldData(readComments(br), readKeyValueEntry(br)));
				br.mark(0);
			} catch (Exception e) {
				continue;
			}
		}
		br.close();
		return formData;
    }
    
	private String readComments(BufferedReader br) throws IOException {
		String line;
		String comments = "";
		br.mark(0);
		while ((line = br.readLine()) != null) {
			if(line.startsWith("#") || line.isEmpty()){
				comments = comments.concat(line);
			}else{
				br.reset();
				break;
			}
			br.mark(0);
		}
		return comments;
		
	}
    
	private String readKeyValueEntry(BufferedReader br) throws IOException {
		String line;
		String keyValueEntry = "";
		int keyValuePairsEncountered = 0;
		br.mark(0);
		while ((line = br.readLine()) != null) {
			if(line.contains("=")){
				keyValuePairsEncountered++;
			}
			if(!line.startsWith("#") && !line.isEmpty() && keyValuePairsEncountered <= 1){
				keyValueEntry = keyValueEntry.concat(line);
			}else{
				br.reset();
				break;
			}
			br.mark(0);
		}
		return keyValueEntry;
	}

	private String readHeader(BufferedReader br) throws IOException {
		String line;
		String header = "";
		br.mark(0);
		while ((line = br.readLine()) != null) {
			if(line.startsWith("###") || line.isEmpty()){
				header = header.concat(line);
			}else{
				br.reset();
				break;
			}
			br.mark(0);
		}
		return header;
	}
	
}
