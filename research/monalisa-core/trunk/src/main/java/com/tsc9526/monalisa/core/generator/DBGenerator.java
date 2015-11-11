package com.tsc9526.monalisa.core.generator;

import java.util.List;

import com.tsc9526.monalisa.core.datasource.DBConfig;
import com.tsc9526.monalisa.core.meta.MetaPartition;
import com.tsc9526.monalisa.core.meta.MetaTable;
import com.tsc9526.monalisa.core.query.DbProp;
import com.tsc9526.monalisa.core.query.model.Model;
import com.tsc9526.monalisa.core.query.partition.Partition;
/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@SuppressWarnings("rawtypes")
public abstract class DBGenerator {
	public static String PROJECT_TMP_PATH="/target/monalisa";
		 
	protected DBConfig dbcfg;
	protected DBMetadata dbmetadata; 
	
	protected String javaPackage;
	protected String resourcePackage;

	protected String dbi;
	
	public void generateFiles(){					
		List<MetaTable> tables=dbmetadata.getTables();
		for(MetaTable table:tables){
			generateJavaFile(table);		 
		}	
		
		generateResources(tables);
	}
	
	
	protected void verifyPartition(MetaTable table){
		MetaPartition mp=table.getPartition();
		if(mp!=null){			 
			try{ 
				Partition p=mp.getPartition();				 
				String error=p.verify(mp,table);
				if(error!=null){
					error(error);
				}						 				
			}catch(Exception e) {
				if(e instanceof RuntimeException){
					RuntimeException re=(RuntimeException)e;
					if(re.getCause()!=null && re.getCause() instanceof ClassNotFoundException){
						//Ingore this exception
						return;
					} 
				}
				e.printStackTrace(System.out);
				
				error(e.getClass().getName()+":\r\n"+e.getMessage());
			}
		}
	}
	 
	protected void error(String message){
		System.out.println(message);
	}
	
	
	protected String getModelClassValue(MetaTable table){
		String modelClass=dbcfg.getProperty(DbProp.PROP_TABLE_MODEL_CLASS+"."+table.getName(), dbcfg.modelClass()); 
		
		if(modelClass==null || modelClass.trim().length()==0){
			modelClass=Model.class.getName(); 
		}
		
		return modelClass;
	}
	 
	
	protected abstract void generateJavaFile(MetaTable table);

	protected abstract void generateResources(List<MetaTable> tables);
}
