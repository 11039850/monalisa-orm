package com.tsc9526.monalisa.plugin.eclipse.tools;

import javax.annotation.processing.ProcessingEnvironment;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.apt.pluggable.core.dispatch.IdeBuildProcessingEnvImpl;

@SuppressWarnings("restriction")
public class IdeProcessing {
	private ProcessingEnvironment processingEnv;
	
	public IdeProcessing(ProcessingEnvironment processingEnv){
		this.processingEnv=processingEnv;
	}
	 
	public String getProjectPath(){		
		if (processingEnv instanceof IdeBuildProcessingEnvImpl) {			
			IJavaProject project = ((IdeBuildProcessingEnvImpl) processingEnv).getJavaProject();
			String path=project.getProject().getLocation().toString();
			return path;
			
			/*
			try{
				String[] cp=org.eclipse.jdt.launching.JavaRuntime.computeDefaultRuntimeClassPath(project);
				for(String c:cp){
					System.out.println(c);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			*/
		}
		
		return ".";		
	}
}
