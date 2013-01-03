/*
 * Browser.java --
 *
 *      Simple example of browser control.
 */

package com.sugarcrm.sugar.examples;

import com.sugarcrm.sugar.SugarTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;

/**
 * The Browser class demonstrates basic web browser control for tests.
 *
 * @author Jon duSaint
 */

public class Browser extends SugarTest {

   /**
    * Run the basic browser test.
    *
    * <p>In this example, the following methods are demonstrated:
    * <dl>
    *   <dd><code>IInterface.start</code></dd>
    *   <dt>Open a browser window</dt>
    *   <dd><code>IInterface.go</code></dd>
    *   <dt>Load a web page</dt>
    *   <dd><code>IInterface.pause</code></dd>
    *   <dt>Pause program execution</dt>
    *   <dd><code>IInterface.stop</code></dd>
    *   <dt>Close the browser</dt>
    * </dl>
    * </p>
    *
    * <p>The <code>@Test</code> annotation is placed on the method
    * that will be run by the testing framework; it is the test's
    * entry point.</p>
    */

   @Test
   public void test() throws Exception {
      System.out.println("iface=" + iface + ", voodoo=" + voodoo);

      /* Open the browser window and prepare for testing. */
      iface.start();

      /* Load a web page  */
      iface.go("http://m.google.com");

      /* Wait half a second to allow the page to load. */
      iface.pause(500);

      /* Maximize the window */
      iface.maximize();

      /* Close the browser window and perform cleanup. */
      iface.stop();
   }

   /* Override these two to prevent logging into/out of sugar. */
   @Override @Before public void setup() throws Exception {}
   @Override @After public void cleanup() throws Exception {}
}
