package com.tsc9526.monalisa.core.query.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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
import com.tsc9526.monalisa.core.datasource.DataSourceManager;
import com.tsc9526.monalisa.core.meta.MetaPartition;
import com.tsc9526.monalisa.core.query.dialect.Dialect;
import com.tsc9526.monalisa.core.query.partition.CreateTableCache;
import com.tsc9526.monalisa.core.query.validator.Validator;
import com.tsc9526.monalisa.core.tools.ClassHelper;
import com.tsc9526.monalisa.core.tools.ClassHelper.FGS;
import com.tsc9526.monalisa.core.tools.ClassHelper.MetaClass;
import com.tsc9526.monalisa.core.tools.ModelHelper;

/**
 * 数据库表模型
 * 
 * 
 * @author zzg.zhou(11039850@qq.com)
 * 
 */
@SuppressWarnings({"rawtypes","unchecked"})
public abstract class Model<T extends Model> implements Serializable{
	private static final long serialVersionUID = 703976566431364670L;

	protected static DataSourceManager dsm=DataSourceManager.getInstance();
	 
	protected boolean     fieldFilterExcludeMode=true;	
	protected Set<String> fieldFilterSets=new LinkedHashSet<String>();
	
	protected boolean     updateKey=false;
	protected boolean     readonly =false;
	
	private transient ModelMeta modelMeta;
	  	
	public Model(){	
		
	}	 
 
	protected ModelMeta mm() {
		if(modelMeta==null){
			synchronized (this) {
				if(modelMeta==null){
					try{
						modelMeta=new ModelMeta(this);
					}catch(Exception e){
						throw new RuntimeException(e);
					}
				}
			}
		}
		return modelMeta;
	}
	
	/**
	 * @see com.tsc9526.monalisa.core.tools.ModelHelper#parseModel(Model, Object)
	 * 
	 * @param dataObject HttpServletRequest, Map, JsonObject, String(json format), JavaBean
	 * @param mappings  [Options] Translate dataObject field to model field <br>
	 * For example: <br> 
	 * "user_id=id", ... // Parse dataObject.user_id to Model.id<br>
	 * Another example:<br>
	 * "~XXX"  //Only parse the fields with prefix: XXX 
	 * 
	 */
	public T parse(Object dataObject,String... mappings) {
		ModelHelper.parse(this, dataObject,mappings);
		
		return (T)this;
	}
	
	/**
	 * 保存对象到数据库,忽略该对象中值为null的字段
	 */
	public int save(){
		return save(true);
	}
	
	/**
	 * 保存或更新对象到数据库,忽略该对象中值为null的字段
	 */
	public int saveOrUpdate(){
		return saveOrUpdate(true);
	}
	
	/**
	 * 更新对象到数据库,忽略该对象中值为null的字段
	 */
	public int update(){
		return update(true);
	}	
	
	
	
	/**
	 * 存储对象到数据库
	 * 
	 * @param selective
	 * true-更新时忽略值为null的字段, false-存储所有字段(包括null)到数据库
	 * 
	 * @return 成功变更的记录数
	 */
	public int save(boolean selective){
		int r=-1;
		try{
			before(Event.INSERT);
			
			doValidate();
			
			if(selective){
				r= new Insert(this).insertSelective();
			}else{
				r= new Insert(this).insert();
			}
			
			return r;
		}finally{
			after(Event.INSERT, r);
		} 			 		
	}
	
	/**
	 * 存储对象到数据库， 如果主键冲突， 则执行更新操作
	 * 
	 * @param selective
	 * true-更新时忽略值为null的字段, false-存储所有字段(包括null)到数据库
	 * 
	 * @return 成功变更的记录数
	 */
	public int saveOrUpdate(boolean selective){
		int r=-1;
		try{
			before(Event.INSERT_OR_UPDATE);
			
			doValidate();
			
			if(selective){
				r= new Insert(this).insertSelective(true);
			}else{
				r= new Insert(this).insert(true);
			}
			
			return r;
		}finally{
			after(Event.INSERT_OR_UPDATE, r);
		} 			 
	}
	
	/**
	 * 更新对象到数据库
	 *  
	 * @param selective
	 * true-更新时忽略值为null的字段, false-存储所有字段(包括null)到数据库
	 * 
	 * @return 成功变更的记录数
	 */
	public int update(boolean selective){
		int r=-1;
		try{
			before(Event.UPDATE);
			
			doValidate();
			
			if(selective){
				r= new Update(this).updateSelective();
			}else{
				r= new Update(this).update();
			}
			
			return r;
		}finally{
			after(Event.UPDATE, r);
		} 		 		
	}
	 
	/**
	 * 从数据库删除该记录
	 * 
	 * @return 成功变更的记录数
	 */
	public int delete(){
		int r=-1;
		try{
			before(Event.DELETE);
			r= new Delete(this).delete();
			return r;
		}finally{
			after(Event.DELETE, r);
		} 
	}
	
	protected void before(Event event){
		if(mm().listener!=null){
			mm().listener.before(event, this);
		}
	}
	
	protected void after(Event event, int r) {
		if(mm().listener!=null){
			mm().listener.after(event, this,r);
		}
	}
	 
	/**
	 * 设置所有字段为null
	 * 
	 * @return 
	 */
	public T clear(){
		for(FGS fgs:fields()){
			fgs.setObject(this,null);
		}
		
		return (T)this;
	}
	
	
	/**
	 * 
	 * @return 返回数据库方言
	 */
	public Dialect getDialect(){		 		
		return mm().dialect;
	}
	
	/**
	 * 
	 * @return 返回表的字段列表
	 */
	public Collection<FGS> fields() {		 
		return mm().hFieldsByColumnName.values();
	}	
	
	public FGS field(String name) {
		return mm().findFieldByName(name);		 
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
	
	public T set(String name,Object value){
		FGS fgs=field(name);
		if(fgs!=null){
			fgs.setObject(this, value);			
		}		
		return (T)this;
	}
	
	/**
	 * 
	 * @return 返回表名等信息
	 */
	public Table table(){
		if(mm().mp!=null){			 
			return CreateTableCache.getTable(mm().mp, this, mm().table);
		}else{
			return mm().table;
		}
	}
	 
	/**
	 * 
	 * @return 返回表的自增字段， 如果没有则返回null
	 */
	public FGS autoField(){ 		
		return mm().autoField;
	}
	
	/**
	 * 
	 * @return 数据库连接信息
	 */
	public DBConfig db(){
		return mm().db;
	}
	
	/**
	 * 设置访问数据库
	 * 
	 * @param db
	 * @return
	 */
	public T use(DBConfig db){
		mm().db=db;
		
		return (T)this;
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
			for(FGS fgs:fields()){
				Column c=fgs.getField().getAnnotation(Column.class);
				if(c.key()){
					fs.add(getDialect().getColumnName(c.name()));
					 
				}
			}			
			
			if(fieldFilterExcludeMode){				
				for(FGS fgs:fields()){
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
		
		for(FGS fgs:fields()){
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
			
			for(FGS fgs:fields()){
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
		String validate=mm().db.getProperty("validate", "false");
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
			String clazz=mm().db.getProperty("validator");
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
		
		return validator.validate(this);
	}
	
	public List<ModelIndex> getIndexes(){
		return mm().indexes;
	}
	
	public List<ModelIndex> getUniqueIndexes(){
		List<ModelIndex> unique=new ArrayList<Model.ModelIndex>();
		for(ModelIndex index:mm().indexes){
			if(index.isUnique()){
				unique.add(index);
			}
		}		  
		return unique;
	}
	 
	public static enum Event {
		INSERT,DELETE,UPDATE,INSERT_OR_UPDATE,LOAD;
	}
	
	public Listener getListener(){
		return mm().listener;
	}
 
	
	public static interface Listener {
		public void before(Event event, Model model);
		public void after (Event event, Model model,int result);
	}
	
	public static interface Parser<T> { 
		public boolean parse(Model<?> m,T data,String... mappings);
	}

	public static class ModelIndex{
		/**
		 * 索引名
		 */
		private String name;
		
		/**
		 * 索引类型
		 */
		private int      type;
		
		/**
		 * 是否唯一性索引
		 */
		private boolean  unique;
		
		/**
		 * 索引字段
		 */
		private List<FGS> fields;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public boolean isUnique() {
			return unique;
		}

		public void setUnique(boolean unique) {
			this.unique = unique;
		}

		public List<FGS> getFields() {
			return fields;
		}

		public void setFields(List<FGS> fields) {
			this.fields = fields;
		}
		
	}
	
	protected static class ModelMeta{
		protected Dialect dialect;
		protected DBConfig  db;
		protected Map<String,FGS> hFieldsByColumnName=new LinkedHashMap<String, ClassHelper.FGS>();
		protected Map<String,FGS> hFieldsByJavaName  =new LinkedHashMap<String, ClassHelper.FGS>();
		protected FGS       autoField;	
		protected Table     table;
		protected MetaPartition mp;		 
		protected Listener  listener;
		protected List<ModelIndex> indexes=new ArrayList<Model.ModelIndex>();
		
		ModelMeta(Model m)throws Exception{
			Class<?> clazz=ClassHelper.findClassWithAnnotation(m.getClass(),DB.class);
			if(clazz==null){
				throw new RuntimeException("Model: "+m.getClass()+" must implement interface annotated by: "+DB.class);
			}
			
			table=m.getClass().getAnnotation(Table.class);	
			
			db=dsm.getDBConfig(clazz);
			
			if(table==null){
				throw new RuntimeException("Model: "+m.getClass()+" must with a annotation: "+Table.class);
			}
		 
			dialect=dsm.getDialect(db);
			
			mp=db.getPartition(table.name());			 
		  
			String ls=db.modelListener();
			if(ls!=null && ls.trim().length()>0){
				try{
					listener=(Listener)Class.forName(ls.trim()).newInstance();
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
		}
		
		public FGS findFieldByName(String name){
			name=name.toLowerCase();
			
			FGS fgs=hFieldsByColumnName.get(name);
			if(fgs==null){
				fgs=hFieldsByJavaName.get(name);
			}
			return fgs;
		}
	}	
}

