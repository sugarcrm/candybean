/**
 * Candybean is a next generation automation and testing framework suite.
 * It is a collection of components that foster test automation, execution
 * configuration, data abstraction, results illustration, tag-based execution,
 * top-down and bottom-up batches, mobile variants, test translation across
 * languages, plain-language testing, and web service testing.
 * Copyright (C) 2013 SugarCRM, Inc. <candybean@sugarcrm.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * RunExamples.java --
 *
 *      Program to run the Voodoo2 example scripts.
 */

package com.sugarcrm.candybean.examples;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * <p>Program to run the Voodoo2 code examples.</p>
 *
 * @author Jon duSaint
 */

public class RunExamples {

   /**
    * Entry point of RunExamples
    *
    * @param args  Command line arguments
    */

   public static void main(String[] args) {
      RunExamples re = new RunExamples();
      re.load(args);
      re.run();
   }

   /**
    * Container for a Class,Method pair.
    *
    * <p>The method half of the pair is the entry point of the example
    * (the first method encountered that's annotated with @Example).</p>
    *
    * @author Jon duSaint
    */

   private class Pair {

      /**
       * The class field.
       */

      private Class<?> cls;

      /**
       * The method field.
       */

      private Method method;

      /**
       * Instantiate a Pair with a class and a method.
       *
       * @param cls     the example class
       * @param method  the example method
       */

      Pair(Class<?> cls, Method method) {
         this.setCls(cls);
         this.setMethod(method);
      }

	public Class<?> getCls() {
		return cls;
	}

	public void setCls(Class<?> cls) {
		this.cls = cls;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}
   }

   /**
    * Storage for the examples passed in on the command line.
    *
    * <p>The key is the canonical class name.  This implies that each
    * example can only be run once regardless of how many times it's
    * specified on the command line.</p>
    */

   private HashMap<String,Pair> classes;

   /**
    * Class loader for example classes.
    *
    * </p>This class loader supports loading classes from class files
    * without knowing the name of the class in advance.</p>
    *
    * @author Jon duSaint
    */

   private class CL extends ClassLoader {

      /**
       * Load a class from a file.
       *
       * @param f  the class file
       * @return the class from the file
       */

      public Class<?> loadClass(File f)
         throws FileNotFoundException, IOException {
         FileInputStream fin = new FileInputStream(f);
         ByteArrayOutputStream buffer = new ByteArrayOutputStream();
         byte bytes[];

         while (true) {
            int b = fin.read();
            if (b == -1) {
               break;
            }
            buffer.write(b);
         }

         try { 
        	 fin.close(); 
         } catch (IOException e) {
         }
         bytes = buffer.toByteArray();

         return defineClass(null, bytes, 0, bytes.length);
      }
   }

   /**
    * Class loader object used to load example classes.
    */

   private CL cl;

   /**
    * Instantiate an instance of this class.
    */

   public RunExamples() {
      this.cl = new CL();
   }

   /**
    * Print a help message.
    *
    * <p>This method is invoked when -h or --help is on the command
    * line.  The program is terminated after the help message is
    * printed.</p>
    */

   private void help() {
      log("RunExamples [-h|--help] <example class> ...");
      System.exit(0);
   }

   /**
    * Load an example class from its class file.
    *
    * @param f  class file
    */

   private Class<?> loadClass(File f) {
      Class<?> c = null;

      try {
         c = cl.loadClass(f);
      } catch (FileNotFoundException e) {
         error("Class file '" + f + "' not found: " + e);
      } catch (IOException e) {
         error("Error reading class file '" + f + "': " + e);
      }

      return c;
   }

   /**
    * Find and return the entry point of this example class.
    *
    * <p>If more than one method in this class is annotated
    * with @Example, only the first is returned.  If no method is
    * found or the class is null, null is returned.  In addition to
    * the @Example annotation, the method must return void and have no
    * parameters.</p>
    *
    * @param c  example class
    * @return the entry point to the example or null
    */

   private Method getEntryPoint(Class<?> c) {
      if (c == null) {
         return null;
      }

      for (Method m: c.getMethods()) {
         if (m.isAnnotationPresent(Example.class) &&
             m.getParameterTypes().length == 0 &&
             m.getGenericReturnType() == Void.TYPE) {
            return m;
         }
      }

      return null;
   }

   /**
    * Load all example classes.
    *
    * @param args  command line arguments
    */

   public void load(String args[]) {
      this.classes = new HashMap<String,Pair>();

      for (String arg: args) {
         Class<?> cls = null;

         if (arg.equals("--help") || arg.equals("-h")) {
            help();  // does not return
         }

         try {
            cls = cl.loadClass(arg);
         } catch (ClassNotFoundException e) {
            File f = new File(arg);
            if (arg.endsWith(".class") && f.exists() && f.isFile()) {
               cls = loadClass(f);
            } else {
               error("'" + arg + "' is not a class file or known class");
               continue;
            }
         }

         Method entry = getEntryPoint(cls);
         if (entry != null) {
            this.classes.put(cls.getCanonicalName(), new Pair(cls, entry));
         } else {
            error(cls.toString() + " has no @Example method");
         }
      }
   }

   /**
    * Run all example classes.
    */

   public void run() {
      for (String className: this.classes.keySet()) {
         Pair p = this.classes.get(className);
         log("Running example " + className);
         try {
            p.getMethod().invoke(p.getCls().newInstance());
         } catch (InstantiationException e) {
            error(e, "Failed to instantiate " + className);
         } catch (IllegalAccessException e) {
            error(e, "Unable to access " + className);
         } catch (java.lang.reflect.InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause == null) {
               cause = e;
            }
            error(e, "Exception during example invocation");
         }
      }
   }

   /**
    * Print a message.
    *
    * @param msg  the message to print
    */

   protected void log(String msg) {
      System.out.println(msg);
   }

   /**
    * Print an error message.
    *
    * @param errm  the error message
    */

   protected void error(String errm) {
      error((Throwable)null, errm);
   }

   /**
    * Print an error message and an exception backtrace.
    *
    * @param exc   the exception
    * @param errm  the error message
    */

   protected void error(Throwable exc, String errm) {
      System.err.println(errm + ":");
      if (exc != null) {
         exc.printStackTrace(System.err);
      }
   }
}
