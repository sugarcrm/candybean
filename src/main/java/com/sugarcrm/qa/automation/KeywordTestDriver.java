package com.sugarcrm.qa.automation;

import au.com.bytecode.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Method;

import org.openqa.selenium.WebDriver;

public class KeywordTestDriver {	
	private final Trellis trellis;
	private final File[] testFiles;
	
	public KeywordTestDriver(Trellis trellis, File[] testFiles) {
		this.trellis = trellis;
		this.testFiles = testFiles;
	}
	
	public void execute(WebDriver browser) throws Exception {
		for (File testFile : testFiles) {
			CSVReader csvReader = new CSVReader(new FileReader(testFile), ',');
		    String[] line;
			while ((line = csvReader.readNext()) != null) {
				if (!line[0].startsWith("#")) { // SKIP LINE
					//String[] classMethod = trellis.getClassMethod(line[0]).split(";");
					//Method method = null;
					switch (line[0].trim().toUpperCase()) {
					case "CLOSE_POPUP": // 0 arguments
//						trellis.log.info("Clearing popup.");
//						Navigation.closePopupWindow(browser);
						break;
					case "CREATE_CUSTOMER": // 12 arguments
//						//method = this.getClass().getClassLoader().loadClass(classMethod[0]).getDeclaredMethod(classMethod[1], WebDriver.class, Trellis.class, Customer.class, boolean.class, boolean.class);
//						CustomerBuilder customerBuilder = new CustomerBuilder();
//						Customer customer = customerBuilder.withCustomerCode(line[1])
//								.withShortName(line[2])
//								.withName(line[3])
//								.withDUNSNum(line[4])
//								.withStateOfInc(line[5])
//								.withCustomerCategory(line[6])
//								.withFedTaxId(line[7])
//								.withStateTaxId(line[8])
//								.withCustomerType(line[9])
//								.withStatus(line[10])
//								.withPaymentTerm(line[11]).build();
//						trellis.log.info("Invoking createCustomer with new customer:" + customer.toString());
//						ManageCustomer.createCustomer(browser, trellis, customer, true, false);
						break;
					case "PAUSE": // 0 arguments
						trellis.log.info("Pausing keyword-driven execution.");
						javax.swing.JOptionPane.showInputDialog("");
						break;
					case "SWITCH_TO_MAIN_WINDOW": // 0 arguments
//						trellis.log.info("Switch focus to main window.");
//						Navigation.switchToMainWindow(browser);
						break;
					case "VERIFY_TEXT": // 4 arguments
						//method = this.getClass().getClassLoader().loadClass(classMethod[0]).getDeclaredMethod(classMethod[1], WebDriver.class, String.class, String.class, String.class);
//						trellis.log.info("Invoking verifyText with:" + line);
//						Verify.verifyText(browser, line[1], line[2], line[3]);
						break;
					default:
						throw new Exception("Keyword not recognized.");
					}
				}
			}
		}
	}
}
