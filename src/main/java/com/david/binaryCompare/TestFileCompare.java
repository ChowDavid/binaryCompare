package com.david.binaryCompare;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class TestFileCompare {
	
	public static void main(String[] args) throws IOException{
		File source=new File("/Users/david/Documents/workspaces/workspace_source_health_check/binaryCompare/SOURCE_1466182531513/ICheckIn21.ear/ICheckIn21EJB.jar/com/cathaypacific/olci/dao/OLCIAdminDAO.class");
		File target = new File("/Users/david/Documents/workspaces/workspace_source_health_check/binaryCompare/PRODUCTION_1466182531513/ICheckIn21.ear/ICheckIn21EJB.jar/com/cathaypacific/olci/dao/OLCIAdminDAO.class");
		InputStream is1=new FileInputStream(source);
		InputStream is2=new FileInputStream(target);
		System.out.println(source.length()+" "+target.length());
		System.out.println(FileUtils.contentEquals(source, target));
		is1.close();
		is2.close();
		
	}

}
