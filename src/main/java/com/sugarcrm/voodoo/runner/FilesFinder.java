package com.sugarcrm.voodoo.runner;

import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/**
 * FilesFinder walks the input directory looking for files of a given extension. 
 * It extends the java built-in SimpleFileVisitor class
 * 
 */
public class FilesFinder extends SimpleFileVisitor<Path> {
	private final String pattern;
	private List<File> result = new ArrayList<File>();
	
	public FilesFinder(String pattern) {
		this.pattern = pattern;
	}
	
	@Override
	public FileVisitResult visitFile(Path file,
			BasicFileAttributes attrs) throws IOException {
		if (file.toString().endsWith("." + this.pattern)) {
			result.add(new File(file.toString()));
		}
		return FileVisitResult.CONTINUE;
	}
	
	public List<File> getResult() {
		return result;
	}
}
