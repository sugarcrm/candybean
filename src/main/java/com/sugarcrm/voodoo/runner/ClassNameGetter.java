package com.sugarcrm.voodoo.runner;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * ClassNameGetter reads a class file and returns the class name.
 * The reading assumes a standard class format. 
 * 
 */
public class ClassNameGetter {
	@SuppressWarnings("unused")
	public static String get(String classFile) {
		final Map<Integer, Integer> offsetTable = new HashMap<Integer, Integer>();
		final Map<Integer, String> utfTable = new HashMap<Integer, String>();
		String className = "";

		try {
			byte[] classBytes = read(classFile);

			DataInputStream data = new DataInputStream(
					new ByteArrayInputStream(classBytes));
			// Standard ClassFile format
			int magicNumber = data.readInt(); // read 4 bytes
			int minorVersion = data.readUnsignedShort(); // 2 bytes
			int majorVersion = data.readUnsignedShort();
			// constantPoolCount is the number of items in constant pool plus 1
			int constantPoolCount = data.readUnsignedShort(); 

			readConstantPool(data, constantPoolCount, offsetTable, utfTable);

			int accessFlags = data.readUnsignedShort();
			int thisClass = data.readUnsignedShort();
			int superClass = data.readUnsignedShort();
			int offset = offsetTable.get(thisClass);
			// The class name internal form is "/" separated. Replace with "."
			className = utfTable.get(offset).replace('/', '.');

		} catch (Exception e) {
			e.printStackTrace();
		}

		return className;
	}

	/**
	 * Name: readConstantPool 
	 * 
	 * @param 
	 * data : DataInputStream 
	 * count : int 
	 * offsetTable :  Map<Integer, Integer> 
	 * utfTable :  Map<Integer, Integer> 
	 * @return void
	 */
	private static void readConstantPool(DataInputStream data, int count,
			Map<Integer, Integer> offsetTable, Map<Integer, String> utfTable)
			throws Exception {
		for (int i = 1; i < count; i++) { // the 0th item is invalid
			int tag = data.read(); // read 2 bytes
			// In general the ClassFile has multiple instances of the same tag
			switch (tag) {
			case 1: // modified UTF-8
				int length = data.readUnsignedShort();
				char[] chars = new char[length];
				for (int j = 0; j < length; j++) {
					chars[j] = (char) data.read(); // read a byte
				}
				String name = new String(chars);
				utfTable.put(i, name);
				break;
			case 3: // integer
			case 4: // float
			case 9: // field_ref
			case 10: // method_ref
			case 11: // interface_method_ref
			case 12: // name_and_type_desc
				data.readInt(); // read 4 bytes;
				break;
			case 5: // long
			case 6: // double
				data.readLong(); // read 8 bytes
				i++; // long and double each occupies 2 slots. Skip the 2nd
						// unused slot
				break;
			case 7: // class_ref
				int offset = data.readShort();
				offsetTable.put(i, offset);
				break;
			case 8: // string_ref
				data.readShort(); // read 2 bytes
				break;
			default:
				throw new Exception("Invalid tag: " + tag);
			}
		}
	}

	/**
	 * Name: read
	 * Reads the input class file into an array of bytes.
	 * 
	 * @param 
	 * classFile : String 
	 * @return byte[] 
	 */
	private static byte[] read(String classFile) throws IOException {
		byte[] bytes = null;
		BufferedInputStream bis = null;
		try {
			//System.out.println("ClassNameGetter(): classFile = " + classFile);
			bis = new BufferedInputStream(new FileInputStream(new File(
					classFile).getCanonicalFile()));
			bytes = new byte[bis.available()]; // number of bytes in buffer
			bis.read(bytes); // read bytes in buffer without blocking
		} finally {
			bis.close();
		}

		return bytes;
	}
}