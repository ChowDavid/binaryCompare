package com.david.binaryCompare;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.xml.stream.events.NotationDeclaration;

public class CompareResult {
	

	private int nonPackageNotFoundCount;
	private int packageNotFoundCount;
	private int nonPackageDifferentCount;
	private int packageDifferentCount;
	private int packagePassCount;
	private int nonPackagePassCount;
	private Set<String> passJarSet;
	private Set<String> problemJarSet;
	
	
	public CompareResult() {
		super();
		passJarSet=new HashSet<String>();
		problemJarSet=new HashSet<String>();

		
	}
	











	public CompareResult(int nonPackageNotFoundCount, int packageNotFoundCount, int nonPackageDifferentCount,
			int packageDifferentCount, int packagePassCount, int nonPackagePassCount) {
		super();
		this.nonPackageNotFoundCount = nonPackageNotFoundCount;
		this.packageNotFoundCount = packageNotFoundCount;
		this.nonPackageDifferentCount = nonPackageDifferentCount;
		this.packageDifferentCount = packageDifferentCount;
		this.packagePassCount = packagePassCount;
		this.nonPackagePassCount = nonPackagePassCount;
	}












	public void set(CompareResult otherResult){
		this.nonPackageDifferentCount=otherResult.getNonPackageDifferentCount();
		this.packageDifferentCount=otherResult.getPackageDifferentCount();
		this.packagePassCount=otherResult.getPackagePassCount();
		this.nonPackagePassCount=otherResult.getNonPackagePassCount();
		this.nonPackageNotFoundCount=otherResult.getNonPackageNotFoundCount();
		this.packageNotFoundCount=otherResult.getPackageNotFoundCount();
		this.passJarSet=new HashSet<String>(otherResult.getPassJarSet());
		this.problemJarSet=new HashSet<String>(otherResult.getProblemJarSet());
	}





	
	public int getNonPackageDifferentCount() {
		return nonPackageDifferentCount;
	}












	public int getPackageDifferentCount() {
		return packageDifferentCount;
	}












	public int getPackagePassCount() {
		return packagePassCount;
	}


	public int getNonPackageNotFoundCount() {
		return nonPackageNotFoundCount;
	}




	public void setNonPackageNotFoundCount(int nonPackageNotFoundCount) {
		this.nonPackageNotFoundCount = nonPackageNotFoundCount;
	}




	public int getPackageNotFoundCount() {
		return packageNotFoundCount;
	}




	public void setPackageNotFoundCount(int packageNotFoundCount) {
		this.packageNotFoundCount = packageNotFoundCount;
	}




	public int getNonPackagePassCount(){
		return nonPackagePassCount;
	}
	

	public void nonPackageDifferentFound(){
		nonPackageDifferentCount++;
	}
	
	public void packageDifferentFound(){
		packageDifferentCount++;
	}
	
	public void packagePassCountFound(){
		packagePassCount++;
	}
	
	public void nonPackagePassCountFound(){
		nonPackagePassCount++;
	}
	public boolean isPass(){
		return nonPackageNotFoundCount==0 && packageNotFoundCount==0 && nonPackageDifferentCount==0 && packageDifferentCount==0 && problemJarSet.size()==0;
		
	}


	public Set<String> getPassJarSet() {
		return passJarSet;
	}


	public void setPassJarSet(Set<String> passJarSet) {
		this.passJarSet = passJarSet;
	}


	public Set<String> getProblemJarSet() {
		return problemJarSet;
	}


	public void setProblemJarSet(Set<String> problemJarSet) {
		this.problemJarSet = problemJarSet;
	}

	public void nonPackageNotFound(){
		nonPackageNotFoundCount++;
	}
	
	public void packageNotFound(){
		packageNotFoundCount++;
	}











	@Override
	public String toString() {
		return "CompareResult [nonPackageNotFoundCount=" + nonPackageNotFoundCount + ", packageNotFoundCount="
				+ packageNotFoundCount + ", nonPackageDifferentCount=" + nonPackageDifferentCount
				+ ", packageDifferentCount=" + packageDifferentCount + ", packagePassCount=" + packagePassCount
				+ ", nonPackagePassCount=" + nonPackagePassCount + ", passJarSet=" + passJarSet + ", problemJarSet="
				+ problemJarSet + "]";
	}











	
	

}
