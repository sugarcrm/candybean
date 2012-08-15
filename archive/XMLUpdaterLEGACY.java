package com.voodoo2;


public class XMLUpdaterLEGACY {
	
//	public static void main(String[] args) throws Exception {
//	
//		String logPath = "C:/Users/pyang/bstone-qa/log/";
//		String logFiles[] = (new File(logPath)).list();
//		
//		String srcHtml = "C:/Users/pyang/bstone-qa/target/site/Trellis_Automation.html";
//		String destHtml = "C:/Users/pyang/bstone-qa/target/site/temp_Trellis_Automation.html";
//		
//		StringBuffer fileData = new StringBuffer(1000);
//        BufferedReader reader = new BufferedReader(
//                new FileReader(new File(srcHtml)));
//        char[] buf = new char[1024];
//        int numRead=0;
//        while((numRead=reader.read(buf)) != -1){
//            String readData = String.valueOf(buf, 0, numRead);
//            fileData.append(readData);
//            buf = new char[1024];
//        }
//        reader.close();
//        
//        String content = fileData.toString();
//		
//		for (int i=0;i<logFiles.length;i++){
//			String logName = logFiles[i].replaceAll(".log", "");
//			int lastDot = logName.lastIndexOf(".");
//			logName = logName.substring(0,lastDot) + logName.substring(lastDot+1);
//			System.out.println("logFile=" + logName);
//			
//			String from = "<a name=\"" + logName + "\"></a>";
//			String to = "<a name=\"";
//			to = to + logName + "\" href=\"";
//			to = to + logPath;
//			to = to + logFiles[i];
//			to = to + "\">view log</a>";
//			System.out.println("to=" + to);
//			
//	        content = content.replaceAll(from, to);
//	        System.out.println("replaced=" + content);
//		}		
//	}
}
