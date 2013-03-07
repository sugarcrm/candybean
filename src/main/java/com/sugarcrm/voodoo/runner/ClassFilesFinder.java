package com.sugarcrm.voodoo.runner;

import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/**
 * Walks the input directory looking for .class files.
 * 
 * @author Soon Han 
 */
 
public class ClassFilesFinder extends SimpleFileVisitor<Path> {
	private List<File> files = new ArrayList<File>();
	
	/**
	 * A required override method.
	 * @param file the {@link Path} to directory to visit 
	 * @return {@link FileVisitResult} 
	 */
	@Override
	public FileVisitResult visitFile(Path file,
			BasicFileAttributes attrs) throws IOException {
		if (file.toString().endsWith(".class")) {
			files.add(new File(file.toString()));
		}
		return FileVisitResult.CONTINUE;
	}
	
	/**
	 * @return a list of the .class files found 
	 */
	public List<File> getFiles() {
		return files;
	}
}
