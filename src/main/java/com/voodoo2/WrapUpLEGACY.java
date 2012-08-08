package com.voodoo2;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WrapUpLEGACY {

	private static String archivePath = System.getProperty("user.dir") + "/archive";
	private static String logPath = System.getProperty("user.dir") + "/log";
	private static String reportPath = System.getProperty("user.dir") + "/report";
	private static String screenshotPath = System.getProperty("user.dir") + "/screenshot";
	private static String siteDocPath = System.getProperty("user.dir") + "/doc/site";
	private static String sitePath =  System.getProperty("user.dir") + "/target/site"; 
	private static String srcHtml = "C:/Users/pyang/bstone-qa/target/site/Trellis_Automation.html";
	private static String destHtml = "C:/Users/pyang/bstone-qa/target/site/temp_Trellis_Automation.html";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		WrapUpLEGACY wu = new WrapUpLEGACY();
		
//		String logPath = System.getProperty("user.dir") + "/log";
//		String reportPath = System.getProperty("user.dir") + "/report";
//		String screenshotPath = System.getProperty("user.dir") + "/screenshot";
//		String siteDocPath = System.getProperty("user.dir") + "/doc/site";
//		String sitePath =  System.getProperty("user.dir") + "/target/site"; 
		
//		System.out.println("logPath=" + logPath);
//		System.out.println("sitePathh=" + sitePath);
		
		
		
		
		
		wu.copyDir((new File(logPath)), (new File(sitePath + "/log")));
		wu.copyDir((new File(reportPath)), (new File(sitePath + "/report")));
		wu.copyDir((new File(screenshotPath)), (new File(sitePath + "/screenshot")));
		wu.copyDir((new File(siteDocPath)), (new File(sitePath)));
		
		wu.updateResultHtml();
//		
		Date date = new Date();
		String DATE_FORMAT = "MM_dd_yyyy_HH_mm_ss";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		
		String currentArchive = archivePath + "/" + sdf.format(date);
		wu.copyDir((new File(sitePath)), (new File(currentArchive)));
		
		System.out.println("Finished wrapping up test. Please refer to file:///archive/" + sdf.format(date) + "/Trellis_Automation.html for test report.");
		
	}
	
	public void copyDir(File src, File dest) throws IOException 
	{
		//Check to ensure that the source is valid...
		if (!src.exists()) 
		{
			throw new IOException("copyFiles: Can not find source: " + src.getAbsolutePath()+".");
		} 
		else if (!src.canRead()) 
		{ //check to ensure we have rights to the source...
			throw new IOException("copyFiles: No right to source: " + src.getAbsolutePath()+".");
		}
		//is this a directory copy?
		if (src.isDirectory()) 	
		{
			if (!dest.exists()) 
			{ //does the destination already exist?
				//if not we need to make it exist if possible (note this is mkdirs not mkdir)
				if (!dest.mkdirs()) 
				{
					throw new IOException("copyFiles: Could not create direcotry: " + dest.getAbsolutePath() + ".");
				}
			}
			//get a listing of files...
			String list[] = src.list();
			//copy all the files in the list.
			for (int i = 0; i < list.length; i++)
			{
				File dest1 = new File(dest, list[i]);
				File src1 = new File(src, list[i]);
				copyDir(src1 , dest1);
			}
		} 
		else 
		{ 
			//This was not a directory, so lets just copy the file
			FileInputStream fin = null;
			FileOutputStream fout = null;
			byte[] buffer = new byte[4096]; //Buffer 4K at a time (you can change this).
			int bytesRead;
			try 
			{
				//open the files for input and output
				fin =  new FileInputStream(src);
				fout = new FileOutputStream (dest);
				//while bytesRead indicates a successful read, lets write...
				while ((bytesRead = fin.read(buffer)) >= 0) 
				{
					fout.write(buffer,0,bytesRead);
				}
			} 
			catch (IOException e) 
			{ //Error copying file... 
				IOException wrapper = new IOException("copyFiles: Unable to copy file: " + 
							src.getAbsolutePath() + "to" + dest.getAbsolutePath()+".");
				wrapper.initCause(e);
				wrapper.setStackTrace(e.getStackTrace());
				throw wrapper;
			} 
			finally 
			{ //Ensure that the files are closed (if they were open).
				if (fin != null) { fin.close(); }
				if (fout != null) { fout.close(); }
			}
		}
	}
	
	public void updateResultHtml() throws Exception {
		//String logPath = "C:/Users/pyang/bstone-qa/log/";
		String logFiles[] = (new File(logPath)).list();
		
		//String srcHtml = "C:/Users/pyang/bstone-qa/target/site/Trellis_Automation.html";
		//String destHtml = "C:/Users/pyang/bstone-qa/target/site/temp_Trellis_Automation.html";
		
		StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(
                new FileReader(new File(srcHtml)));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        
        String content = fileData.toString();
		
		for (int i=0;i<logFiles.length;i++){
			String logName = logFiles[i].replaceAll(".log", "");
			int lastDot = logName.lastIndexOf(".");
			logName = logName.substring(0,lastDot) + logName.substring(lastDot+1);
//			System.out.println("logFile=" + logName);
			
			String from = "<a name=\"" + logName + "\"></a>";
			String to = "<a name=\"";
			to = to + logName + "\" href=\"file:///\\";
			to = to + logPath.replace(System.getProperty("file.separator"), "/");
			to = to + "/" + logFiles[i];
			to = to + "\">view log</a>";
//			System.out.println("to=" + to);
			
	        content = content.replaceAll(from, to);
//	        System.out.println("replaced=" + content);
		}
		
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(destHtml));
			out.write(content);
			out.close();
			}
		catch (IOException e)
		{
			System.out.println("Exception ");
		}
		
		copy(destHtml, srcHtml);
	}
	
	public String copy(String orig, String dest)
	{
		String status = "OK";
		try
		{
			File src = new File(orig);
			File dst = new File(dest);
    		InputStream in = new FileInputStream(src);
    		OutputStream out = new FileOutputStream(dst);

			// Transfer bytes from in to out
    		byte[] buf = new byte[1024];
    		int len;
    		while ((len = in.read(buf)) > 0) 
    		{
        		out.write(buf, 0, len);
    		}
    		in.close();
    		out.close();
    	}
  		catch (Exception ex)
  		{
	  		status = ex.getMessage();
	  		ex.printStackTrace();	
  		}
  		return status;  
	}
//	public boolean copyDir(String src, String dest) {
//		boolean success = false; 
//		File[] files = (new File(src)).listFiles(); 
//		
//		File destDir = new File(dest); 
//		success = destDir.mkdir(); 
//		for(int i = 0; i <files.length; i++) { 
//		    File f = files[i]; 
//		    
//		} 
//
//		return success;
//	}

}
