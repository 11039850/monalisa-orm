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
package com.tsc9526.monalisa.orm.tools.generator;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.tsc9526.monalisa.orm.datasource.DBConfig;
import com.tsc9526.monalisa.orm.datasource.DbProp;
import com.tsc9526.monalisa.orm.meta.MetaPartition;
import com.tsc9526.monalisa.orm.meta.MetaTable;
import com.tsc9526.monalisa.orm.model.Model;
import com.tsc9526.monalisa.orm.partition.Partition;
import com.tsc9526.monalisa.orm.processor.DBAnnotationProcessor;
import com.tsc9526.monalisa.orm.tools.logger.Logger;
/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@SuppressWarnings("rawtypes")
public abstract class DBGenerator {
	public static Logger plogger=Logger.getLogger(DBAnnotationProcessor.class);
	  
	protected DBConfig dbcfg;
	protected DBMetadata dbmetadata; 
	
	protected String javaPackage;
	protected String resourcePackage;

	protected String dbi;
	 
	public void generateFiles(){					
		List<MetaTable> tables=dbmetadata.getTables();
		
		plogger.info("Loaded tables: "+tables.size()+", package: "+javaPackage);
		
		generateJavaFiles(tables);		 
		generateResources(tables);
	}
	
	protected void verifyPartition(MetaTable table){
		MetaPartition mp=table.getPartition();
		if(mp!=null){			 
			try{ 
				Partition p=mp.getPartition();				 
				String error=p.verify(mp,table);
				if(error!=null){
					plogger.error(error);
				}						 				
			}catch(Exception e) {				 
				plogger.error(e.getClass().getName()+":\r\n"+e.getMessage(),e);
			}
		}
	}
	 
 
	
	protected String getModelClassValue(MetaTable table){
		String modelClass=dbcfg.getCfg().getProperty(DbProp.PROP_TABLE_MODEL_CLASS.getKey()+"."+table.getName(), dbcfg.getCfg().getModelClass()); 
		
		if(modelClass==null || modelClass.trim().length()==0){
			modelClass=Model.class.getName(); 
		}
		
		return modelClass;
	}
	 
	
	protected void generateResources(List<MetaTable> tables){		
		try{	
			List<String> rns=new ArrayList<String>();
			for(MetaTable table:tables){
				if(table.getCreateTable()!=null){
					rns.add(table.getName());
				}
			}
			
			if(rns.size()>0){
				plogger.info("Create resource from tables: "+rns);
				
				Writer w = getResourceWriter();
				for(MetaTable table:tables){
					if(table.getCreateTable()!=null){
						w.write("/***CREATE TABLE: "+table.getNamePrefix()+" :: "+table.getName()+"***/\r\n");
						w.write(table.getCreateTable().getOriginSQL()); 
						w.write("\r\n\r\n\r\n");
					}
				}
				w.close();
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	protected void generateJavaFiles(List<MetaTable> tables){		
		try{
			int maxLen=0;
			for(MetaTable table:tables){
				int len=table.getJavaName().length();
				if(len>maxLen){
					maxLen=len;
				}
			}
			
			int i=1;
			int total=tables.size();
			for(MetaTable table:tables){
				String message=(i+"/"+total)+" "+" Create class: "+getRightPadding(table.getJavaName(),maxLen,' ')+" from table: "+table.getName();
				plogger.info(message);
				
				MetaTable clone=table.clone();
				clone.setJavaName(null).setName(clone.getNamePrefix());
				
				Writer writer=getJavaWriter(clone);
				 
				DBTableGenerator g2=new DBTableGenerator(clone, getModelClassValue(clone), dbi);
				g2.generate(writer);
				
				verifyPartition(table);
				
				i++;
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	private String getRightPadding(String s,int maxLen,char paddingChar){
		StringBuilder sb=new StringBuilder(s);
		
		int delta=maxLen-s.length();
		for(int i=0;i<delta;i++){
			sb.append(paddingChar);
		}
		return sb.toString();
	}
	
	
	protected abstract Writer getJavaWriter(MetaTable table);
	
	protected abstract Writer getResourceWriter();
}
