package com.voodoo2;


public class DataTestDriver {	
//	public static enum TYPE { CREATE_CUSTOMER }
//	private final Trellis trellis;
//	private final File testFile;
//	private final TYPE type;
//	
//	public DataTestDriver(Trellis trellis, File testFile, TYPE type) {
//		this.trellis = trellis;
//		this.testFile = testFile;
//		this.type = type;
//	}
//	
//	public void execute(WebDriver browser) throws Exception {
//		CSVReader csvReader = new CSVReader(new FileReader(testFile), ',');
//	    String[] line;
//		while ((line = csvReader.readNext()) != null) {
//			if (!line[0].startsWith("#")) { // SKIP LINE
//				String[] classMethod = trellis.getClassMethod(this.type.name()).split(";");
//				trellis.log.info("Parsing CSV:" + testFile.getAbsolutePath() + ", type:" + type.name());
//				switch (this.type) {
//				case CREATE_CUSTOMER: // 12 arguments
////					Method method = this.getClass().getClassLoader().loadClass(classMethod[0]).getDeclaredMethod(classMethod[1], WebDriver.class, Trellis.class, Customer.class, boolean.class, boolean.class);
////					CustomerBuilder customerBuilder = new CustomerBuilder();
////					Customer customer = customerBuilder.withCustomerCode(line[0])
////							.withShortName(line[1])
////							.withName(line[2])
////							.withDUNSNum(line[3])
////							.withStateOfInc(line[4])
////							.withCustomerCategory(line[5])
////							.withFedTaxId(line[6])
////							.withStateTaxId(line[7])
////							.withCustomerType(line[8])
////							.withStatus(line[9])
////							.withPaymentTerm(line[10]).build();
////					trellis.log.info("Invoking " + classMethod[1] + " with new customer " + customer.toString());
////					method.invoke(null, browser, trellis, customer, true, false);
//////					javax.swing.JOptionPane.showInputDialog("");
////					try {
////						Assert.assertTrue(browser.findElement(By.id(line[11])).isDisplayed());
////					} catch (Exception e) {
////						Assert.fail("Unexpected exception when finding, asserting visibility for element:" + line[11]);
////					} finally {
////						browser.close();
////						Set<String> windows = browser.getWindowHandles();
////						if (windows.size() > 1) throw new Exception("More than one window open; expecting 1");
////						for (String window : windows) browser.switchTo().window(window);
////						IdentityManagement.logout(browser, trellis);
////						browser.quit();
////					}
//					break;
//				}
//			}
//		}
//		csvReader.close();
//	}
}
