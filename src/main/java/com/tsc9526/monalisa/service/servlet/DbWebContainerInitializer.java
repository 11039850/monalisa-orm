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
package com.tsc9526.monalisa.service.servlet;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.HandlesTypes;

import com.tsc9526.monalisa.orm.annotation.DB;
import com.tsc9526.monalisa.orm.datasource.DBConfig;
import com.tsc9526.monalisa.tools.logger.Logger;
import com.tsc9526.monalisa.tools.string.MelpString;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@HandlesTypes(DB.class)
public class DbWebContainerInitializer implements ServletContainerInitializer {
	static Logger logger=Logger.getLogger(DbWebContainerInitializer.class);
	 
	public void onStartup(Set<Class<?>> dbAnnotationClasses, ServletContext servletContext)throws ServletException {
		String webroot=servletContext.getContextPath();
		logger.info("Startup web: "+ (webroot.length()==0?"/":webroot) );
	
		servletContext.addListener(DestoryListener.class);
		
		List<Class<?>> dbsc=new ArrayList<Class<?>>();
		for(Class<?> dbclazz:dbAnnotationClasses){
			DBConfig db=DBConfig.fromClass(dbclazz);
			if(!MelpString.isEmpty(db.getCfg().getDbs())){
				dbsc.add(dbclazz);
			}
		}
		
		if(dbsc.size()>0){
			startDBService(servletContext,dbsc);
		}
	}
	
	protected void startDBService(ServletContext servletContext,List<Class<?>> dbAnnotationClasses){
		final Map<String, String[]> cfgfiles=new HashMap<String, String[]>();
		for(int i=0;i<dbAnnotationClasses.size();i++){
			Class<?> clazz=dbAnnotationClasses.get(i);
			DBConfig db=DBConfig.fromClass(clazz);
			
			String configfile=db.getCfg().getConfigFile();
			String prefix=DbQueryHttpServlet.DB_CFG_PREFIX+(i+1);
			
			String cfgname=db.getCfg().getConfigName();
			if(MelpString.isEmpty(cfgname)){
				cfgname="cfg";
			}
			
			cfgname=DBConfig.PREFIX_DB+"."+cfgname;
			cfgfiles.put(prefix,new String[]{cfgname,configfile});
		}
		
		DbQueryHttpServlet servlet=new DbQueryHttpServlet(){
			private static final long serialVersionUID = -3809557368401L;
			protected void printAuthWarn(String prefix){
				String[] cfgfile=cfgfiles.get(prefix);
				
				logger.warn("Missing auth config\r\n"+/**~!{*/""
						+ "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
						+ "\r\n!!! Missing auth config: " +((cfgfile[0]))+ ".dbs.auth.users in " +((cfgfile[1]))+ ", "
						+ "\r\n!!! default authorization is monalisa:monalisa (user:password,user2:password2 ...) "
						+ "\r\n!!! set to \"none\" means disable auth"
						+ "\r\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
					+ "\r\n"/**}*/.trim());
			}
		};
		ServletRegistration regist=servletContext.addServlet("dbs", servlet);
		regist.addMapping("/dbs/*"); 
		
		for(int i=0;i<dbAnnotationClasses.size();i++){
			Class<?> clazz=dbAnnotationClasses.get(i);
			DBConfig db=DBConfig.fromClass(clazz);
			Properties dbsp=db.getCfg().getDbsProperties();
			
			String name=dbsp.getProperty("name");
			String spath=servletContext.getContextPath()+"/dbs/"+name;
			logger.info("Add service("+db.getKey()+") context path: "+spath);
			     
			String prefix=DbQueryHttpServlet.DB_CFG_PREFIX+(i+1)+".";
			
			Map<String, String> initParameters=new LinkedHashMap<String, String>();
			initParameters.put(prefix+"class", clazz.getName());
			for(Object key:dbsp.keySet()){
				initParameters.put(prefix+key, dbsp.getProperty(key.toString()));
			}
			regist.setInitParameters(initParameters);
			
			for(String key:initParameters.keySet()){
				regist.setInitParameter(key,initParameters.get(key));
			}
		}
	}

}
