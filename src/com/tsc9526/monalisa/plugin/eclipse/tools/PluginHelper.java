package com.tsc9526.monalisa.plugin.eclipse.tools;

import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.TagElement;

public class PluginHelper {

	public static String getJavadocField(Javadoc doc,String property){
		String value=null;	
		if(doc!=null && doc.isDocComment()){			
			for(Object o:doc.tags()){
				TagElement tag=(TagElement)o;
				String tn=tag.getTagName();
				if(property.equals(tn)){
					value=tag.toString();
					int p=value.indexOf(property);
					value=value.substring(p+property.length()).trim();
					break;
				}
			}
		}
		return value;
	}
}
