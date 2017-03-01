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
package com.tsc9526.monalisa.tools.template;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.tsc9526.monalisa.tools.logger.Logger;
import com.tsc9526.monalisa.tools.string.MelpString;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class VarTemplate {
	static Logger logger=Logger.getLogger(VarTemplate.class);
	
	private Map<String, String> variables;
	private String  nullVar         ="";
	private boolean throwExceptionOnVarNotFound=false;
	
	public VarTemplate(Map<String, String> variables){
		this.variables=variables;
	}
	
	public VarTemplate(Properties variables){
		this.variables=new HashMap<String, String>();
		for(Object key:variables.keySet()){
			this.variables.put((String)key, (String)variables.get(key));
		}
	}
	
	public String getValue(String templateString){
		if(templateString==null){
			return null;
		}
		
		if(templateString.length()==0){
			return "";
		}
		
		Set<String> missingVars=new LinkedHashSet<String>();
		
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<templateString.length();i++){
			char c1=templateString.charAt(i);
			if(c1=='$' && (i+1)<templateString.length()){
				char c2=templateString.charAt(i+1);
				if(c2=='{'){//${...}
					int x2=templateString.indexOf("}",i+1);
					if(x2>0){
						String var=templateString.substring(i+2,x2);
						String value=variables.get(var);
						if(value!=null){
							sb.append(value);
						}else{
							sb.append(nullVar);
							missingVars.add(var);
						}
						i=x2;
					}else{
						sb.append(c1);
					}
				}else if( (c2>='a' && c2<='z') || (c2>='A' && c2<='Z') || c1=='_'){//$var_abc
					StringBuilder var=new StringBuilder();
					var.append(c2);
					
					i+=2;
					while(i<templateString.length()){
						char c3=templateString.charAt(i);
						
						if( (c3>='a' && c3<='z') || (c3>='A' && c3<='Z') || (c3>='0' && c3<='9') || c3=='_'){
							var.append(c3);
						}else{
							i--;
							
							break;
						}
						
						i++;
					}
					 
					String value=variables.get(var.toString());
					if(value!=null){
						sb.append(value);
					}else{
						sb.append(nullVar);
						missingVars.add(var.toString());
					}
				}else{//$ 单独一个
					sb.append("$");
				}
			}else{
				sb.append(c1);
			}
		}
		
		StringBuilder msg=new StringBuilder();
		if(missingVars.size()>0){
			msg.append("Missing variables: ");
			msg.append(MelpString.toString(missingVars));
			msg.append(", template: "+templateString+", exist variable: "+MelpString.toString(variables));
		}
		
		if(throwExceptionOnVarNotFound && missingVars.size()>0){
			throw new RuntimeException(msg.toString());
		}else{
			if(msg.length()>0){
				logger.warn(msg.toString());
			}
			return sb.toString();
		}
	}

	public String getNullVar() {
		return nullVar;
	}

	public VarTemplate setNullVar(String nullVar) {
		this.nullVar = nullVar;
		return this;
	}

	public boolean isThrowExceptionOnVarNotFound() {
		return throwExceptionOnVarNotFound;
	}

	public VarTemplate setThrowExceptionOnVarNotFound(boolean throwVarNotFound) {
		this.throwExceptionOnVarNotFound = throwVarNotFound;
		return this;
	}
}
