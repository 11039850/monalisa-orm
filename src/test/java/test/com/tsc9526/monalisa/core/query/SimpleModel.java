package test.com.tsc9526.monalisa.core.query;

 
import java.util.Date;

import com.tsc9526.monalisa.core.annotation.Column;
import com.tsc9526.monalisa.core.annotation.DB;
import com.tsc9526.monalisa.core.annotation.Table;
import com.tsc9526.monalisa.core.datasource.DBConfig;
import com.tsc9526.monalisa.core.query.dao.Model;
import com.tsc9526.monalisa.core.tools.ClassHelper;

@Table(name="simple_model")
public class SimpleModel extends Model<SimpleModel> implements SimpleDB{
	private static final long serialVersionUID = 3625669623226866359L;

	@Column(auto=true,name="auto",key=true, notnull=true)
	private Integer auto;
	
	@Column(name="int_field1")
	private Integer intField1;
	
	@Column(name="int_field2")
	private Integer intField2;
	
	@Column(name="string_field1")
	private String stringField1;
	
	@Column(name="string_field2")
	private String stringField2;
	
	@Column(name="date_field1")
	private Date dateField1;
	
	@Column(name="date_field2")
	private Date dateField2;
	
	@Column(name="status")
	private StatusA status;
	
	@Column(name="status_b")
	private StatusB statusB;
	
	public static Criteria createCriteria(){
		return new Example(new SimpleModel()).createCriteria();
	}
	
		
	public static class Example extends com.tsc9526.monalisa.core.query.criteria.Example<Criteria,SimpleModel>{
		public Example(){}
		public Example(SimpleModel model) {
			super(model);
		}
		protected Criteria createInternal(){
			Criteria x= new Criteria(this);
			
			Class<?> clazz=ClassHelper.findClassWithAnnotation(SimpleModel.class,DB.class);
			DBConfig db=dsm.getDBConfig(clazz);
			   
			x.getQuery().use(db);
			return x;
		}
	}
	
	public static class Criteria extends com.tsc9526.monalisa.core.query.criteria.Criteria{
		
		private Example example;
		
		private Criteria(Example example){
			this.example=example;
		}
		
		public Example getExample(){
			return this.example;
		}
		
		public com.tsc9526.monalisa.core.query.criteria.Field.FieldInteger<Criteria> intField1 = new com.tsc9526.monalisa.core.query.criteria.Field.FieldInteger<Criteria>("int_field1", this);
		public com.tsc9526.monalisa.core.query.criteria.Field.FieldInteger<Criteria> intField2 = new com.tsc9526.monalisa.core.query.criteria.Field.FieldInteger<Criteria>("int_field1", this);
		public com.tsc9526.monalisa.core.query.criteria.Field.FieldString<Criteria> stringField1 = new com.tsc9526.monalisa.core.query.criteria.Field.FieldString<Criteria>("string_field1", this);
		public com.tsc9526.monalisa.core.query.criteria.Field.FieldString<Criteria> stringField2 = new com.tsc9526.monalisa.core.query.criteria.Field.FieldString<Criteria>("string_field2", this);
		public com.tsc9526.monalisa.core.query.criteria.Field<Date,Criteria> dateField1 = new com.tsc9526.monalisa.core.query.criteria.Field<Date,Criteria>("date_field1", this);
		public com.tsc9526.monalisa.core.query.criteria.Field<Date,Criteria> dateField2 = new com.tsc9526.monalisa.core.query.criteria.Field<Date,Criteria>("date_field2", this);
		public com.tsc9526.monalisa.core.query.criteria.Field<StatusA,Criteria> status = new com.tsc9526.monalisa.core.query.criteria.Field<StatusA,Criteria>("status", this);
		public com.tsc9526.monalisa.core.query.criteria.Field<StatusB,Criteria> statusB = new com.tsc9526.monalisa.core.query.criteria.Field<StatusB,Criteria>("status_b", this);
	}

	public Integer getIntField1() {
		return intField1;
	}

	public void setIntField1(Integer intField1) {
		this.intField1 = intField1;
	}

	public Integer getIntField2() {
		return intField2;
	}

	public void setIntField2(Integer intField2) {
		this.intField2 = intField2;
	}

	public String getStringField1() {
		return stringField1;
	}

	public void setStringField1(String stringField1) {
		this.stringField1 = stringField1;
	}

	public String getStringField2() {
		return stringField2;
	}

	public void setStringField2(String stringField2) {
		this.stringField2 = stringField2;
	}

	public Date getDateField1() {
		return dateField1;
	}

	public void setDateField1(Date dateField1) {
		this.dateField1 = dateField1;
	}

	public Date getDateField2() {
		return dateField2;
	}

	public void setDateField2(Date dateField2) {
		this.dateField2 = dateField2;
	}

	public Integer getAuto() {
		return auto;
	}

	public void setAuto(Integer auto) {
		this.auto = auto;
	}

	public StatusA getStatus() {
		return status;
	}

	public void setStatus(StatusA status) {
		this.status = status;
	}

	public StatusB getStatusB() {
		return statusB;
	}

	public void setStatusB(StatusB statusB) {
		this.statusB = statusB;
	}
	
}
