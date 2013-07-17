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
package com.sugarcrm.candybean.runner;

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
