package com.david.binaryCompare;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;

public class Decompile {

	public static void folder(File sourceTemp) {
		System.out.println("Decompile folder "+sourceTemp.getAbsolutePath());
		for (File file:FileUtils.listFiles(sourceTemp, new WildcardFileFilter("*.class"), TrueFileFilter.INSTANCE)){
			new JavaMetaData(file);
		}
	}

}
