package org.bonitasoft.reporting.utils.tools;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ReportResourceLoader extends ClassLoader {

	private ClassLoader parentCL = null;
	private String resourcePath=null;

	/**
	 * Instantiates a new dynamic class loader.
	 * 
	 * @param jarFileName the jar file name
	 * @param cl the cl
	 */

	public ReportResourceLoader(String path,ClassLoader cl) {		
		super(cl);
		resourcePath=path;
	}




	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}



	
	@Override
	public InputStream getResourceAsStream(String name) {
		String resourceFile = resourcePath + name;		
		File file=new File(resourceFile);
		FileInputStream fis=null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			return null;
		}
		return fis;
	}

}