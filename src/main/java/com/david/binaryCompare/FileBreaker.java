package com.david.binaryCompare;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;



public class FileBreaker {
	
	private FileBreaker(){	
	}
	
	public static void unPackage(File tarFile, File directory) throws IOException {
		System.out.println("Un-Pack ["+tarFile.getAbsolutePath()+"]->["+directory.getAbsolutePath()+"]");
		if (directory.exists()){
			directory.delete();
		}
		if (tarFile.isDirectory()){
			File oneLevelDeep=new File(directory,tarFile.getName());
			FileUtils.copyDirectory(tarFile, oneLevelDeep);
		} else {
			if (!isJavaPackage(tarFile)){
				if (tarFile.isFile()){
					FileUtils.copyFile(tarFile, new File(directory,tarFile.getName()));
				} 
				if (tarFile.isDirectory()){
					FileUtils.copyDirectory(tarFile, new File(directory,tarFile.getName()));
				}
				
			} else {
				File newFolder=new File(directory,tarFile.getName());
				System.out.println(newFolder.getAbsolutePath());
				unJar(tarFile,newFolder);
				
			}
		}
		boolean found=true;
		while(found){
			found=searchInner(directory);
		}
		
	}
	
	private  static boolean searchInner(File directory) throws IOException{
		boolean found=false;
		if (!directory.exists() || directory.isFile()) return found;
		
		for (File file:FileUtils.listFiles(directory, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE)){
			if (isJavaPackage(file)){
				found=true;
				File newFile=new File(file.getAbsolutePath()+".tmp");
				newFile.mkdir();
				unJar(file,newFile);
				file.delete();
				newFile.renameTo(file);
				newFile.delete();
			}
		}
		return found;
	}

	public static List<String> unJar(File tarFile, File directory) throws IOException {
	    List<String> result = new ArrayList<String>();
	    InputStream inputStream = new FileInputStream(tarFile);
	    JarArchiveInputStream in = new JarArchiveInputStream(inputStream);
	    JarArchiveEntry entry = in.getNextJarEntry();
	    while (entry != null) {
	        if (entry.isDirectory()) {
	            entry = in.getNextJarEntry();
	            continue;
	        }
	        File curfile = new File(directory, entry.getName()); 
	        //System.out.println("getLastModifiedDate="+entry.getLastModifiedDate());
	        //System.out.println("getTime="+new Date(entry.getTime()));
	  
	        
	        curfile.setLastModified(entry.getLastModifiedDate().getTime());
	        File parent = curfile.getParentFile();
	        if (!parent.exists()) {
	            parent.mkdirs();
	        }
	        OutputStream out = new FileOutputStream(curfile);
	        IOUtils.copy(in, out);
	        
	        out.close();
	        result.add(entry.getName());
	        entry = in.getNextJarEntry();
	    }
	    in.close();
	    return result;
	}
	
	private static boolean isJavaPackage(File tempFile){
		if (tempFile.isFile()){
			String name=tempFile.getName();
			if (name.endsWith("jar") || name.endsWith("ear") || name.endsWith("war")){
				return true;
			}
		}
		return false;
		
	}

}
