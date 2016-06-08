package com.david.binaryCompare;

import javax.xml.stream.events.NotationDeclaration;

public class CompareResult {
	
	private int notFoundCount;
	private int differentCount;
	private int passCount;
	
	
	public CompareResult() {
		super();
		notFoundCount=0;
		differentCount=0;
		passCount=0;
	}
	
	
	public CompareResult(int notFoundCount, int differentCount, int passCount) {
		super();
		this.notFoundCount = notFoundCount;
		this.differentCount = differentCount;
		this.passCount = passCount;
	}


	public int getNotFoundCount() {
		return notFoundCount;
	}
	public int getDifferentCount() {
		return differentCount;
	}
	public int getPassCount() {
		return passCount;
	}
	
	public void notFound(){
		notFoundCount++;
	}
	
	public void differentFound(){
		differentCount++;
	}
	
	public void passFound(){
		passCount++;
	}
	
	public boolean isPass(){
		if (notFoundCount==0 && differentCount==0) return true;
		return false;
	}
	
	

}
