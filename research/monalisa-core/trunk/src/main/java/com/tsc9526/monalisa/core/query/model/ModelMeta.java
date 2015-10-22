package com.tsc9526.monalisa.core.query.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tsc9526.monalisa.core.annotation.Column;
import com.tsc9526.monalisa.core.annotation.DB;
import com.tsc9526.monalisa.core.annotation.Index;
import com.tsc9526.monalisa.core.annotation.Table;
import com.tsc9526.monalisa.core.datasource.DBConfig;
import com.tsc9526.monalisa.core.meta.MetaPartition;
import com.tsc9526.monalisa.core.query.dialect.Dialect;
import com.tsc9526.monalisa.core.query.validator.Validator;
import com.tsc9526.monalisa.core.tools.ClassHelper;
import com.tsc9526.monalisa.core.tools.ClassHelper.FGS;
import com.tsc9526.monalisa.core.tools.ClassHelper.MetaClass;

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
	
	
	
	ModelMeta(){		
	}
		
	synchronized void initModelMeta(Model<?> m){
		if(initialized){
			return;
		}
		
		this.model=m;
		Class<?> clazz=ClassHelper.findClassWithAnnotation(m.getClass(),DB.class);
		if(clazz==null){
			throw new RuntimeException("Model: "+m.getClass()+" must implement interface annotated by: "+DB.class);
		}
		
		table=m.getClass().getAnnotation(Table.class);	
		
		db=Model.dsm.getDBConfig(clazz);
		
		if(table==null){
			throw new RuntimeException("Model: "+m.getClass()+" must with a annotation: "+Table.class);
		}
	 
		dialect=Model.dsm.getDialect(db);
		
		mp=db.getPartition(table.name());			 
	  
		String ls=db.modelListener();
		if(ls!=null && ls.trim().length()>0){
			try{
				listener=(ModelListener)Class.forName(ls.trim()).newInstance();
			}catch(Exception e){
				throw new RuntimeException("Invalid model listener class: "+ls.trim()+", "+e,e);
			}
		}
		
		MetaClass metaClass=ClassHelper.getMetaClass(m.getClass());
		List<FGS> fields=metaClass.getFieldsWithAnnotation(Column.class);
		
		for(Object o:fields){
			FGS fgs=(FGS)o;				
			Column c=fgs.getField().getAnnotation(Column.class);
			
			hFieldsByColumnName.put(c.name().toLowerCase(),fgs);
			hFieldsByJavaName  .put(fgs.getFieldName().toLowerCase(),fgs);
			
			if(c.auto() && autoField==null){
				autoField=fgs;					 
			}
		}
		
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
		
		initialized=true;
	}
	
	public FGS findFieldByName(String name){
		name=name.toLowerCase();
		
		FGS fgs=hFieldsByColumnName.get(name);
		if(fgs==null){
			fgs=hFieldsByJavaName.get(name);
		}
		return fgs;
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
				Column c=fgs.getField().getAnnotation(Column.class);
				if(c.key()){
					fs.add(model.dialect().getColumnName(c.name()));
					 
				}
			}			
			
			if(fieldFilterExcludeMode){				
				for(FGS fgs:model.fields()){
					Column c=fgs.getField().getAnnotation(Column.class);
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
	 * 
	 
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
			Column column=fgs.getField().getAnnotation(Column.class);
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
	
	/**
	 * 复制对象数据
	 */
	public Model<?> copyModel(){
		try{
			Model<?> x=model.getClass().newInstance();
			
			for(FGS fgs:model.fields()){
				Object value=fgs.getObject(model);
				fgs.setObject(x, value);
			}
	 	 	
			x.modelMeta.updateKey  = updateKey;			
			x.modelMeta.tableName  = tableName;
			x.modelMeta.primaryKeys= primaryKeys;
			x.modelMeta.readonly   = readonly;
			x.modelMeta.dirty      = true;
			x.modelMeta.entity     = false;
			
			x.modelMeta.fieldFilterExcludeMode=fieldFilterExcludeMode;
			x.modelMeta.fieldFilterSets.addAll(fieldFilterSets);
			
			return x;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	  
	protected void doValidate() {
		String validate=db.getProperty("validate", "false");
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
			String clazz=db.getProperty("validator");
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