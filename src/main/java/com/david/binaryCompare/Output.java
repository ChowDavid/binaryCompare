package com.david.binaryCompare;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class Output {
	
	private static File file;
	
	public static void setFile(File file1){
		file=file1;
	}
	
	public static void print(String s) throws IOException{
		System.out.println(s);
		if (file==null){
			System.out.println("Sorry Output.print file cannot be null");
			System.exit(-1);
		} else {
			FileUtils.writeStringToFile(file, s+String.format("%n"),true);
		}
	}
	
}
