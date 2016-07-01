package com.david.binaryCompare;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class FileWareHouse {
	
	private Map<String,List<File>> maps;

	public FileWareHouse(File rootFolder) {
		super();
		maps=new HashMap<String,List<File>>();
		for (File file:FileUtils.listFiles(rootFolder, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE)){
			if (customFilter(rootFolder,file)){
				continue;
			}
			String name=FileAliasUtil.getAlias(file.getAbsolutePath().substring((int)rootFolder.getAbsolutePath().length()+1));
			if (maps.containsKey(name)){
				maps.get(name).add(file);
			} else {
				List<File> files=new ArrayList<File>();
				files.add(file);
				maps.put(name, files);
			}
			
		}
		
	}
	
	/*
	 * if hit the file name has folder *.war.* it will return true
	 */
	private boolean customFilter(File rootFolder, File file) {
		String fileName=file.getParentFile().getAbsolutePath().substring(rootFolder.getAbsolutePath().length());
		fileName=fileName.replaceAll("\\\\", "/");
		
		for (String fileSessionName:fileName.split("/")){
			Matcher shouldMatch=Pattern.compile("^.+\\.(war)|(ear)|(jar)").matcher(fileSessionName);
			Matcher shouldNotMatch=Pattern.compile("(jar$)|(war$)|(ear$)").matcher(fileSessionName);
			if (shouldMatch.find() && !shouldNotMatch.find()){
				return true;
			}
		}
		return false;
	}

	public List<File> getByName(String name){
		if (maps.containsKey(name)){
			return maps.get(name);
		}
		return null;
	}
	
	public Set<String> getAllNames(){
		return maps.keySet();
	}

	@Override
	public String toString() {
		return "FileWareHouse [maps=" + maps + "]";
	}
	
	

}
