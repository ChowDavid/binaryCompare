package com.david.binaryCompare;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import com.strobel.decompiler.DecompilerDriver;

/**
 * Hello world!
 *
 */
public class AppMain {
	public static void main(String[] args) throws IOException {
		System.out.println("Binary Compare 1.0.0-SNAPSHOT Start It will read two java package and compare and list the different");
		if (args.length < 3) {
			System.out.println("Please provide header message, source and target package in either jar,war or ear file or folder");
			System.exit(1);
		}
		String headerMessage= args[0];
		File sourceFile = new File(args[1]);
		File targetFile = new File(args[2]);
		if (!sourceFile.exists() || !targetFile.exists()) {
			System.out.println("Either source or target file not exists");
			System.out.println("SOURCE=" + sourceFile.getAbsolutePath());
			System.out.println("PRODUCTION=" + targetFile.getAbsolutePath());
			System.exit(2);
		} 
		File sourceTemp = new File("SOURCE_"+headerMessage+"_" + System.currentTimeMillis());
		File targetTemp = new File("PRODUCTION_" +headerMessage+"_"+ System.currentTimeMillis());
		File outputFile = new File("RESULT_"+headerMessage+"_"+ System.currentTimeMillis());
		Output.setFile(outputFile);
		Output.print("SOURCE=" + sourceFile.getAbsolutePath());
		Output.print("PRODUCTION=" + targetFile.getAbsolutePath());
		

		FileBreaker.unPackage(sourceFile, sourceTemp);
		FileBreaker.unPackage(targetFile, targetTemp);
		CompareResult sourceResult = new CompareResult();
		CompareResult targetResult = new CompareResult();
		FileCompare.directory(sourceTemp, targetTemp, sourceResult, targetResult);
		Output.print("======================================================================");
		Output.print("Binary compare result for "+headerMessage);
		if (sourceResult.isPass() && targetResult.isPass()) {
			Output.print("Compare success complete");
		} else {
			Output.print("Compare with error");
		}
		Output.print("From SOURCE");
		printOutput(sourceResult,false);
		Output.print("From PRODUCTION");
		printOutput(targetResult,false);
		Output.print("======================================================================");
		Output.print("From SOURCE detail");
		printOutput(sourceResult,true);
		Output.print("From PRODUCTION detail");
		printOutput(targetResult,true);
		
		

	}

	private static void printOutput(CompareResult sourceResult,boolean detail) throws IOException {
		if (!detail){
			int classDifferentCount=0;
			int nonClassDifferentCount=0;
			int classNotFoundCount=0;
			int nonClassNotFoundCount=0;
			int classPassCount=0;
			int nonClassPassCount=0;
			for (String s:sourceResult.getDifferentLists()){
				if (s.endsWith(".class")){
					classDifferentCount++;
				} else {
					nonClassDifferentCount++;
				}
			}
			for (String s:sourceResult.getNotfoundLists()){
				if (s.endsWith(".class")){
					classNotFoundCount++;
				} else {
					nonClassNotFoundCount++;
				}
			}
			for (String s:sourceResult.getPassLists()){
				if (s.endsWith(".class")){
					classPassCount++;
				} else {
					nonClassPassCount++;
				}
			}
			
			
			Output.print("Main module Same file found          ="+(classPassCount+nonClassPassCount));
			Output.print("Main module Same class file found      ="+classPassCount);
			Output.print("Main module Same non-class file found  ="+ nonClassPassCount);
			Output.print("Main module Different file found     ="+(classDifferentCount+nonClassDifferentCount));
			Output.print("Main module Different class            ="+classDifferentCount);
			Output.print("Main module Different non-class        ="+nonClassDifferentCount);
			Output.print("Main module file not found           ="+(classNotFoundCount+nonClassNotFoundCount));
			Output.print("Main module class not found            ="+classNotFoundCount);
			Output.print("Main module non-class not found        ="+nonClassNotFoundCount);
			Output.print("Sub module same package found           ="+sourceResult.getPackagePassCount());
			Output.print("Sub module different package found      ="+sourceResult.getPackageDifferentCount());
			Output.print("Sub module package not found            ="+sourceResult.getPackageNotFoundCount());
			Output.print("JDK found "+sourceResult.getJdks());
			Output.print("Different Lists "+sourceResult.getDifferentLists().size());
		
		} else {
			for (String s:sourceResult.getDifferentLists()){
				if (s.endsWith(".class")){
					Output.print("Class different:"+s);
				}
			}
			for (String s:sourceResult.getDifferentLists()){
				if (!s.endsWith(".class")){
					Output.print("Non Class different:"+s);
				}
			}
			Output.print("Not found Lists "+sourceResult.getNotfoundLists().size());
			for (String s:sourceResult.getNotfoundLists()){
				if (s.endsWith(".class")){
					Output.print("Class not found:"+s);
				}
			}
			for (String s:sourceResult.getNotfoundLists()){
				if (!s.endsWith(".class")){
					Output.print("Non Class not found:"+s);
				}
			}
			Output.print("Decompile Error List " +sourceResult.getUnSuccessDecompileClassLists().size());
			for (String s:sourceResult.getUnSuccessDecompileClassLists()){
				Output.print("Decompile Error List:"+s);
			}
			
			if (sourceResult.getProblemJarSet().size() > 0) {
				Output.print("Java Packages does not found");
				for (String jarName : sourceResult.getProblemJarSet()) {
					Output.print("Package does not found " + jarName);
				}
			}
		}
		Output.print("======================================================================");
		
	}
}
