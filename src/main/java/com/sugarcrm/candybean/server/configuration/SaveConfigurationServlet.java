package com.sugarcrm.candybean.server.configuration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.exceptions.CandybeanException;

@SuppressWarnings("serial")
public class SaveConfigurationServlet extends HttpServlet {

	public static Logger logger = Logger
			.getLogger(SaveConfigurationServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		@SuppressWarnings("unchecked")
		Map<String, String[]> parameterMap = request.getParameterMap();
		try {
			Candybean candybean = Candybean.getInstance();
			Configuration config = candybean.config;
			Properties props = config.getPropertiesCopy();
			for (String key : parameterMap.keySet()) {
				String[] values = parameterMap.get(key);
				String newValue = values.length > 0 ? values[0] : "";
				if (StringUtils.isNotEmpty(newValue)) {
					props.remove(key);
					props.setProperty(key, newValue);
				}
			}
			writeNewConfigFile(config, props);
			logger.info("Candybean configuration saved");
			response.getWriter().println("<h1>Candybean configuration saved successfully</h1>");
		} catch (CandybeanException e) {
			logger.severe("Unable to save candybean configuration");
			response.getWriter().println("<h1>Unable to save configuration</h1>");
		}

	}

	private void writeNewConfigFile(Configuration config, Properties props)
			throws IOException {
		File f = config.configFile;
		f.delete();
		f.createNewFile();
		FileWriter fw = new FileWriter(f.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		//Write header first if it exists
		if(props.containsKey("configuration.header")){
			String[] headerLines = props.get("configuration.header").toString().split("###");
			if (headerLines.length > 0) {
				for (String headerLine : headerLines) {
						bw.write("###" + headerLine + "\n");
				}
				bw.write("\n");
			}
		}
		//Write the remaining key value pairs
		for (Object key : props.keySet()) {
			String mapKey = key.toString();
			if (!mapKey.endsWith(".desc.hidden") && !mapKey.equals("configuration.header")) {
				String descKey = mapKey + ".desc.hidden";
				if (props.containsKey(descKey)) {
					String[] comments = props.get(descKey).toString()
							.split("#");
					if (comments.length > 0) {
						for (String comment : comments) {
							if (!comment.isEmpty()) {
								bw.write("#" + comment + "\n");
							}
						}
					}
				}
				bw.write(key + "=" + props.get(key) + "\n");
			}
		}
		bw.close();
	}

}
