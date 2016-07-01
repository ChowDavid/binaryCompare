package com.david.binaryCompare;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;

public class Decompile {

	public static void folder(File sourceTemp) throws IOException {
		Output.print("Decompile folder "+sourceTemp.getAbsolutePath());
		for (File file:FileUtils.listFiles(sourceTemp, new WildcardFileFilter("*.class"), TrueFileFilter.INSTANCE)){
			new JavaMetaData(file);
		}
	}

}
