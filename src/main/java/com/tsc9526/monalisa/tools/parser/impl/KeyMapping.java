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

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class KeyMapping extends HashMap<String,Object> implements Serializable, Cloneable {
    private static final long serialVersionUID = -1074655917369299456L;
 
    private String  prefix     = null; 
    private boolean ignoreCase = false;
    private Map<String, String> hNameMapping=new HashMap<String,String>();
    
    public KeyMapping(Map<String,?> data, String... mappings){
    	super();
        	  
    	initCheck(data,mappings);
    	inputData(data,mappings);
    }
    
    private void initCheck(Map<String,?> data, String... mappings){
    	if(mappings!=null){
        	for(String m:mappings){
        		if(m.indexOf("=")<0){
        			if(m.equals("~")) {
						ignoreCase = true;
					}else if(m.startsWith("~")){
        				prefix=m.substring(1).trim();							 
					}
        		}
        	}
        
        	prefix = convertKey(prefix); 
        	 
        	for(String m:mappings){
        		if(m.indexOf("=")>0){
	        		String[] nv   = m.split("=");	        		
	        		String mKey   = nv[0].trim();
	        		String mValue = nv[1].trim();
	        		 
	        		hNameMapping.put(convertKey(mKey),convertKey(mValue));		        		 
        		}
        	}
        }
    }
  
    
    private void inputData(Map<String,?> data, String... mappings){	        
        for(Object key:data.keySet()){
        	String k = convertKey ( key.toString() );
        	Object v = data.get(key);
        	 
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
        		k = convertKey ( hNameMapping.get(k) );
        	} 	       	
        	this.put(k,v);
        }
    }
    
    public Object put(String key,Object value){
    	return super.put(convertKey(key),value);
    }
    
    public Object get(Object key){
    	return super.get(convertKey((String)key));
    }
    
    public boolean containsKey(Object key){
    	return super.containsKey(convertKey((String)key));
    }
   
    protected String convertKey(String key) {
    	if(ignoreCase && key!=null) {
    		return key.toLowerCase();
    	}
        return key;
    } 
}