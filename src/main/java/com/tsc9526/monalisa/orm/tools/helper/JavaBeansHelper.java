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
package com.tsc9526.monalisa.orm.tools.helper;

import java.util.Date;
import java.util.HashMap;


/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */  
public class JavaBeansHelper { 
    private JavaBeansHelper() {
        super();
    }
   
    private static HashMap<String,Object> hDefaultValue=new HashMap<String, Object>(){    	 
		private static final long serialVersionUID = 7570819176794474057L;
		{
    		put(String.class.getSimpleName(), "");
    		put(Integer.class.getSimpleName(),new Integer(0)); 
    		put(Long.class.getSimpleName(),   new Long(0));
    		put(Float.class.getSimpleName(),  new Float(0));
    		put(Double.class.getSimpleName(), new Double(0));
    		put(Byte.class.getSimpleName(),   new Byte((byte)0));    		    		 
    	}
    };
    
    public static Object getDefaultValue(int jdbcType,Class<?> fieldType){
    	String type=TypeHelper.getJavaType(jdbcType);
    	
    	Object v=hDefaultValue.get(type);
    	if(v==null){
    		if(type.equals(Date.class.getName())){
    			v=new Date();
    		}
    	}
    	
    	return v;
    }
    
    public static String getGetterMethodName(String property,String javaType) {
        StringBuilder sb = new StringBuilder();

        sb.append(property);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
         
        if (javaType.equals("boolean") || javaType.equals("Boolean") || javaType.equals("java.lang.Boolean")) {
            sb.insert(0, "is");
        } else {
            sb.insert(0, "get");
        }

        return sb.toString();
    }

    /**
     * JavaBeans rules:<br>
     * 
     * eMail     -&gt; setEMail() <br>
     * firstName -&gt; setFirstName() <br>
     * URL       -&gt; setURL()<br> 
     * XAxis     -&gt; setXAxis() <br> 
     * a         -&gt; setA() <br> 
     * B         -&gt; setB() <br> 
     * @param property property
     * @return the setter method name
     */
    public static String getSetterMethodName(String property) {
        StringBuilder sb = new StringBuilder();

        sb.append(property);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
         
        sb.insert(0, "set");

        return sb.toString();
    }

    public static String getJavaName(String name,boolean firstCharacterUppercase){
    	if(name==null || name.trim().length()==0){
    		return "_";
    	}
    	
	    String javaName=JavaBeansHelper.getCamelCaseString(name, firstCharacterUppercase);			
		 
		if(TypeHelper.isJavaKeyword(javaName) || javaName.length()==0){
			javaName=javaName+"_";
		}
		return javaName;
    }
    
    public static String getCamelCaseString(String inputString,boolean firstCharacterUppercase) {
        StringBuilder sb = new StringBuilder();
        
        boolean nextUpperCase = false;
        for (int i = 0; i < inputString.length(); i++) {
            char c = inputString.charAt(i);
            if( (c>='0' && c<='9') || (c>='a' && c<='z') ||  (c>='A' && c<='Z') ){
            	if(c>='0' && c<='9' && sb.length()==0){
            		sb.append("N");
            	}
            	
            	if (nextUpperCase) {
                    sb.append(Character.toUpperCase(c));
                    nextUpperCase = false;
                } else {
                    sb.append(Character.toLowerCase(c));
                } 
            }else if ((c >= 0x4e00)&&(c <= 0x9fbb)){ 
            	//中文字
            	sb.append(c);
            }else{
            	if (sb.length() > 0) {
                    nextUpperCase = true;
                }            		
            }
        }

        if (firstCharacterUppercase) {
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        }

        return sb.toString();
    } 
}
