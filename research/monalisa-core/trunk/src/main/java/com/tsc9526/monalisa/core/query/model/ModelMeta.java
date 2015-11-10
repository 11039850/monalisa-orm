package com.tsc9526.monalisa.core.query.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.map.CaseInsensitiveMap;

import com.tsc9526.monalisa.core.annotation.Column;
import com.tsc9526.monalisa.core.annotation.DB;
import com.tsc9526.monalisa.core.annotation.Index;
import com.tsc9526.monalisa.core.annotation.Table;
import com.tsc9526.monalisa.core.datasource.DBConfig;
import com.tsc9526.monalisa.core.meta.MetaColumn;
import com.tsc9526.monalisa.core.meta.MetaPartition;
import com.tsc9526.monalisa.core.meta.MetaTable;
import com.tsc9526.monalisa.core.query.DbProp;
import com.tsc9526.monalisa.core.query.dialect.Dialect;
import com.tsc9526.monalisa.core.query.validator.Validator;
import com.tsc9526.monalisa.core.tools.ClassHelper;
import com.tsc9526.monalisa.core.tools.ClassHelper.FGS;
import com.tsc9526.monalisa.core.tools.ClassHelper.MetaClass;
import com.tsc9526.monalisa.core.tools.TableHelper;

class ModelMeta{
	protected boolean     fieldFilterExcludeMode=true;	
	protected Set<String> fieldFilterSets=new LinkedHashSet<String>();
	
	protected boolean     updateKey=false;
	protected boolean     readonly =false;
	
	protected boolean     dirty  = true;
	protected boolean     entity = false;
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
	 
	protected CaseInsensitiveMap hModelValues=null;
	
	//javaName
	protected Set<String> 		 changedFields=new LinkedHashSet<String>();
	 
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
		String ls=getProperty(DbProp.PROP_TABLE_MODEL_LISTENER, db.modelListener());		
		if(ls!=null && ls.trim().length()>0){
			try{
				listener=(ModelListener)Class.forName(ls.trim()).newInstance();
			}catch(Exception e){
				throw new RuntimeException("Invalid model listener class: "+ls.trim()+", "+e,e);
			}
		}		
	}
	
	protected void initPartioners(){
		mp=db.getPartition(table.name());
	}
	
	protected List<FGS> loadModelFields(){
		MetaClass metaClass=ClassHelper.getMetaClass(model.getClass());
		List<FGS> fields=metaClass.getFieldsWithAnnotation(Column.class);						
		if(fields.size()==0){
			if(hModelValues==null){
				hModelValues=new CaseInsensitiveMap();
			}
			
			loadFieldsFromDB(metaClass);
			
			fields=metaClass.getFieldsWithAnnotation(Column.class);
		}
	  
		return fields;		
	}
	
	protected void loadFieldsFromDB(MetaClass metaClass) {
		try{
			MetaTable mTable=TableHelper.getMetaTable(db, tableName);
			if(mTable!=null){				 
				List<FGS> fs=new ArrayList<FGS>();
				 
				for(MetaColumn c:mTable.getColumns()){
					FGS mfd=metaClass.getField(c.getJavaName());
					if(mfd==null){
						metaClass.getField(c.getName());
					}
					 
					FGS fgs=createFGS(c,mfd);	
					fs.add(fgs);								 
				}		
				
				metaClass.addFields(fs);
			}else{
				throw new RuntimeException("Table not found: "+tableName+", DB: "+db.key());
			}			
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
	
	
	protected FGS createFGS(final MetaColumn c,final FGS mfd){		 
		FGS fgs=new FGS(c.getJavaName(), mfd==null?null:mfd.getType()){
			
			public void setObject(Object bean,Object v){
				if(mfd!=null){
					mfd.setObject(bean, v);
				}else{
					hModelValues.put(c.getName(), v);
				}
			}
			
			public Object getObject(Object bean){
				if(mfd!=null){
					return mfd.getObject(bean);
				}else{
					return hModelValues.get(c.getName());
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
	
	protected Column createColumn(final MetaColumn c){
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
	
	protected Table createTable(final String tableName,final String ...primaryKeys){
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
	
	public Collection<FGS> changedFields(){
		Set<FGS> fields=new LinkedHashSet<FGS>();
		
		for(String name:changedFields){
			FGS fgs=findFieldByName(name);
			if(fgs!=null){
				fields.add(fgs);
			}
		}
		
		//Fill user defined fields without annotation: @Column
		for(FGS fgs:fields()){
			Field f=fgs.getField();
			if(f!=null && f.getAnnotation(Column.class)==null){
				fields.add(fgs);		
			}
		}
		
		return fields;
	}
	 
	/**
	 * 
	 * @return 逗号分隔的字段名， *： 表示返回所有字段
	 */
	public String filterFields(){
		if(fieldFilterSets.size()>0){	
			Set<String> fs=new LinkedHashSet<String>();			 
			//Add primary key
			for(FGS fgs:model.fields()){
				Column c=fgs.getAnnotation(Column.class);
				if(c.key()){
					fs.add(model.dialect().getColumnName(c.name()));
					 
				}
			}			
			
			if(fieldFilterExcludeMode){				
				for(FGS fgs:model.fields()){
					Column c=fgs.getAnnotation(Column.class);
					String f=model.dialect().getColumnName(c.name());
					if(fieldFilterSets.contains(f.toLowerCase()) == false && fs.contains(f)==false){
						fs.add(f);						
					}
				}								
			}else{
				for(String f:fieldFilterSets){
					f=model.dialect().getColumnName(f);
					if(fs.contains(f)==false){
						fs.add(f);
					}
				}
			}
			
			if(fs.size()>0){
				StringBuffer sb=new StringBuffer(); 
				for(String f:fs){
					if(sb.length()>0){
						sb.append(", ");
					}
					sb.append(f);
				}
				return sb.toString();
			}else {
				return "*";
			}
		}else{
			return "*";
		}
	}
	
	
	/**
	 * 排除表的某些字段。 用于在查询表时， 过滤掉某些不必要的字段
	 * 
	 * @param fields 要排除的字段名
	 * 
	 * @return  模型本身
	 */
	public void exclude(String ... fields){		 
		fieldFilterExcludeMode=true;
		
		if(fields==null || fields.length==0){
			fieldFilterSets.clear();
		}else{
			for(String f:fields){
				fieldFilterSets.add(model.dialect().getColumnName(f.toLowerCase()));
			}
		}		 
	}
		
	/**
	 * 排除大字段（字段长度 大于等于 #Short.MAX_VALUE)
	 */
	public void excludeBlobs(){
		excludeBlobs(Short.MAX_VALUE);
	}
	
	/**
	 *  排除超过指定长度的字段
	 * 
	 * @param maxLength  字段长度
	 * 
	 */
	public void excludeBlobs(int maxLength){
		List<String> es=new ArrayList<String>();
		
		for(FGS fgs:model.fields()){
			Column column=fgs.getAnnotation(Column.class);
			if(column.length()>=maxLength){
				es.add(model.dialect().getColumnName(column.name().toLowerCase()));
			}
		}
		exclude(es.toArray(new String[0]));
	}
	
	/**
	 * 只提取某些字段
	 * 
	 * @param fields  需要的字段名称	 
	 */
	public void include(String ... fields){		 
		fieldFilterExcludeMode=false;
		
		if(fields==null || fields.length==0){
			fieldFilterSets.clear();
		}else{
			for(String f:fields){
				fieldFilterSets.add(model.dialect().getColumnName(f.toLowerCase()));
			}
		}
		 
	}		
	
	public void fieldChanged(String fieldJavaName){
		if(!changedFields.contains(fieldJavaName)){
			changedFields.add(fieldJavaName);
		}
		
		dirty=true;
	}
	
	public void clearChanges(){
		changedFields.clear();
		
		dirty=false;
	}
	
	/**
	 * 复制对象数据
	 */
	public Model<?> copyModel(){
		try{
			Model<?> x=model.getClass().newInstance();
			
			if(hModelValues==null){
				for(FGS fgs:model.fields()){				
					Object value=fgs.getObject(model);
					fgs.setObject(x, value);
				}
			}else{
				x.modelMeta.hModelValues=new CaseInsensitiveMap();
				
				for(FGS fgs:model.fields()){				
					Object value=fgs.getObject(hModelValues);
					fgs.setObject(x.modelMeta.hModelValues, value);
				}
			}
	 	 	
			x.modelMeta.updateKey  = updateKey;			
			x.modelMeta.tableName  = tableName;
			x.modelMeta.primaryKeys= primaryKeys;
			x.modelMeta.readonly   = readonly;
			x.modelMeta.dirty      = true;
			x.modelMeta.entity     = false;
			
			x.modelMeta.fieldFilterExcludeMode=fieldFilterExcludeMode;
			x.modelMeta.fieldFilterSets.addAll(fieldFilterSets);
			x.modelMeta.changedFields.clear();
			x.modelMeta.changedFields.addAll(changedFields);
			
			return x;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	  
	protected void doValidate() {
		String validate=getProperty(DbProp.PROP_TABLE_VALIDATE, "false");		 
		if(validate.equalsIgnoreCase("true") || validate.equals("1")){			
			List<String> errors=validate();
			if(errors.size()>0){
				throw new RuntimeException(errors.toString());
			}
		}
	}
	
	protected String getProperty(String key,String defaultValue){
		String v=db.getProperty(key+"."+tableName);
		if(v==null){
			v=db.getProperty(key,defaultValue);
		}
		return v;
	}
	
	/**
	 * 校验字段数据的是否合法.
	 * 
	 *  @return 不合法的字段列表{字段名: 错误信息}. 如果没有错误, 则为空列表.
	 */
	public List<String> validate(){
		if(validator==null){
			String clazz=getProperty(DbProp.PROP_TABLE_VALIDATOR,"");
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
}