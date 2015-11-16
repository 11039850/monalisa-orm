package com.tsc9526.monalisa.core.query.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.tsc9526.monalisa.core.annotation.Column;
import com.tsc9526.monalisa.core.annotation.DB;
import com.tsc9526.monalisa.core.annotation.Index;
import com.tsc9526.monalisa.core.annotation.Table;
import com.tsc9526.monalisa.core.datasource.DBConfig;
import com.tsc9526.monalisa.core.datasource.DbProp;
import com.tsc9526.monalisa.core.meta.MetaColumn;
import com.tsc9526.monalisa.core.meta.MetaPartition;
import com.tsc9526.monalisa.core.meta.MetaTable;
import com.tsc9526.monalisa.core.query.dialect.Dialect;
import com.tsc9526.monalisa.core.query.validator.Validator;
import com.tsc9526.monalisa.core.tools.ClassHelper;
import com.tsc9526.monalisa.core.tools.ClassHelper.FGS;
import com.tsc9526.monalisa.core.tools.ClassHelper.MetaClass;
import com.tsc9526.monalisa.core.tools.TableHelper;

class ModelMeta{
	protected String      tableName= null;
	protected String[]    primaryKeys= null;
	protected Validator   validator=null;
	
	protected boolean initialized=false;
	protected Model<?>  model;
	
	protected DBConfig  db;
	protected Dialect   dialect;
	protected FGS       autoField;	
	protected Table     table;
	
	protected MetaPartition    mp;		 
	protected ModelListener    listener;
	protected List<ModelIndex> indexes=new ArrayList<ModelIndex>();
	
	protected Map<String,FGS> hFieldsByColumnName=new LinkedHashMap<String, ClassHelper.FGS>();
	protected Map<String,FGS> hFieldsByJavaName  =new LinkedHashMap<String, ClassHelper.FGS>();
	  
	ModelMeta(){
	}
 
	synchronized void initModelMeta(Model<?> m){
		if(initialized){
			return;
		}
		  	
		this.model=m;
		 
		initDB();
		
		initTable();
				
		initFields();
		
		initIndexes();
		
		initListeners();
		
		initPartioners();			 
		 
		initialized=true;
	}
	
	protected void initDB() {
		if(db==null){
			Class<?> clazz=ClassHelper.findClassWithAnnotation(model.getClass(),DB.class);
			if(clazz==null){
				throw new RuntimeException("Model: "+model.getClass()+" must implement interface annotated by: "+DB.class);
			}
			db=Model.dsm.getDBConfig(clazz);
		}
		
		dialect=Model.dsm.getDialect(db);
	}
	
	protected void initTable() {
		table=model.getClass().getAnnotation(Table.class);			  
		if(table==null){
			if(tableName==null || tableName.trim().length()==0){
				tableName=model.getClass().getSimpleName();
			}
			
			table=createTable(tableName,primaryKeys);
		}
	}
	
	protected void initFields(){
		List<FGS> fields=loadModelFields();				
		for(Object o:fields){
			FGS fgs=(FGS)o;				
			Column c=fgs.getAnnotation(Column.class);
			
			hFieldsByColumnName.put(c.name().toLowerCase(),fgs);
			hFieldsByJavaName  .put(fgs.getFieldName().toLowerCase(),fgs);
			
			if(c.auto() && autoField==null){
				autoField=fgs;					 
			}
		}
	}
	
	protected void initIndexes() {
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
	
	protected void initListeners(){
		String ls=DbProp.PROP_TABLE_MODEL_LISTENER.getValue(db,tableName);
		
		if(ls==null){
			ls=db.getCfg().getModelListener();
		}
		
		if(ls!=null && ls.trim().length()>0){
			try{
				listener=(ModelListener)Class.forName(ls.trim()).newInstance();
			}catch(Exception e){
				throw new RuntimeException("Invalid model listener class: "+ls.trim()+", "+e,e);
			}
		}		
	}
	
	protected void initPartioners(){
		mp=db.getCfg().getPartition(table.name());
	}
	
	protected List<FGS> loadModelFields(){
		MetaClass metaClass=ClassHelper.getMetaClass(model.getClass());
		List<FGS> fields=metaClass.getFieldsWithAnnotation(Column.class);						
		if(fields.size()==0){
			fields=loadFieldsFromDB(metaClass);			 
		}
		
		return fields;		
	}
	
	protected List<FGS> loadFieldsFromDB(MetaClass metaClass) {
		try{
			MetaTable mTable=TableHelper.getMetaTable(db, tableName);
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
				
				metaClass.replaceFields(fs);
				
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
		 
	/**
	 * 复制对象数据
	 */
	public Model<?> copyModel(){
		try{
			Model<?> x=model.getClass().newInstance();
			x.modelMeta.tableName  = tableName;
			x.modelMeta.primaryKeys= primaryKeys;
			
			x.modelHolder.updateKey  = model.modelHolder.updateKey;			
			x.modelHolder.readonly   = model.modelHolder.readonly;
			x.modelHolder.dirty      = true;
			x.modelHolder.entity     = false;
			
			x.modelMeta.model=x;
			x.mm();
			
			x.modelHolder.fieldFilterExcludeMode=model.modelHolder.fieldFilterExcludeMode;
			x.modelHolder.fieldFilterSets.addAll(model.modelHolder.fieldFilterSets);
			 
			for(FGS fgs:model.fields()){				
				Object value=fgs.getObject(model);
				fgs.setObject(x, value);
			}
			x.modelHolder.changedFields.clear();
			x.modelHolder.changedFields.addAll(model.modelHolder.changedFields);
 			
			return x;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	  
	protected void doValidate() {
		String validate=DbProp.PROP_TABLE_VALIDATE.getValue(db,tableName);
		
		if(validate.equalsIgnoreCase("true") || validate.equals("1")){			
			List<String> errors=validate();
			if(errors.size()>0){
				throw new RuntimeException(errors.toString());
			}
		}
	}
   
	
	/**
	 * 校验字段数据的是否合法.
	 * 
	 *  @return 不合法的字段列表{字段名: 错误信息}. 如果没有错误, 则为空列表.
	 */
	public List<String> validate(){
		if(validator==null){
			String clazz=DbProp.PROP_TABLE_VALIDATOR.getValue(db,tableName);
			
			if(clazz==null || clazz.trim().length()==0){
				validator=new Validator();
			}else{
				try{
					validator=(Validator)Class.forName(clazz.trim()).newInstance();
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
	
	 

	protected static FGS createFGS(final MetaColumn c,final FGS mfd){		 
		FGS fgs=new FGS(c.getJavaName(), mfd==null?null:mfd.getType()){
			
			public void setObject(Object bean,Object v){
				if(mfd!=null){
					mfd.setObject(bean, v);
				}else{
					if(bean instanceof Model<?>){
						Model<?> m=(Model<?>)bean;
						m.modelHolder.set(c.getName(), v);						 
					} 					
				}
			}
			
			public Object getObject(Object bean){
				if(mfd!=null){
					return mfd.getObject(bean);
				}else{
					if(bean instanceof Model<?>){
						Model<?> m=(Model<?>)bean;
						return m.modelHolder.get(c.getName());
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
	
	protected static Table createTable(final String tableName,final String ...primaryKeys){
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