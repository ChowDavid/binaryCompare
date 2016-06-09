package com.david.binaryCompare;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

public class JavaMetaData {

	private int majorVersion;
	private int minorVersion;
	private String md5Hash;
	private File classFile;
	private boolean success;
	private static final Map<Integer, String> majorVersionToJdkVersion;

	static {
		final Map<Integer, String> tempMajorVersionToJdkVersion = new HashMap<Integer, String>();
		tempMajorVersionToJdkVersion.put(45, "JDK 1.1");
		tempMajorVersionToJdkVersion.put(46, "JDK 1.2");
		tempMajorVersionToJdkVersion.put(47, "JDK 1.3");
		tempMajorVersionToJdkVersion.put(48, "JDK 1.4");
		tempMajorVersionToJdkVersion.put(49, "J2SE 5");
		tempMajorVersionToJdkVersion.put(50, "Java SE 6");
		tempMajorVersionToJdkVersion.put(51, "Java SE 7");
		tempMajorVersionToJdkVersion.put(52, "Java SE 8");
		majorVersionToJdkVersion = Collections.unmodifiableMap(tempMajorVersionToJdkVersion);
	}

	public JavaMetaData(final File classFile) {
		this.classFile = classFile;
		success = false;
		process();

	}

	public JavaMetaData(String classFileName) {
		this.classFile = new File(classFileName);
		success = false;
		process();
	}

	private void process() {
		if (!classFile.getName().endsWith(".class")) {
			return;
		}
		try {
			final DataInputStream input = new DataInputStream(new FileInputStream(classFile));
			final String firstFourBytes = Integer.toHexString(input.readUnsignedShort())
					+ Integer.toHexString(input.readUnsignedShort());
			if (firstFourBytes.equalsIgnoreCase("cafebabe")) {
				minorVersion = input.readUnsignedShort();
				majorVersion = input.readUnsignedShort();
				md5Hash = DigestUtils.md2Hex(IOUtils.toByteArray(new FileInputStream(classFile)));
				success=true;
			}
			input.close();
		} catch (FileNotFoundException e) {
			System.out.println("Required file not found " + classFile.getAbsolutePath());
		} catch (IOException e) {
			System.out.println("Required file cannot read " + classFile.getAbsolutePath());
		}
	}



	public String getVersion() {
		if (success) {
			return majorVersionToJdkVersion.get(majorVersion);
		}
		return "";
	}

	public int getMajorVersion() {
		return majorVersion;
	}

	public int getMinorVersion() {
		return minorVersion;
	}

	public String getMd5Hash() {
		return md5Hash;
	}

	public boolean isSuccess() {
		return success;
	}


	
	

}
