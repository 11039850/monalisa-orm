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
package test.com.tsc9526.monalisa.orm.query;

 
import java.sql.Types;
import java.util.Date;

import com.google.gson.JsonObject;
import com.tsc9526.monalisa.orm.annotation.Column;
import com.tsc9526.monalisa.orm.annotation.DB;
import com.tsc9526.monalisa.orm.annotation.Index;
import com.tsc9526.monalisa.orm.annotation.Table;
import com.tsc9526.monalisa.orm.criteria.QEH;
import com.tsc9526.monalisa.orm.datasource.DBConfig;
import com.tsc9526.monalisa.orm.model.Model;
import com.tsc9526.monalisa.tools.annotation.Alias;
import com.tsc9526.monalisa.tools.clazz.MelpClass;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Table(name="simple_model",
	primaryKeys={"auto"},
	remarks="",
	indexes={
		  @Index(name="PK_simple_model", type=1, unique=true, fields={"auto"}) 
		, @Index(name="UK_int_string",   type=1, unique=true, fields={"int_field1", "string_field1"}) 
	}
)
public class TestSimpleModel extends Model<TestSimpleModel> implements TestSimpleDB{
	private static final long serialVersionUID = 3625669623226866359L;

	@Column(auto=true,name="auto",key=true, notnull=true)
	private Integer auto;
	
	@Column(name="int_field1")
	@Alias("int_field1")
	private Integer intField1;
	
	@Column(name="int_field2")
	@Alias("int_field2")
	private Integer intField2;
	
	@Column(name="string_field1")
	@Alias("string_field1")
	private String stringField1;
	
	@Column(name="string_field2")
	@Alias("string_field2")
	private String stringField2;
	
	@Column(name="date_field1")
	@Alias("date_field1")
	private Date dateField1;
	
	@Column(name="date_field2")
	@Alias("date_field2")
	private Date dateField2;
	
	@Column(name="status")
	private StatusA status;
	
	@Column(name="status_b")
	@Alias("status_b")
	private StatusB statusB;
	
	@Column(name="status_c",jdbcType=Types.VARCHAR)
	@Alias("status_c")
	private StatusC statusC;
	
	
	@Column(name="array_1",jdbcType=Types.VARCHAR)
	@Alias("array_1")
	private String[] array1;
	 
	@Column(name="json_1",jdbcType=Types.VARCHAR)
	@Alias("json_1")
	private JsonObject json1;
	
	@Column(name="object_one",jdbcType=Types.VARCHAR)
	@Alias("object_one")
	private TestSimpleObject objectOne;
	
	@Column(name="object_two",jdbcType=Types.VARCHAR)
	@Alias("object_two")
	private TestSimpleObjectTwo objectTwo;
	
	public static Criteria createCriteria(){
		return new Example().createCriteria();
	}
	
		
	public static class Example extends com.tsc9526.monalisa.orm.criteria.Example<Criteria,TestSimpleModel>{
		public Example(){}
		 
		protected Criteria createInternal(){
			Criteria x= new Criteria(this);
			
			Class<?> clazz=MelpClass.findClassWithAnnotation(TestSimpleModel.class,DB.class);
			DBConfig db=dsm.getDBConfig(clazz);
			   
			QEH.getQuery(x).use(db);
			return x;
		}
	}
	
	public static class Criteria extends com.tsc9526.monalisa.orm.criteria.Criteria<Criteria>{
		
		private Example example;
		
		private Criteria(Example example){
			this.example=example;
		}
		
		public Example getExample(){
			return this.example;
		}
		
		public com.tsc9526.monalisa.orm.criteria.Field.FieldInteger<Criteria> intField1 = new com.tsc9526.monalisa.orm.criteria.Field.FieldInteger<Criteria>("int_field1", this);
		public com.tsc9526.monalisa.orm.criteria.Field.FieldInteger<Criteria> intField2 = new com.tsc9526.monalisa.orm.criteria.Field.FieldInteger<Criteria>("int_field1", this);
		public com.tsc9526.monalisa.orm.criteria.Field.FieldString<Criteria> stringField1 = new com.tsc9526.monalisa.orm.criteria.Field.FieldString<Criteria>("string_field1", this);
		public com.tsc9526.monalisa.orm.criteria.Field.FieldString<Criteria> stringField2 = new com.tsc9526.monalisa.orm.criteria.Field.FieldString<Criteria>("string_field2", this);
		public com.tsc9526.monalisa.orm.criteria.Field<Date,Criteria> dateField1 = new com.tsc9526.monalisa.orm.criteria.Field<Date,Criteria>("date_field1", this);
		public com.tsc9526.monalisa.orm.criteria.Field<Date,Criteria> dateField2 = new com.tsc9526.monalisa.orm.criteria.Field<Date,Criteria>("date_field2", this);
		public com.tsc9526.monalisa.orm.criteria.Field<StatusA,Criteria> status = new com.tsc9526.monalisa.orm.criteria.Field<StatusA,Criteria>("status", this);
		public com.tsc9526.monalisa.orm.criteria.Field<StatusB,Criteria> statusB = new com.tsc9526.monalisa.orm.criteria.Field<StatusB,Criteria>("status_b", this);
		public com.tsc9526.monalisa.orm.criteria.Field<StatusC,Criteria> statusC = new com.tsc9526.monalisa.orm.criteria.Field<StatusC,Criteria>("status_c", this);
		
		public com.tsc9526.monalisa.orm.criteria.Field<String[],Criteria> array1  = new com.tsc9526.monalisa.orm.criteria.Field<String[],Criteria>("array_1", this);
		public com.tsc9526.monalisa.orm.criteria.Field<JsonObject,Criteria> json1 = new com.tsc9526.monalisa.orm.criteria.Field<JsonObject,Criteria>("json_1", this);
	}

	public Integer getIntField1() {
		return intField1;
	}

	public TestSimpleModel setIntField1(Integer intField1) {
		this.intField1 = intField1;
		
		fieldChanged("intField1");
		return this;
	}

	public Integer getIntField2() {
		return intField2;
	}

	public TestSimpleModel setIntField2(Integer intField2) {
		this.intField2 = intField2;
		
		fieldChanged("intField2");
		return this;
	}

	public String getStringField1() {
		return stringField1;
	}

	public TestSimpleModel setStringField1(String stringField1) {
		this.stringField1 = stringField1;
		fieldChanged("stringField1");
		return this;
	}

	public String getStringField2() {
		return stringField2;
	}

	public TestSimpleModel setStringField2(String stringField2) {
		this.stringField2 = stringField2;
		fieldChanged("stringField2");
		return this;
	}

	public Date getDateField1() {
		return dateField1;
	}

	public TestSimpleModel setDateField1(Date dateField1) {
		this.dateField1 = dateField1;
		fieldChanged("dateField1");
		return this;
	}

	public Date getDateField2() {
		return dateField2;
	}

	public TestSimpleModel setDateField2(Date dateField2) {
		this.dateField2 = dateField2;
		fieldChanged("dateField2");
		return this;
	}

	public Integer getAuto() {
		return auto;
	}

	public TestSimpleModel setAuto(Integer auto) {
		this.auto = auto;
		fieldChanged("auto");
		return this;
	}

	public StatusA getStatus() {
		return status;
	}

	public TestSimpleModel setStatus(StatusA status) {
		this.status = status;
		fieldChanged("status");
		return this;
	}

	public StatusB getStatusB() {
		return statusB;
	}

	public TestSimpleModel setStatusB(StatusB statusB) {
		this.statusB = statusB;
		fieldChanged("statusB");
		return this;
	}
	
	public StatusC getStatusC() {
		return statusC;
	}

	public TestSimpleModel setStatusC(StatusC statusC) {
		this.statusC = statusC;
		fieldChanged("statusC");
		return this;
	}

	public String[] getArray1() {
		return array1;
	}

	public TestSimpleModel setArray1(String[] array1) {
		this.array1 = array1;
		fieldChanged("array1");
		return this;
	}

	public JsonObject getJson1() {
		return json1;
	}

	public TestSimpleModel setJson1(JsonObject json1) {
		this.json1 = json1;
		fieldChanged("json1");
		return this;
	}

	public TestSimpleObject getObjectOne() {
		return objectOne;
	}

	public TestSimpleModel setObjectOne(TestSimpleObject objectOne) {
		this.objectOne = objectOne;
		fieldChanged("objectOne");
		return this;
	}

	public TestSimpleObjectTwo getObjectTwo() {
		return objectTwo;
	}

	public TestSimpleModel setObjectTwo(TestSimpleObjectTwo objectTwo) {
		this.objectTwo = objectTwo;
		fieldChanged("objectTwo");
		return this;
	}
	
}
