package com.tsc9526.monalisa.core.query.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.tsc9526.monalisa.core.annotation.Column;
import com.tsc9526.monalisa.core.annotation.DB;
import com.tsc9526.monalisa.core.annotation.Table;
import com.tsc9526.monalisa.core.datasource.DBConfig;
import com.tsc9526.monalisa.core.datasource.DataSourceManager;
import com.tsc9526.monalisa.core.query.dialect.Dialect;
import com.tsc9526.monalisa.core.query.partition.CreateTableCache;
import com.tsc9526.monalisa.core.query.partition.Partition;
import com.tsc9526.monalisa.core.query.validator.Validator;
import com.tsc9526.monalisa.core.tools.ClassHelper;
import com.tsc9526.monalisa.core.tools.ClassHelper.FGS;
import com.tsc9526.monalisa.core.tools.ClassHelper.MetaClass;
import com.tsc9526.monalisa.core.tools.ModelParseHelper;

/**
 * 数据库表模型
 * 
 * 
 * @author zzg
 * 
 */
@SuppressWarnings({"rawtypes","unchecked"})
public abstract class Model<T extends Model> implements Serializable{
	private static final long serialVersionUID = 703976566431364670L;

	protected static DataSourceManager dsm=DataSourceManager.getInstance();
	
	protected DBConfig db;
 
	protected boolean     fieldFilterExcludeMode=true;	
	protected Set<String> fieldFilterSets=new LinkedHashSet<String>();
		 
	protected MetaClass metaClass;
	protected List<FGS> fields;	 
	protected Table table;	
 	protected FGS   autoField;
 	protected boolean updateKey=false;
 	
 	protected Partition partition;
 	protected ModelListener	modelListener;
 	
 	protected boolean readonly=false;
 	
	public Model(){
		Class<?> clazz=ClassHelper.findClassWithAnnotation(this.getClass(),DB.class);
		if(clazz==null){
			throw new RuntimeException("Model: "+this.getClass()+" must implement interface annotated by: "+DB.class);
		}
		
		this.table=this.getClass().getAnnotation(Table.class);
		
		this.db=dsm.getDBConfig(clazz);
		
		if(this.table==null){
			throw new RuntimeException("Model: "+this.getClass()+" must with a annotation: "+Table.class);
		}
	 
		String ls=this.db.modelListener();
		if(ls!=null && ls.trim().length()>0){
			try{
				modelListener=(ModelListener)Class.forName(ls.trim()).newInstance();
			}catch(Exception e){
				throw new RuntimeException("Invalid model listener class: "+ls.trim()+", "+e,e);
			}
		}
		
		this.metaClass=ClassHelper.getMetaClass(this.getClass());
		this.fields=metaClass.getFieldsWithAnnotation(Column.class);
		
		for(Object o:this.fields()){
			FGS fgs=(FGS)o;
			
			Column c=fgs.getField().getAnnotation(Column.class);
			if(c.auto()){
				autoField=fgs;
				break;
			}
		}
	}	 
 
	/**
	 * @see com.tsc9526.monalisa.core.tools.ModelParseHelper#parseModel(Model, Object)
	 * 
	 * @param dataObject 
	 * @param mappings  [Options] Translate dataObject field to model field for example:  "user_id=id","user_name=name" ...
	 */
	public T parse(Object dataObject,String... mappings) {
		ModelParseHelper.parseModel(this, dataObject,mappings);
		
		return (T)this;
	}
	
	
	/**
	 * 存储对象到数据库
	 * 
	 * @return 成功变更的记录数
	 */
	public int save(){
		if(modelListener!=null){
			int r=-1;
			try{
				modelListener.before(ModelEvent.INSERT, this);
				doValidate();				
		 		r= new Insert(this).insertSelective();
		 		return r;
			}finally{
				modelListener.after(ModelEvent.INSERT, this,r);
			}
		}else{
			return new Insert(this).insertSelective();
		}
		
		
	}
	
	/**
	 * 存储对象到数据库， 如果主键冲突， 则执行更新操作
	 * 
	 * @return 成功变更的记录数
	 */
	public int saveOrUpdate(){
		if(modelListener!=null){
			int r=-1;
			try{
				modelListener.before(ModelEvent.INSERT_OR_UPDATE, this);
				doValidate();
				r= new Insert(this).insertSelective(true);
				return r;
			}finally{
				modelListener.after(ModelEvent.INSERT_OR_UPDATE, this,r);
			}
		}else{
			return new Insert(this).insertSelective(true);
		} 
	}
	
	/**
	 * 更新对象到数据库
	 * 
	 * @return 成功变更的记录数
	 */
	public int update(){
		if(modelListener!=null){
			int r=-1;		
			try{
				modelListener.before(ModelEvent.UPDATE, this);	
				doValidate();
				r= new Update(this).update();
				return r;
			}finally{
				modelListener.after(ModelEvent.UPDATE, this,r);
			}
		}else{
			return new Update(this).update();
		}		 
	}
	
	/**
	 * 从数据库删除该记录
	 * 
	 * @return 成功变更的记录数
	 */
	public int delete(){
		if(modelListener!=null){
			int r=-1;
			try{
				modelListener.before(ModelEvent.DELETE, this);
				r= new Delete(this).delete();
				return r;
			}finally{
				modelListener.after(ModelEvent.DELETE, this,r);
			}
		}else{
			return new Delete(this).delete();
		}		 
	}
	
	public Select<T> select(){
		return new Select(this);
	}
	
	protected Dialect dialect;
	/**
	 * 
	 * @return 返回数据库方言
	 */
	public Dialect getDialect(){
		if(dialect==null){
			dialect=dsm.getDialect(this.db);
		}
		
		return dialect;
	}
	
	/**
	 * 
	 * @return 返回表的字段列表
	 */
	public List<FGS> fields() {
		return this.fields;
	}		
	
	/**
	 * How to update the model's primary key:<br>
	 * <code>
	 *   Model model=new Model(oldId).load(); <br>
	 *   model.setId(newId); <br>
	 *   model.setXxx ... <br>
	 *   <br>
	 *   Update update=new Update(model,true); <br>
	 *   update.update("id",oldId);<br>
	 * </code>
	 * 
	 * @return Enable update the model's primary key, default is: false
	 */
	public boolean enableUpdateKey(){
		return updateKey;
	}
	
	void enableUpdateKey(boolean updateKey){
		this.updateKey=updateKey;
	}
	
	/**
	 * 
	 * @return 返回表名等信息
	 */
	public Table table(){
		if(partition!=null){			 
			return CreateTableCache.getTable(partition, this, this.table);
		}else{
			return this.table;
		}
	}
	 
	/**
	 * 
	 * @return 返回表的自增字段， 如果没有则返回null
	 */
	public FGS autoField(){ 
		return autoField;
	}
	
	/**
	 * 
	 * @return 数据库连接信息
	 */
	public DBConfig db(){
		return this.db;
	}
	
	
	public boolean readonly(){
		return readonly;
	}
	
	void readonly(boolean readonly){
		this.readonly=readonly;		
	}
	
	
	/**
	 * 
	 * @return 逗号分隔的字段名， *： 表示返回所有字段
	 */
	public String filterFields(){
		if(fieldFilterSets.size()>0){	
			Set<String> fs=new LinkedHashSet<String>();			 
			//Add primary key
			for(FGS fgs:fields){
				Column c=fgs.getField().getAnnotation(Column.class);
				if(c.key()){
					fs.add(getDialect().getColumnName(c.name()));
					 
				}
			}			
			
			if(fieldFilterExcludeMode){				
				for(FGS fgs:fields){
					Column c=fgs.getField().getAnnotation(Column.class);
					String f=getDialect().getColumnName(c.name());
					if(fieldFilterSets.contains(f.toLowerCase()) == false && fs.contains(f)==false){
						fs.add(f);						
					}
				}								
			}else{
				for(String f:fieldFilterSets){
					f=getDialect().getColumnName(f);
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
	public T exclude(String ... fields){		 
		fieldFilterExcludeMode=true;
		
		if(fields==null || fields.length==0){
			fieldFilterSets.clear();
		}else{
			for(String f:fields){
				fieldFilterSets.add(getDialect().getColumnName(f.toLowerCase()));
			}
		}
		
		return (T)this;
	}
		
	/**
	 * 排除大字段（字段长度 大于等于 #Short.MAX_VALUE)
	 * 
	 * @return 模型本身
	 */
	public T excludeBlobs(){
		return excludeBlobs(Short.MAX_VALUE);
	}
	
	/**
	 *  排除超过指定长度的字段
	 * 
	 * @param maxLength  字段长度
	 * 
	 * @return 模型本身
	 */
	public T excludeBlobs(int maxLength){
		List<String> es=new ArrayList<String>();
		
		for(FGS fgs:fields){
			Column column=fgs.getField().getAnnotation(Column.class);
			if(column.length()>=maxLength){
				es.add(getDialect().getColumnName(column.name().toLowerCase()));
			}
		}
		return exclude(es.toArray(new String[0]));
	}
	
	/**
	 * 只提取某些字段
	 * 
	 * @param fields  需要的字段名称
	 * @return 模型本身
	 */
	public T include(String ... fields){		 
		fieldFilterExcludeMode=false;
		
		if(fields==null || fields.length==0){
			fieldFilterSets.clear();
		}else{
			for(String f:fields){
				fieldFilterSets.add(getDialect().getColumnName(f.toLowerCase()));
			}
		}
		
		return (T)this;
	}		
	
	/**
	 * 复制对象数据
	 */
	public T copy(){
		try{
			T x=(T)this.getClass().newInstance();
			
			for(FGS fgs:fields){
				Object value=fgs.getObject(this);
				fgs.setObject(x, value);
			}
	 	 	
			x.updateKey=updateKey;
			x.fieldFilterExcludeMode=fieldFilterExcludeMode;
			x.fieldFilterSets.addAll(fieldFilterSets);
			
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
	
	protected Validator validator=null;
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
		return new Validator().validate(this);
	}
}

