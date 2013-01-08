/*
 * Select.java --
 *
 *      Demonstrate usage of VSelect.
 */

package com.sugarcrm.sugar.examples;

import com.sugarcrm.voodoo.automation.control.VControl;
import com.sugarcrm.voodoo.automation.control.VSelect;
import com.sugarcrm.voodoo.automation.control.VHook;
import com.sugarcrm.sugar.SugarTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;

/**
 * The Select class demonstrates the usage of {@link VSelect} to
 * interact with SELECT elements.
 *
 * @author Jon duSaint
 */

public class Select extends SugarTest {

   /**
    * Run the Select test.
    */

   @Test
   public void test() throws Exception {
      /* Open the browser */
      iface.start();

      /* Close the browser. */
      iface.stop();
   }

   /* Override these two to prevent logging into/out of sugar. */
   @Override @Before public void setup() throws Exception {}
   @Override @After public void cleanup() throws Exception {}
}
