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
package com.tsc9526.monalisa.core.generator;

import java.io.Writer;
import java.util.List;

import com.tsc9526.monalisa.core.datasource.DBConfig;
import com.tsc9526.monalisa.core.datasource.DbProp;
import com.tsc9526.monalisa.core.logger.Logger;
import com.tsc9526.monalisa.core.meta.MetaPartition;
import com.tsc9526.monalisa.core.meta.MetaTable;
import com.tsc9526.monalisa.core.processor.DBAnnotationProcessor;
import com.tsc9526.monalisa.core.query.model.Model;
import com.tsc9526.monalisa.core.query.partition.Partition;
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
		
		plogger.info("Loaded tables: "+tables.size());
		
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
			Writer w = getResourceWriter();
			for(MetaTable table:tables){
				if(table.getCreateTable()!=null){
					plogger.info("["+table.getName()+"] Create resources");
				 	
					w.write("/***CREATE TABLE: "+table.getNamePrefix()+" :: "+table.getName()+"***/\r\n");
					w.write(table.getCreateTable().getOriginSQL()); 
					w.write("\r\n\r\n\r\n");
				}
			}
			w.close();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	protected void generateJavaFiles(List<MetaTable> tables){		
		try{			
			for(MetaTable table:tables){
				plogger.info("["+table.getName()+"] Create class: "+table.getJavaName());
				
				MetaTable clone=table.clone();
				clone.setJavaName(null).setName(clone.getNamePrefix());
				
				Writer writer=getJavaWriter(clone);
				 
				DBTableGenerator g2=new DBTableGenerator(clone, getModelClassValue(clone), dbi);
				g2.generate(writer);
				
				verifyPartition(table);
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	
	protected abstract Writer getJavaWriter(MetaTable table);
	
	protected abstract Writer getResourceWriter();
}
