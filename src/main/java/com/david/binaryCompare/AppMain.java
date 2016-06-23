package com.david.binaryCompare;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.IOUtils;

/**
 * Hello world!
 *
 */
public class AppMain {
	public static void main(String[] args) throws IOException {
		System.out.println("Binary Compare Start It will read two java package and compare and list the different");
		if (args.length < 2) {
			System.out.println("Please provide source and target package in either jar,war or ear file or folder");
			System.exit(1);
		}
		File sourceFile = new File(args[0]);
		File targetFile = new File(args[1]);
		if (!sourceFile.exists() || !targetFile.exists()) {
			System.out.println("Either source or target file not exists");
			System.out.println("SOURCE=" + sourceFile.getAbsolutePath());
			System.out.println("PRODUCTION=" + targetFile.getAbsolutePath());
			System.exit(2);
		} else {
			System.out.println("SOURCE=" + sourceFile.getAbsolutePath());
			System.out.println("PRODUCTION=" + targetFile.getAbsolutePath());
		}
		File sourceTemp = new File("SOURCE_" + System.currentTimeMillis());
		File targetTemp = new File("PRODUCTION_" + System.currentTimeMillis());

		FileBreaker.unPackage(sourceFile, sourceTemp);
		FileBreaker.unPackage(targetFile, targetTemp);
		Decompile.folder(sourceTemp);
		Decompile.folder(targetTemp);
		CompareResult sourceResult = new CompareResult();
		CompareResult targetResult = new CompareResult();
		FileCompare.directory(sourceTemp, targetTemp, sourceResult, targetResult);
		System.out.println("======================================================================");
		if (sourceResult.isPass() && targetResult.isPass()) {
			System.out.println("Compare success complete");
			System.out.println("======================================================================");
			System.exit(0);
		} else {
			System.out.println("Compare with error");
			System.out.println("From SOURCE");

			System.out.printf("Main module Same file found          =%s\n", sourceResult.getNonPackagePassCount());
			System.out.printf("Main module Different file found     =%s\n", sourceResult.getNonPackageDifferentCount());
			System.out.printf("Main module file not found           =%s\n", sourceResult.getNonPackageNotFoundCount());
			System.out.printf("Sub module Same file found           =%s\n", sourceResult.getPackagePassCount());
			System.out.printf("Sub module Different file found      =%s\n", sourceResult.getPackageDifferentCount());
			System.out.printf("Sub module file not found            =%s\n", sourceResult.getPackageNotFoundCount());
			if (sourceResult.getProblemJarSet().size() > 0) {
				System.out.println("Java Package found different SOUNCE against PRODUCTION");
				for (String jarName : sourceResult.getProblemJarSet()) {
					System.out.println("Package does not found from SOURCE  " + jarName);
				}
			}
			System.out.println("======================================================================");
			System.out.println("From PRODUCTION");
			System.out.printf("Main module Same file found          =%s\n", targetResult.getNonPackagePassCount());
			System.out.printf("Main module Different file found     =%s\n", targetResult.getNonPackageDifferentCount());
			System.out.printf("Main module file not found           =%s\n", targetResult.getNonPackageNotFoundCount());
			System.out.printf("Sub module Same file found           =%s\n", targetResult.getPackagePassCount());
			System.out.printf("Sub module Different file found      =%s\n", targetResult.getPackageDifferentCount());
			System.out.printf("Sub module file not found            =%s\n", targetResult.getPackageNotFoundCount());
			if (targetResult.getProblemJarSet().size() > 0) {
				System.out.println("Java Package found different PRODUCTION against SOURCE");
				for (String jarName : targetResult.getProblemJarSet()) {
					System.out.println("Package does not found from PRODUCTION " + jarName);
				}
			}
			System.out.println("======================================================================");
			System.exit(-1);
		}

	}
}
