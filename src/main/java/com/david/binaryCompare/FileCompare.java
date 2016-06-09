package com.david.binaryCompare;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class FileCompare {

	public static CompareResult directory(File source,File target) throws IOException{
		if (!source.exists() || source.isFile()) return null;
		if (!target.exists() || target.isFile()) return null;
		System.out.println("SOURCE_UNPACKED="+source.getAbsolutePath());
		System.out.println("PRODUCTION_UNPACKED="+target.getAbsolutePath());
		CompareResult result1=compare("SOURCE vs PRODUCTION",source,target,true);
		CompareResult result2=compare("PRODUCTION vs SOURCE",target,source,false);
		CompareResult result=new CompareResult(result1.getNotFoundCount()+result2.getNotFoundCount(),result1.getDifferentCount(),result1.getPassCount());
		return result;
	}

	private static CompareResult compare(String message, File source, File target, boolean checkDifferent) throws IOException {
		//System.out.println("compare ["+source.getAbsolutePath()+"]["+target.getAbsolutePath()+"]");
		CompareResult result=new CompareResult();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String sourceName=source.getAbsolutePath();
		String targetName=target.getAbsolutePath();
		
		for (File sourceFile:FileUtils.listFiles(source, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE)){
			File targetFile=getTargetFile(sourceFile,sourceName,targetName);
			if (sourceFile.getName().startsWith("pom.") 
					|| sourceFile.getName().equals("MANIFEST.MF") 
					|| sourceFile.getName().equals(".classpath") 
					|| sourceFile.getName().equals(".project")) continue;
			if (!targetFile.exists()){
				System.out.println("ERROR the file does not exists ["+message+"] "+targetFile.getAbsolutePath().substring(target.getAbsolutePath().length()+1));
				result.notFound();
			} else if (checkDifferent){
				if (!customCompare(sourceFile,targetFile)){
				System.out.println("WARNING source and target are different ["+message+"] ["+sdf.format(new Date(sourceFile.lastModified()))+"] FILE="+sourceFile.getAbsolutePath().substring(source.getAbsolutePath().length()+1));
				result.differentFound();
				} else {
					System.out.println("PASSED ["+message+"]  FILE="+sourceFile.getAbsolutePath().substring(source.getAbsolutePath().length()+1));
					result.passFound();
				}
			}
		}
		return result;
		
	}

	private static boolean customCompare(File sourceFile, File targetFile) throws IOException {
		if (sourceFile.getName().endsWith(".class")){
			JavaMetaData sourceData=new JavaMetaData(sourceFile);
			JavaMetaData targetData=new JavaMetaData(targetFile);
			if (!sourceData.isSuccess() || !targetData.isSuccess()){
				return FileUtils.contentEquals(sourceFile, targetFile);
			} else {
				if (!sourceData.getMd5Hash().equals(targetData.getMd5Hash()) || !sourceData.getVersion().equals(targetData.getVersion())){
					System.out.println("WARNING MetalData not match ONE->["+sourceData.getMd5Hash()+"]["+sourceData.getVersion()+"]");
					System.out.println("WARNING MetalData not match TWO->["+targetData.getMd5Hash()+"]["+targetData.getVersion()+"]");
					return false;
				} 
				return true;
			}
		} else {
			List<String> sLines=FileUtils.readLines(sourceFile);
			List<String> tLines=FileUtils.readLines(targetFile);
			if (sLines.size()!=tLines.size()) return false;
			for (int i=0;i<sLines.size();i++){
				if (!sLines.get(i).trim().equalsIgnoreCase(tLines.get(i).trim())){
					if (sourceFile.getName().endsWith("web.xml")){
						System.out.println("customCompare 1="+sLines.get(i));
						System.out.println("customCompare 2="+tLines.get(i));
					}
					return false;
				}
			}
			return true;
		}
	}
	


	private static File getTargetFile(File sourceFile, String sourceName, String targetName) {
		//System.out.println("getTargetFile ["+sourceFile.getAbsolutePath()+"]["+sourceName+"]["+targetName+"]");
		String fileName=sourceFile.getAbsolutePath();
		String newName=targetName+fileName.substring(sourceName.length());
		//System.out.println("getTargetFile sourceFile "+sourceFile.getAbsolutePath());
		//System.out.println("getTargetFile sourceName "+sourceName);
		//System.out.println("getTargetFile targetName "+targetName);
		//System.out.println("getTargetFile final_Name "+newName);
		return new File(newName);
	}
}
