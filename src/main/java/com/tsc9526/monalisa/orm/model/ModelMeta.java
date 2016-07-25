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
package com.tsc9526.monalisa.orm.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import com.tsc9526.monalisa.orm.annotation.Column;
import com.tsc9526.monalisa.orm.annotation.DB;
import com.tsc9526.monalisa.orm.annotation.Index;
import com.tsc9526.monalisa.orm.annotation.Table;
import com.tsc9526.monalisa.orm.datasource.DBConfig;
import com.tsc9526.monalisa.orm.datasource.DBTasks;
import com.tsc9526.monalisa.orm.datasource.DbProp;
import com.tsc9526.monalisa.orm.dialect.Dialect;
import com.tsc9526.monalisa.orm.meta.MetaColumn;
import com.tsc9526.monalisa.orm.meta.MetaPartition;
import com.tsc9526.monalisa.orm.meta.MetaTable;
import com.tsc9526.monalisa.orm.model.validator.Validator;
import com.tsc9526.monalisa.orm.tools.helper.ClassHelper;
import com.tsc9526.monalisa.orm.tools.helper.TableHelper;
import com.tsc9526.monalisa.orm.tools.helper.ClassHelper.FGS;
import com.tsc9526.monalisa.orm.tools.helper.ClassHelper.MetaClass;
import com.tsc9526.monalisa.orm.tools.logger.Logger;
 
/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class ModelMeta{	
	static Logger logger=Logger.getLogger(ModelMeta.class.getName());
	
	
	private static Map<String, ModelMeta> hMonitorMetas=new ConcurrentHashMap<String, ModelMeta>();
	private static Map<String, ModelMeta> hMetas=new HashMap<String, ModelMeta>();
	
	public synchronized static ModelMeta getModelMeta(Model<?> model){
		String key=getModelKey(model);
		
		ModelMeta mm=hMetas.get(key);
		
		int interval=DbProp.CFG_RELOAD_MODEL_INTERVAL;
		if(mm==null && interval>0){
			DBTasks.schedule("ModelChangeTask", new TimerTask() {
				public void run() {
					reloadModelMetas();
				}
			}, interval*1000, interval*1000);
		}
		
		
		if(mm==null || mm.iChanged()){
			mm=new ModelMeta(key);
			mm.init(model);
			
			if(mm.record){
				hMonitorMetas.put(key, mm);
			}
			
			hMetas.put(key, mm);
		}
		return mm;
	}
	 
	public synchronized static void reloadModelMetas(){
		for(ModelMeta mm:hMonitorMetas.values()){
			 mm.checkChanged();
		}
	}
	
	private static String getModelKey(Model<?> model){
		String key=model.getClass().getName();
		
		Class<?> clazz=ClassHelper.findClassWithAnnotation(model.getClass(),DB.class);
		if(clazz==null){
			if(model.$db==null){
				throw new RuntimeException("Dynamic model can not found DB, call model.use(DB) first!");
			}
			
			Table table=model.getClass().getAnnotation(Table.class);
			if(table==null){
				if(model.$tableName==null || model.$tableName.trim().length()<1){
					throw new RuntimeException("Dynamic model can not found table: "+model.$tableName+", call model(TableName) first!");
				}else{				
					key="#"+model.$db.getKey()+"$"+model.$tableName;
				}
			}
		}else{
			Table table=model.getClass().getAnnotation(Table.class);
			if(table==null){
				if(model.$tableName==null || model.$tableName.trim().length()<1){
					throw new RuntimeException("Dynamic model can not found table: "+model.$tableName+", call model(TableName) first!");
				}else{	
					DBConfig db=Model.dsm.getDBConfig(clazz);
					key=db.getKey()+"$"+model.$tableName;
				}
			}
		}
	 
		return key;
	}
	
	protected DBConfig  db;
	protected Dialect   dialect;
	
	protected String      tableName   = null;
	protected String[]    primaryKeys = null;
	protected Validator   validator   = null;
	 
	protected FGS       autoField;	
	protected Table     table;
	
	protected MetaPartition    mp;		 
	protected ModelListener    listener;
	protected List<ModelIndex> indexes=new ArrayList<ModelIndex>();
	
	protected Map<String,FGS> hFieldsByColumnName=new LinkedHashMap<String, ClassHelper.FGS>();
	protected Map<String,FGS> hFieldsByJavaName  =new LinkedHashMap<String, ClassHelper.FGS>();
	
	protected boolean record=false;
	protected boolean changed=false;
	
	protected String key;
	private ModelMeta(String key){		
		this.key=key;
	}
	
	void init(Model<?> model){		
		this.tableName  =model.$tableName;
		this.primaryKeys=model.$primaryKeys;
		
		initDB(model);
		
		initTable(model);
				
		initFields(model);
		
		initIndexes(model);
		
		initListeners(model);
		
		initPartioners(model); 
	}
	
	protected void initDB(Model<?> model) {
		Class<?> clazz=ClassHelper.findClassWithAnnotation(model.getClass(),DB.class);
		if(clazz!=null){
			db=Model.dsm.getDBConfig(clazz);
		}else{
			db=model.$db;
		}
		
		if(db==null){
			throw new RuntimeException("Model: "+model.getClass()+" must implement interface annotated by: "+DB.class+", Or call use(db) first!");
		}
		
		dialect=Model.dsm.getDialect(db);
	}
	
	protected void initTable(Model<?> model) {
		table=model.getClass().getAnnotation(Table.class);			  
		if(table==null){
			if(tableName==null || tableName.trim().length()==0){
				tableName=model.getClass().getSimpleName();
			}
			
			table=createTable(tableName,primaryKeys);
		}
	}
	
	protected void initFields(Model<?> model){
		List<FGS> fields=loadModelFields(model);		
		
		List<String> pks=new ArrayList<String>();
		for(Object o:fields){
			FGS fgs=(FGS)o;				
			Column c=fgs.getAnnotation(Column.class);
			
			hFieldsByColumnName.put(c.name().toLowerCase(),fgs);
			hFieldsByJavaName  .put(fgs.getFieldName().toLowerCase(),fgs);
			
			if(c.auto() && autoField==null){
				autoField=fgs;					 
			}
			
			if(c.key()){
				pks.add(c.name());
			}
		}
		
		if(primaryKeys==null || primaryKeys.length==0){
			primaryKeys=pks.toArray(new String[0]);
		}				
	}
	
	protected void initIndexes(Model<?> model) {
		Index[] tbIndexes=table.indexes();
		if(tbIndexes!=null && tbIndexes.length>0){
			for(Index index:tbIndexes){
				ModelIndex mIndex=new ModelIndex();
				mIndex.setName(index.name());
				mIndex.setType(index.type());
				mIndex.setUnique(index.unique());
				
				List<FGS> fs=new ArrayList<ClassHelper.FGS>();
				for(String f:index.fields()){
					FGS x=findFieldByName(f);
					
					assert x!=null;
					
					fs.add(x);
				}
				mIndex.setFields(fs);
				
				indexes.add(mIndex);
			}
		}
	}
	
	protected void initListeners(Model<?> model){
		String ls=DbProp.PROP_TABLE_MODEL_LISTENER.getValue(db,tableName);
		
		if(ls==null){
			ls=db.getCfg().getModelListener();
		}
		
		if(ls!=null && ls.trim().length()>0){
			try{
				listener=(ModelListener)ClassHelper.forClassName(ls.trim()).newInstance();
			}catch(Exception e){
				throw new RuntimeException("Invalid model listener class: "+ls.trim()+", "+e,e);
			}
		}		
	}
	
	protected void initPartioners(Model<?> model){
		mp=db.getCfg().getPartition(table.name());
	}
	
	protected List<FGS> loadModelFields(Model<?> model){
		MetaClass metaClass=ClassHelper.getMetaClass(model.getClass());
		List<FGS> fields=metaClass.getFieldsWithAnnotation(Column.class);						
		if(fields.size()==0){
			record=true;
			fields=loadFieldsFromDB(metaClass);	
			
			StringBuilder sb=new StringBuilder();
			for(FGS f:fields){
				if(sb.length()>0){
					sb.append(", ");
				}
				sb.append(f.getFieldName());
			}
			logger.info("Load table: "+tableName+"{"+sb.toString()+"}");
		}
		
		return fields;		
	}
	
	public void checkChanged(){
		if(mTable!=null && changed==false){
			try{
				MetaTable t2=TableHelper.getMetaTable(db, tableName);
				if(isTableFieldChanged(this.mTable,t2)){
					logger.info("Table struct changed: "+tableName);
					this.changed=true;
				}
			}catch(Exception e){
				logger.error("Check table: "+tableName+" exception: "+e,e);
			}
		}
	}
	
	private boolean isTableFieldChanged(MetaTable t1,MetaTable t2){
		if(t1==null || t2==null){
			return false;
		}
		
		for(MetaColumn x:t1.getColumns()){
			MetaColumn y=t2.getColumn(x.getName());
			if(y!=null){
				if(x.getJdbcType()!=y.getJdbcType() || x.getLength() !=y.getLength()){
					return true;
				}
			}else{
				return true;
			}
		}
		
		for(MetaColumn x:t2.getColumns()){
			MetaColumn y=t1.getColumn(x.getName());
			if(y!=null){
				if(x.getJdbcType()!=y.getJdbcType() || x.getLength() !=y.getLength()){
					return true;
				}
			}else{
				return true;
			}
		}
		
		return false;
	}
	
	
	private MetaTable mTable;
	protected List<FGS> loadFieldsFromDB(MetaClass metaClass) {
		try{
			mTable=TableHelper.getMetaTable(db, tableName);
			if(mTable!=null){				 
				List<FGS> fs=new ArrayList<FGS>();
				 
				for(MetaColumn c:mTable.getColumns()){
					FGS mfd=metaClass.getField(c.getJavaName());
					if(mfd==null){
						mfd=metaClass.getField(c.getName());
					}
					 
					FGS fgs=createFGS(c,mfd);	
					fs.add(fgs);								 
				}		
				 
				return fs;
			}else{
				throw new RuntimeException("Table not found: "+tableName+", DB: "+db.getKey());
			}			
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
	 
	public FGS findFieldByName(String name){
		name=name.toLowerCase();
		
		FGS fgs=hFieldsByColumnName.get(name);
		if(fgs==null){
			fgs=hFieldsByJavaName.get(name);
		}
		return fgs;
	}
	
	public Collection<FGS> fields(){
		return hFieldsByColumnName.values();
	}
		 
	public List<FGS> getPkFields(){
		List<FGS> pks=new ArrayList<FGS>();
		 
		for(Object o:fields()){
			FGS fgs=(FGS)o;
			Column c=fgs.getAnnotation(Column.class);
			if(c.key()){
				pks.add(fgs);
			}
		}
		return pks;
	}
	
	/**
	 * 复制对象数据
	 * @param model model to copy
	 * @return copy of the model
	 */
	public Model<?> copyModel(Model<?> model){
		try{
			Model<?> x=model.getClass().newInstance();
  
			x.holder().updateKey  = model.holder().updateKey;			
			x.holder().readonly   = model.holder().readonly;
			x.holder().dirty      = true;
			x.holder().entity     = false;
			 
			x.holder().fieldFilterExcludeMode=model.holder().fieldFilterExcludeMode;
			x.holder().fieldFilterSets.addAll(model.holder().fieldFilterSets);
			 
			for(FGS fgs:model.fields()){				
				Object value=fgs.getObject(model);
				fgs.setObject(x, value);
			}
			x.holder().changedFields.clear();
			x.holder().changedFields.addAll(model.holder().changedFields);
 			
			return x;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	  
	protected void doValidate(Model<?> model) {
		String validate=DbProp.PROP_TABLE_VALIDATE.getValue(db,tableName);
		
		if(validate.equalsIgnoreCase("true") || validate.equals("1")){			
			List<String> errors=validate(model);
			if(errors.size()>0){
				throw new RuntimeException(errors.toString());
			}
		}
	}
   
	
	/**
	 * 校验字段数据的是否合法.
	 * 
	 * @param model model for validate
	 * @return 不合法的字段列表{字段名: 错误信息}. 如果没有错误, 则为空列表.
	 */
	public List<String> validate(Model<?> model){
		if(validator==null){
			String clazz=DbProp.PROP_TABLE_VALIDATOR.getValue(db,tableName);
			
			if(clazz==null || clazz.trim().length()==0){
				validator=new Validator();
			}else{
				try{
					validator=(Validator)ClassHelper.forClassName(clazz.trim()).newInstance();
				}catch(Exception e){
					throw new RuntimeException(e);
				}
			}
		}
		
		return validator.validate(model);
	}
	  
	
	public List<ModelIndex> uniqueIndexes(){
		List<ModelIndex> unique=new ArrayList<ModelIndex>();
		for(ModelIndex index:indexes){
			if(index.isUnique()){
				unique.add(index);
			}
		}		  
		return unique;
	}
	
	
	boolean iChanged(){
		return changed;
	}
	 

	protected static FGS createFGS(final MetaColumn c,final FGS mfd){		 
		FGS fgs=new FGS(c.getJavaName(), mfd==null?null:mfd.getType()){
			
			public void setObject(Object bean,Object v){
				if(mfd!=null){
					mfd.setObject(bean, v);
				}else{
					if(bean instanceof Model<?>){
						Model<?> m=(Model<?>)bean;
						m.holder().set(c.getName(), v);							
					} 					
				}				
			}
			
			public Object getObject(Object bean){
				if(mfd!=null){
					return mfd.getObject(bean);
				}else{
					if(bean instanceof Model<?>){
						Model<?> m=(Model<?>)bean;
						return m.holder().get(c.getName());
					}else{
						return null;
					}
				}
			}
			
			public Field getField(){
				return mfd==null?null:mfd.getField();
			}
			
			@SuppressWarnings("unchecked")
			public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {				
				if(annotationClass==Column.class){
					return (T)createColumn(c);
				}else{
					return null;
				}
			}
		};
		
		return fgs;
	}
	
	protected static Column createColumn(final MetaColumn c){
		return new Column(){								 
			public Class<? extends Annotation> annotationType() {
				return Column.class;
			}
								 
			public String value() {							 
				return c.getValue();
			}
			
			 
			public String table() {							 
				return c.getTable().getName();
			}
			
			 
			public String remarks() {							 
				return c.getRemarks();
			}
									 
			public boolean notnull() {
				 
				return c.isNotnull();
			}
						 
			public String name() {
				return c.getName();
			}
			 
			public int length() {				 
				return c.getLength();
			}
			  
			public boolean key() {
				return c.isKey();
			}
			 
			public int jdbcType() {				 
				return c.getJdbcType();
			}
					 
			public boolean auto() {				 
				return c.isAuto();
			}
		};
	}
	
	public static Table createTable(final String tableName,final Table modelTable){
		return new Table(){  
			public String name() {					 
				return tableName;
			}
			
			public String value() {					 
				return tableName;
			}
	 			
			public String remarks() {
				return modelTable.remarks();
			}
			
			public String[] primaryKeys(){
				return modelTable.primaryKeys();
			}
			
			public Index[] indexes(){
				return modelTable.indexes();
			}
			
			public Class<? extends Annotation> annotationType() {
				return Table.class;
			}
		};
	}
	
	public static Table createTable(final String tableName,final String ...primaryKeys){
		Table tb=new Table(){ 
			public Class<? extends Annotation> annotationType() {
				return Table.class;
			}
			 
			public String value() {				 
				return tableName;
			}

			 
			public String name() {				 
				return tableName;
			}
			 
			public String remarks() {				 
				return "";
			}
 
			public String[] primaryKeys() {				 
				return primaryKeys;
			}
			 
			public Index[] indexes() {				 
				return new Index[0];
			}			
		};
		
		return tb;
	}
}