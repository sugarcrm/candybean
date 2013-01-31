/*
 * Search.java --
 *
 *      Demonstrate working with HTML elements via VControl by running
 *      a Google search.
 */


package com.sugarcrm.sugar.examples;

import com.sugarcrm.voodoo.automation.control.VControl;
import com.sugarcrm.voodoo.automation.control.VHook;
import com.sugarcrm.sugar.SugarTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;

/**
 * The Search class demonstrates working with web elements via
 * {@link VControl} by running a Google search.
 *
 * @author Jon duSaint
 */

public class Search extends SugarTest {

   /**
    * Run the Search test.
    *
    * <p>In this test, a google search for &quot;Voodoo Driver&quot;
    * is performed and the text of the first hit is logged.</p>
    */

   @Test
   public void test() throws Exception {
      /* Open the browser */
      iface.start();

      /* Load Google's search page */
      iface.go("http://www.google.com");
      
      /* Get the search box */
      VControl q = iface.getControl(new VHook(VHook.Strategy.NAME, "q"));

      /* Enter our query */
      q.sendString("Voodoo Driver");

      /* Allow a little time for Google's AJAXy goo to run */
      iface.pause(250);

      /* Click the search button */
      VControl b = iface.getControl(new VHook(VHook.Strategy.NAME, "btnG"));
      b.click();

      /* Arbitrary wait for results to be returned. */
      iface.pause(1000);

      try {
         /*
          * Get the first hit.  The search results are in an ordered
          * list inside of a div with the id "ires".
          */
         VControl hit = iface.getControl(new VHook(VHook.Strategy.CSS,
                                                   "div#ires>ol>li a"));
         voodoo.log.info("First hit: '" + hit.getText() + "'");
      } catch (Exception e) {
         voodoo.log.severe("Element not found: " + e);
      }

      /* Close the browser. */
      iface.stop();
   }

   /* Override these two to prevent logging into/out of sugar. */
   @Override @Before public void setup() throws Exception {}
   @Override @After public void cleanup() throws Exception {}
}
