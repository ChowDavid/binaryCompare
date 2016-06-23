package com.david.binaryCompare;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;


/**
 * The output of the master should include
 * Number of Pass NonJavaPackage + number of Package
 * Number of Different any class or file are different
 * Number of NotFound 1 against 2 .class + number of Package
 * 
 * @author david
 *
 */
public class FileCompare {

	public static void directory(File source,File target, CompareResult sourceResult, CompareResult targetResult) throws IOException{
		if (!source.exists() || source.isFile()) {
			return ;
		}
		if (!target.exists() || target.isFile()) {
			return ;
		}
		System.out.println("SOURCE_UNPACKED="+source.getAbsolutePath());
		System.out.println("PRODUCTION_UNPACKED="+target.getAbsolutePath());
		sourceResult.set(compare("SOURCE vs PRODUCTION",source,target,true));
		targetResult.set(compare("PRODUCTION vs SOURCE",target,source,true));
		System.out.println("SOURCE="+sourceResult);
		System.out.println("PRODUCTION="+targetResult);
		//CompareResult result=new CompareResult(result1.getNotFoundCount()+result2.getNotFoundCount(),result1.getDifferentCount(),result1.getPassCount(),result1.getNonPackagePassCount());

	}

	private static CompareResult compare(String message, File source, File target, boolean checkDifferent) throws IOException {
		//System.out.println("compare ["+source.getAbsolutePath()+"]["+target.getAbsolutePath()+"]");
		Set<String> problemJarSet=new HashSet<String>();
		Set<String> passJarSet=new HashSet<String>();
		CompareResult result=new CompareResult();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String sourceName=source.getAbsolutePath();
		String targetName=target.getAbsolutePath();
		for (File sourceFile:FileUtils.listFiles(source, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE)){
			File targetFile=getTargetFile(sourceFile,sourceName,targetName);
			String fileName=targetFile.getAbsolutePath().substring(target.getAbsolutePath().length()+1);

			if (sourceFile.getName().startsWith("pom.") 
					|| sourceFile.getName().equals("MANIFEST.MF") 
					|| sourceFile.getName().equals(".classpath") 
					|| sourceFile.getName().equals(".project")
					|| sourceFile.getName().endsWith(".java")) continue;
			
			if (!targetFile.exists()){
				String packageName=getPackageName(fileName);
				if (!isJavaPackage(fileName)){
					result.nonPackageNotFound();
				} else if (!problemJarSet.contains(packageName)){
					result.packageNotFound();
					problemJarSet.add(packageName);
				}
				System.out.println("ERROR the file does not exists ["+message+"] "+fileName);
				
			} else if (checkDifferent){
				if (!customCompare(sourceFile.getAbsolutePath(),targetFile.getAbsolutePath())){
					if (isJavaPackage(fileName)){
						result.packageDifferentFound();
					} else {
						result.nonPackageDifferentFound();
					}
					System.out.println("WARNING source and target are different ["+message+"] ["+sdf.format(new Date(sourceFile.lastModified()))+"] FILE="+fileName);
					
				} else {
					if (!isJavaPackage(fileName)){
						result.nonPackagePassCountFound();
					} else if (!passJarSet.contains(getPackageName(fileName))){
						System.out.println("FOUND ["+message+"]  PACKAGE="+getPackageName(fileName));
						passJarSet.add(getPackageName(fileName));
						result.packagePassCountFound();
					}
					System.out.println("PASSED ["+message+"]  FILE="+fileName);
					
				}
			}
		}
		result.setPassJarSet(passJarSet);
		result.setProblemJarSet(problemJarSet);
		return result;
		
	}

	private static boolean customCompare(String sourceFileName, String targetFileName) throws IOException {
		File sourceFile=new File(sourceFileName);
		File targetFile=new File(targetFileName);
		if (sourceFile.getName().endsWith(".class")){
			//check binary compare first on .class otherwise check .java
			if (FileUtils.contentEquals(sourceFile,targetFile)){
				return true;
			} else {
				File sourceJava=new File(sourceFile.getAbsolutePath().replace(".class", ".java"));
				File targetJava=new File(targetFile.getAbsolutePath().replace(".class", ".java"));
				return FileUtils.contentEquals(sourceJava, targetJava);
			}
		} else {
			List<String> sLines=FileUtils.readLines(sourceFile);
			List<String> tLines=FileUtils.readLines(targetFile);
			if (sLines.size()!=tLines.size()) {
				return false;
			}
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
	


	private static boolean binaryCompareEquals(File sourceFile, File targetFile)  {
		boolean result=false;
		try {
			InputStream is1=new FileInputStream(sourceFile);
			InputStream is2=new FileInputStream(targetFile);
			result = IOUtils.contentEquals(is1, is2);
			is1.close();
			is2.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
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
	
	/**
	 * It will check the input and output if there are belong to jar file or war file or ear file in the after the first \ or /
	 * @param fileName
	 * @return null if that is not belong to package package
	 * Example : ICheckIn21Web.war\WEB-INF\lib\axiom-api-1.2.7.jar\org\apache\axiom\soap\SOAP11Constants.class
	 * output : axiom-api-1.2.7.jar
	 * 
	 */
	private  static String getPackageName(String fileName){

		String[] parts;
		if (fileName.indexOf('/')>-1){
			parts=fileName.split("/");
		} else {
			 parts=fileName.split("\\\\");
		}
		for (int i=1; i<parts.length; i++){
			if (parts[i].endsWith("jar") || parts[i].endsWith("war") || parts[i].endsWith("ear")){
				return parts[i];
			}
		}
		return null;
		
	}
	
	private static boolean isJavaPackage(String fileName){
		return getPackageName(fileName)!=null;
	}
	
	
	public static void main(String[] args){
		String filePath=args[0];
		System.out.println(filePath);
		System.out.println(getPackageName(filePath));
		System.out.println(isJavaPackage(filePath));
		
	}
}
