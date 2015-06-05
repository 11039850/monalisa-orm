package com.tsc9526.monalisa.plugin.eclipse.resources;

import org.eclipse.jface.resource.ImageDescriptor;

public final class Resource {
	public static ImageDescriptor getImage(String name){
		return ImageDescriptor.createFromFile(Resource.class, name+".png");
	}
	
	
}
