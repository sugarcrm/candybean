package com.sugarcrm.sugar.test.forecasts;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sugarcrm.sugar.SugarTest;
import com.sugarcrm.sugar.admin.TimePeriod;
import com.sugarcrm.sugar.admin.TimePeriods;


public class ForecastsTest extends SugarTest {
	
	private TimePeriod timePeriod;
	
	@BeforeClass
	public static void setupOnce() throws Exception { SugarTest.setupOnce(); }

	@Override
	@Before
	public void setup() throws Exception {
		super.setup();
		timePeriod = new TimePeriod("tp_new", true, "12/01/2012", "11/30/2013");
		TimePeriods.create(voodoo, sugar, timePeriod);
//		1. All default Time Periods are removed from the system 
//		2. 5 custom time periods exist with the following data (YY represents the current year, MM represents the current month, 2M represents month MM+2, 3M = MM+3, 6M = MM+6)
//        Name      -     Start Date       -       End Date     - Is Fiscal Year    -    Fiscal Year    - Forecast Start Date
//        Time1     -       MM/1/YY        -       12/31/YY     - No                -    None           - 1/1/YY
//        Time2     -       1/1/YY         -       12/31/YY     - Yes               -    N/A            - 1/1/YY
//        Time3     -       MM/1/YY        -       2M/30/YY     - No                -    Time2          - 1/1/YY
//        Time4     -       MM/1/YY        -       3M/30/YY     - No                -    None           - 1/1/YY
//        Time5     -       MM/1/YY        -       6M/30/YY     - No                -    None           - 1/1/YY
	}
	
	@Test
	public void test10968() throws Exception {
		
	}

	@Override
	@After
	public void cleanup() throws Exception { super.cleanup(); }

	@AfterClass
	public static void cleanupOnce() { SugarTest.cleanupOnce(); }
}
