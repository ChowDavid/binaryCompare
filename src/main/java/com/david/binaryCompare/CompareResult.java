package com.david.binaryCompare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
	private Set<String> jdks;
	private List<String> notfoundLists;
	private List<String> differentLists;
	private List<String> unSuccessDecompileClassLists;
	private List<String> passLists;

	public CompareResult() {
		super();
		passJarSet=new HashSet<String>();
		problemJarSet=new HashSet<String>();
		jdks=new HashSet<String>();
		notfoundLists=new ArrayList<String>();
		differentLists=new ArrayList<String>();
		unSuccessDecompileClassLists=new ArrayList<String>();		
		passLists=new ArrayList<String>();
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
		this.jdks=new HashSet<String>(otherResult.getJdks());
		this.notfoundLists=new ArrayList<String>(otherResult.getNotfoundLists());
		this.differentLists=new ArrayList<String>(otherResult.getDifferentLists());
		this.unSuccessDecompileClassLists=new ArrayList<String>(otherResult.getUnSuccessDecompileClassLists());
		this.passLists=new ArrayList<String>(otherResult.getPassLists());
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

	public Set<String> getJdks() {
		return jdks;
	}


	public List<String> getNotfoundLists() {
		return notfoundLists;
	}

	public List<String> getDifferentLists() {
		return differentLists;
	}


	public List<String> getUnSuccessDecompileClassLists() {
		return unSuccessDecompileClassLists;
	}

	public List<String> getPassLists() {
		return passLists;
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
