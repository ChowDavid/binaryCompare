package com.david.binaryCompare;

public class FileAliasUtil {

	/**
	 * use to return an packageness name back to the system
	 * it replace all -,or number to empty
	 * @param fileName
	 * @return
	 */
	public static String getAlias(String fileName){
		if (fileName==null){
			return null;
		}
		if (fileName.length()==0){ 
			return fileName;
		}
		String newName=fileName.replaceAll("\\\\", "/");
		StringBuilder sb=new StringBuilder();
		boolean first=true;
		for (String part:newName.split("/")){
			String newPart=part;
			if (!first){
				sb.append("/");
			}
			if (part.endsWith(".ear") || part.endsWith(".jar") || part.endsWith(".war")){
				//newPart=part.replaceAll("(-[0-9]+\\.[0-9]+(\\.[0-9]){0,1})|(-SNAPSHOT)|(-RELEASE)|(-betal)","");
				newPart=part.replaceAll("-[0-9]+\\.[0-9]+(\\.[0-9]){0,1}-SNAPSHOT","");
				newPart=newPart.replaceAll("-[0-9]+\\.[0-9]+(\\.[0-9]){0,1}-RELEASE","");
				newPart=newPart.replaceAll("-[0-9]+\\.[0-9]+(\\.[0-9]){0,1}-betal","");
				newPart=newPart.replaceAll("-[0-9]+\\.[0-9]+(\\.[0-9]){0,1}.+\\.jar",".jar");
				newPart=newPart.replaceAll("-[0-9]+\\.[0-9]+(\\.[0-9]){0,1}.+\\.war",".war");
				newPart=newPart.replaceAll("-[0-9]+\\.[0-9]+(\\.[0-9]){0,1}.+\\.ear",".ear");
				newPart=newPart.replaceAll("-[0-9]+\\.[0-9]+(\\.[0-9]){0,1}","");
			}
			sb.append(newPart);
			first=false;
		}
		return sb.toString();
	}
	
	
	
	

}
