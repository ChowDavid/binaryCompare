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
 * The output of the master should include Number of Pass NonJavaPackage +
 * number of Package Number of Different any class or file are different Number
 * of NotFound 1 against 2 .class + number of Package
 * 
 * @author david
 *
 */
public class FileCompare {

	private static final Set<String> BY_PASS_FILES = new HashSet<String>();
	private static JavaMetaData sourceJavaMetaData;
	private static JavaMetaData targetJavaMetaData;

	static {
		BY_PASS_FILES.add("pom.xml");
		BY_PASS_FILES.add("pom.properties");
		BY_PASS_FILES.add("MANIFEST.MF");
		BY_PASS_FILES.add(".classpath");
		BY_PASS_FILES.add(".project");
		BY_PASS_FILES.add(".setting");
	}

	public static void directory(File source, File target, CompareResult sourceResult, CompareResult targetResult)
			throws IOException {
		if (!source.exists() || source.isFile()) {
			return;
		}
		if (!target.exists() || target.isFile()) {
			return;
		}
		Output.print("SOURCE_UNPACKED=" + source.getAbsolutePath());
		Output.print("PRODUCTION_UNPACKED=" + target.getAbsolutePath());
		sourceResult.set(compare("SOURCE vs PRODUCTION", source, target, true));
		targetResult.set(compare("PRODUCTION vs SOURCE", target, source, true));

	}

	private static CompareResult compare(String message, File source, File target, boolean checkDifferent)
			throws IOException {
		// System.out.println("compare
		// ["+source.getAbsolutePath()+"]["+target.getAbsolutePath()+"]");
		Set<String> problemJarSet = new HashSet<String>();
		Set<String> passJarSet = new HashSet<String>();
		CompareResult result = new CompareResult();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String sourceName = source.getAbsolutePath();
		String targetName = target.getAbsolutePath();
		FileWareHouse sourceStore = new FileWareHouse(source);
		FileWareHouse targetStore = new FileWareHouse(target);
		for (String sourceAliasName : sourceStore.getAllNames()) {
			File sourceFile = sourceStore.getByName(sourceAliasName) == null ? null
					: sourceStore.getByName(sourceAliasName).get(0);
			File targetFile = targetStore.getByName(sourceAliasName) == null ? null
					: targetStore.getByName(sourceAliasName).get(0);
			if (sourceStore.getByName(sourceAliasName) != null && targetStore.getByName(sourceAliasName) != null
					&& (sourceStore.getByName(sourceAliasName).size() > 1
							|| targetStore.getByName(sourceAliasName).size() > 1)) {
				File previousS1=null;
				for (File s1 : sourceStore.getByName(sourceAliasName)) {
					if (previousS1==null){
						previousS1=s1;
					} else {
						if (!FileUtils.contentEquals(previousS1, s1)){
							Output.print("ERROR content not same as first one ["+message+"] "+getFileName(source, previousS1)+"<-->"+getFileName(source, s1));
						} 
					}
				}
				/*
				File previoust1=null;
				for (File t1 : targetStore.getByName(sourceAliasName)) {
					if (previoust1==null){
						previoust1=t1;
					} else {
						if (!FileUtils.contentEquals(previoust1, t1)){
							System.out.println("ERROR content not same as first one ["+message+"] from TWO "+getFileName(target, previoust1)+"<-->"+getFileName(target, t1));
						} 
					}
				}*/
			}
			if (isByPassFile(sourceFile.getName()))
				continue;
			String fileName = getFileName(source, sourceFile);
			if (targetFile == null) {
				String packageName = getPackageName(fileName);
				if (!isJavaPackage(fileName)) {
					result.nonPackageNotFound();
					result.getNotfoundLists().add(fileName);
				} else if (!problemJarSet.contains(packageName)) {
					result.packageNotFound();
					problemJarSet.add(packageName);
				}
				//System.out.println("ERROR the file does not exists [" + message + "] " + fileName);
			} else if (checkDifferent) {
				
				if (!customCompare(sourceFile.getAbsolutePath(), targetFile.getAbsolutePath())) {
					if (isJavaPackage(fileName)) {
						result.packageDifferentFound();
					} else {
						result.nonPackageDifferentFound();
						result.getDifferentLists().add(fileName);
					}
					if (checkDecompileError(sourceFile.getAbsolutePath())) {
						result.getUnSuccessDecompileClassLists().add(fileName);
						//System.out.println("WARNING source file decompile error [" + message + "] ["+ sdf.format(new Date(sourceFile.lastModified())) + "] FILE=" + fileName);
					} 
					//System.out.println("WARNING source and target are different [" + message + "] ["+sdf.format(new Date(sourceFile.lastModified())) + "] FILE=" + fileName);
					

				} else {
					if (!isJavaPackage(fileName)) {
						result.nonPackagePassCountFound();
						result.getPassLists().add(fileName);
					} else if (!passJarSet.contains(getPackageName(fileName))) {
						//System.out.println("FOUND [" + message + "]  PACKAGE=" + getPackageName(fileName));
						passJarSet.add(getPackageName(fileName));
						result.packagePassCountFound();
					}
					//System.out.println("PASSED [" + message + "]  FILE=" + fileName);
				}
				//Check JDK version
				if (sourceJavaMetaData!=null){
					result.getJdks().add(sourceJavaMetaData.getVersion());
				}
			}
		}
		result.setPassJarSet(passJarSet);
		result.setProblemJarSet(problemJarSet);
		return result;

	}

	private static String getFileName(File target, File targetFile) {
		return targetFile.getAbsolutePath().substring(target.getAbsolutePath().length() + 1);
	}

	private static boolean checkDecompileError(String sourceFileName) throws IOException {
		File sourceFile = new File(sourceFileName);
		if (sourceFile.getName().endsWith(".class")) {
			File javaFile=new File(sourceFile.getAbsolutePath().replace(".class", ".java"));
			if (javaFile.exists()){
				List<String> sLines = FileUtils.readLines(javaFile);
				for (String line : sLines) {
					if (line.trim().equalsIgnoreCase(
							"throw new IllegalStateException(\"An error occurred while decompiling this method.\");")) {
						return true;
					}
				}
			}
		}
		return false;

	}

	private static boolean customCompare(String sourceFileName, String targetFileName) throws IOException {
		File sourceFile = new File(sourceFileName);
		File targetFile = new File(targetFileName);
		if (sourceFile.getName().endsWith(".class")) {
			// check binary compare first on .class otherwise check .java
			if (FileUtils.contentEquals(sourceFile, targetFile)) {
				return true;
			} else {
				sourceJavaMetaData=null;
				targetJavaMetaData=null;
				sourceJavaMetaData = new JavaMetaData(sourceFile);
				targetJavaMetaData = new JavaMetaData(targetFile);
				File sourceJava = new File(sourceFile.getAbsolutePath().replace(".class", ".java"));
				File targetJava = new File(targetFile.getAbsolutePath().replace(".class", ".java"));
				return FileUtils.contentEquals(sourceJava, targetJava);
			}
		} else {
			List<String> sLines = FileUtils.readLines(sourceFile);
			List<String> tLines = FileUtils.readLines(targetFile);
			if (sLines.size() != tLines.size()) {
				return false;
			}
			for (int i = 0; i < sLines.size(); i++) {
				if (!sLines.get(i).trim().equalsIgnoreCase(tLines.get(i).trim())) {
					if (sourceFile.getName().endsWith("web.xml")) {
						Output.print("customCompare 1=" + sLines.get(i));
						Output.print("customCompare 2=" + tLines.get(i));
					}
					return false;
				}
			}
			return true;
		}
	}

	private static boolean binaryCompareEquals(File sourceFile, File targetFile) {
		boolean result = false;
		try {
			InputStream is1 = new FileInputStream(sourceFile);
			InputStream is2 = new FileInputStream(targetFile);
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
		String fileName = sourceFile.getAbsolutePath();
		String newName = targetName + fileName.substring(sourceName.length());
		return new File(newName);
	}

	/**
	 * It will check the input and output if there are belong to jar file or war
	 * file or ear file in the after the first \ or /
	 * 
	 * @param fileName
	 * @return null if that is not belong to package package Example :
	 *         ICheckIn21Web.war\WEB-INF\lib\axiom-api-1.2.7.jar\org\apache\
	 *         axiom\soap\SOAP11Constants.class output : axiom-api-1.2.7.jar
	 * 
	 */
	private static String getPackageName(String fileName) {

		String[] parts;
		if (fileName.indexOf('/') > -1) {
			parts = fileName.split("/");
		} else {
			parts = fileName.split("\\\\");
		}
		for (int i = 1; i < parts.length; i++) {
			if (parts[i].endsWith("jar") || parts[i].endsWith("war") || parts[i].endsWith("ear")) {
				return parts[i];
			}
		}
		return null;

	}

	private static boolean isJavaPackage(String fileName) {
		return getPackageName(fileName) != null;
	}

	
	private static boolean isByPassFile(String name) {
		return name.endsWith(".java") || BY_PASS_FILES.contains(name);
	}
}
