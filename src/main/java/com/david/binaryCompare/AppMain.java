package com.david.binaryCompare;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

/**
 * Hello world!
 *
 */
public class AppMain {
    public static void main( String[] args ) throws IOException{
        System.out.println( "Binary Compare Start It will read two java package and compare and list the different" );
        if (args.length<2){
        	System.out.println("Please provide source and target package in either jar,war or ear file or folder");
        	System.exit(1);
        }
        File sourceFile=new File(args[0]);
        File targetFile=new File(args[1]);
        if (!sourceFile.exists() || !targetFile.exists()){
        	System.out.println("Either source or target file not exists");
        	System.out.println("SOURCE="+sourceFile.getAbsolutePath());
        	System.out.println("PRODUCTION="+targetFile.getAbsolutePath());
        	System.exit(2);
        } else {
        	System.out.println("SOURCE="+sourceFile.getAbsolutePath());
        	System.out.println("PRODUCTION="+targetFile.getAbsolutePath());
        }
        File sourceTemp=new File("SOURCE_"+System.currentTimeMillis());
        File targetTemp=new File("PRODUCTION_"+System.currentTimeMillis());
        
        FileBreaker.unPackage(sourceFile, sourceTemp);
        FileBreaker.unPackage(targetFile, targetTemp);
        
        CompareResult result=FileCompare.directory(sourceTemp, targetTemp);
        if (result.isPass()){
        	System.out.println("Compare success complete");
        	System.exit(0);
        } else {
        	System.out.println("Compare with error");
        	System.out.println("Result Different ="+result.getDifferentCount());
        	System.out.println("Result Not Found ="+result.getNotFoundCount());
        	System.out.println("Result SameFound ="+result.getPassCount());
        	System.exit(-1);
        }

        
        
        
        
        
        
        
        
    }
}
