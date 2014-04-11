package com.sugarcrm.candybean.server.configuration;

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

	public static Logger logger = Logger.getLogger(SaveConfigurationServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		@SuppressWarnings("unchecked")
		Map<String,String[]> parameterMap = request.getParameterMap();
		try {
			Candybean candybean = Candybean.getInstance();
			Configuration config = candybean.config;
			Properties props = config.getPropertiesCopy();
			for(String key: parameterMap.keySet()){
				if (props.containsKey(key)) {
					String[] values = parameterMap.get(key);
					String newValue = values.length > 0? values[0]:"";
					if (StringUtils.isNotEmpty(newValue)) {
						props.remove(key);
						props.setProperty(key, newValue);
					}
				}
			}
			config.overwrite(props);
			logger.info("Candybean configuration saved");
			response.getWriter().println("<h1>Candybean configuration saved successfully</h1>");
		} catch (CandybeanException e) {
			logger.severe("Unable to save candybean configuration");
			response.getWriter().println("<h1>Unable to save configuration</h1>");
		}
		
	}
	
}
