/*******************************************************************************************
 *	Copyright (c) 2016, zzg.zhou(11039850@qq.com)
 * 
 *  Monalisa is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU Lesser General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.

 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU Lesser General Public License for more details.

 *	You should have received a copy of the GNU Lesser General Public License
 *	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************************/
package com.tsc9526.monalisa.tools.parser.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.tsc9526.monalisa.tools.clazz.MelpClass;
import com.tsc9526.monalisa.tools.datatable.CaseInsensitiveMap;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class KeyMapping extends HashMap<Object,Object> implements Serializable, Cloneable {
    private static final long serialVersionUID = -1074655917369299456L;

    private boolean caseSensitive = false;
    private String  prefix=null; 
    private Map<String, String> hNameMapping=new CaseInsensitiveMap<String>();
    
    public KeyMapping(Map<String,?> data, String... mappings){
    	super();
        	  
    	initCheck(data,mappings);
    	inputData(data,mappings);
    }
    
    private void initCheck(Map<String,?> data, String... mappings){
    	if(mappings!=null){
        	for(String m:mappings){
        		if(m.indexOf("=")<0){
        			if(m.indexOf(MelpClass.OPTIONS_NAME_CASE_SENSITIVE)>=0){
        				caseSensitive=true;
        			}else if(m.startsWith("~")){
        				prefix=m.substring(1);							 
					}	        			 
        		}
        	}
        
        	for(String m:mappings){
        		if(m.indexOf("=")>0){
	        		String[] nv=m.split("=");	        		
	        		
	        		hNameMapping.put(nv[0].trim(),nv[1].trim());		        		 
        		}
        	}
        }
    }
        
    private void inputData(Map<String,?> data, String... mappings){	        
        for(Object key:data.keySet()){
        	String k=key.toString();
        	Object v=data.get(key);
        	
        	if(prefix!=null){
    			if(k.startsWith(prefix)){
    				k=k.substring(prefix.length());
    				if(k.length()>0){
    					char c=k.charAt(0);
    					if(!( (c>='a' && c<='z') || (c>='A' && c<='Z') ) ){
    						k=k.substring(1);
    					}
    				}
    				
    				if(k.length()<1){
    					continue;
    				}
    			}else{
    				continue;
    			}
    		}
        	
        	if(hNameMapping.containsKey(k)){
        		k=hNameMapping.get(k);
        	} 	       	
        	this.put(k,v);
        }
    }
    
    public Object put(Object key,Object value){
    	return super.put(convertKey(key),value);
    }
    
    public Object get(Object key){
    	return super.get(convertKey(key));
    }
    
    public boolean containsKey(Object key){
    	return super.containsKey(convertKey(key));
    }
    
    protected Object convertKey(Object key) {
        if (key != null) {
        	if(caseSensitive){
	    		return key.toString();
	    	}else{
	    		return key.toString().toLowerCase();
        	}	
        } else {
            return null;
        }
    } 
}