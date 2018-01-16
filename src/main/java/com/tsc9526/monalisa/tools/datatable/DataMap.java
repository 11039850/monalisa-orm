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
package com.tsc9526.monalisa.tools.datatable;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.tsc9526.monalisa.orm.model.Model;
import com.tsc9526.monalisa.tools.clazz.MelpClass;
import com.tsc9526.monalisa.tools.clazz.MelpClass.ClassHelper;
import com.tsc9526.monalisa.tools.clazz.MelpClass.FGS;
import com.tsc9526.monalisa.tools.converters.impl.ArrayTypeConversion;
import com.tsc9526.monalisa.tools.json.MelpJson;
import com.tsc9526.monalisa.tools.misc.MelpException;
import com.tsc9526.monalisa.tools.string.MelpDate;
import com.tsc9526.monalisa.tools.string.MelpString;

/**
 *  
 * case insensitive and linked hash map
 * 
 * @author zzg.zhou(11039850@qq.com)
 */

public class DataMap extends CaseInsensitiveMap<Object>{ 
	private static final long serialVersionUID = -8132926422921115814L;	
	 
	public static DataMap fromXml(String xml){
		return MelpString.xml2Map(xml);
	}
	
	public static DataMap fromJson(String json){
		return MelpString.json2Map(json);
	}
	
	public static DataMap fromMap(Map<?,?> map){
		return new DataMap(map);
	}
	
	public static DataMap fromBean(Object bean){
		return MelpClass.convert(bean, DataMap.class); 
	}
	
	public DataMap(){
	}
	 
	public DataMap(int initialCapacity) {
        super(initialCapacity);
    }
	
	public DataMap(Map<?,?> m){
		super();
		
		replace(m);
	}
	
	public String toJson() {
		return toJson(true);
	}

	public String toJson(boolean pretty) {
		GsonBuilder gb=MelpJson.createGsonBuilder();
		
		if(pretty){
			gb.setPrettyPrinting();
		}
		
		return gb.create().toJson(this);
	}
	
	public void replace(Map<?, ?> other){
		clear();
		
		if(other!=null){
			Map<String, Object> tmp = new LinkedHashMap<String, Object>();
			for(Entry<?, ?> entry:other.entrySet()){
				Object key   = entry.getKey();
				Object value = entry.getValue();
				
				tmp.put(key==null?null:key.toString(), value);
			}
			
			this.putAll(tmp);
		}
	}
	
	/**
	 * all keys will be added to this prefix
	 * 
	 * @param prefix the prefix key
	 * @return a new DataMap
	 */
	public DataMap addPrefix(String prefix){
		DataMap x = new DataMap();
		
		for(Entry<String, Object> entry:entrySet()){
			String key   = entry.getKey();
			Object value = entry.getValue();
			
			if(key==null){
				x.put(key, value);
			}else{
				x.put(prefix+key, value);
			}
		}
		return x;
	}
	
	/**
	 * all the keys that contain a given prefix will be truncated 
	 * 
	 * @param prefix the prefix key
	 * @return a new DataMap
	 */
	public DataMap removePrefix(String prefix){
		DataMap x = new DataMap();
		
		int len = prefix.length();
		for(Entry<String, Object> entry:entrySet()){
			String key   = entry.getKey();
			Object value = entry.getValue();
			
			if(key!=null && key.startsWith(prefix)){
				x.put(key.substring(len), value);
			}else{
				x.put(key, value);
			}
		}
		
		return x;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T gets(Object key){
		return (T)super.get(key);
	}
	
	@SuppressWarnings("unchecked")
 	public <T> T as(Class<T> toClass){
		try {
			if(toClass.isAssignableFrom(DataMap.class)){
				return (T)this;
			}
			
			T r = toClass.newInstance();
			
			ClassHelper mc=MelpClass.getClassHelper(toClass);
			for(FGS fgs:mc.getFields()){
				fgs.mapto(this,r);
			}  
			
			return r;
		}catch(Exception e) {
			return MelpException.throwRuntimeException(e);
		}
	}
	 	
	/**
	 * Join key and value, default charset is utf-8
	 * 
	 * @return key1=v1&amp;key2=v2&amp;key3=v3...
	 */
	public String toUrlQuery(){
		return toUrlQuery("utf-8");
	}
	
	/**
	 * Join key and value using the charset
	 * 
	 * @param charset encode values using the charset
	 * 
	 * @return key1=v1&amp;key2=v2&amp;key3=v3...
	 */
	public String toUrlQuery(String charset){
		String split="&";
		
		StringBuilder sb = new StringBuilder();
		
		for(java.util.Map.Entry<String,Object> entry:entrySet()){
			String key   = entry.getKey();
			Object value = entry.getValue();
			 
			if(value == null){
				joinAppend(sb,charset,split,key,"");
			}else{
				if(value.getClass().isArray()){
					Object[] vs = null;
					if(value.getClass().getComponentType().isPrimitive()){
						ArrayTypeConversion conversion = new ArrayTypeConversion();
						
						vs = (Object[])conversion.convert(value, Object[].class);
					}else {
						vs = (Object[]) value;
					}
					
					joinAppend(sb,charset,split,key,vs);
					
				}else if(value instanceof List){
					joinAppend(sb,charset,split,key,(List<?>)value);
				}else{
					joinAppend(sb,charset,split,key,value.toString());
				}
			}
			
		}
		
		return sb.toString();
	}
	
	private void joinAppend(StringBuilder sb,String charset,String split,String key,Object[] vs){
		for(Object o:vs){
			joinAppend(sb,charset,split,key,o==null?"":o.toString());
		}
	}
	
	private void joinAppend(StringBuilder sb,String charset,String split,String key,List<?> vs){
		for(Object o:vs){
			joinAppend(sb,charset,split,key,o==null?"":o.toString());
		}
	}
	
	private void joinAppend(StringBuilder sb,String charset,String split,String key,String value){
		if(sb.length()>0){
			sb.append(split);
		}
		
		try{
			String v = URLEncoder.encode(value, charset);
			
			sb.append(key).append("=").append(v);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * 
	 * @param index  The first key is 0.
	 * @return Object value
	 */
	@SuppressWarnings("unchecked")
	public Object get(int index){
		Map.Entry<String,Object> entry=(Map.Entry<String,Object>)this.entrySet().toArray()[index];
		 
		return entry.getValue();
	}
	
	/**
	 * <b>For example:</b> <br> 
	 * <code>
	 * DataMap m=DataMap.fromJson("{'f1':{'f2':'yes'}}");
	 * <br>
	 * m.getByPath("f1/f2") = "yes"
	 * 
	 * </code>
	 * <br><br>
	 * <b>Another example(array):</b> <br> 
	 * <code>
	 * DataMap m=DataMap.fromJson("{'f1':{'f2':['a','b','c']}}");
	 * <br> m.getByPath("f1/f2[1]") = "a"
	 * <br> m.getByPath("f1/f2[2]") = "b"
	 * <br> m.getByPath("f1/f2[3]") = "c"
	 * <br>
	 * <br> m.getByPath("f1/f2[-1]") = "c"
	 * <br> m.getByPath("f1/f2[-2]") = "b"
	 * <br> m.getByPath("f1/f2[-3]") = "a"
	 * </code>
	 * 
	 * @param pathString split by /
	 * @param <T> result type
	 * @return the object value, maybe null
	 */
	public <T> T getByPath(String pathString){
		String paths=pathString;
		if(paths.startsWith("/")){
			paths=paths.substring(1);
		}
		
		String[] sv=paths.split("/");
		
		Object ret=null;
		
		Object m=this;
		for(int i=0;i<sv.length;i++){
			Object v=getValue(m,sv[i]);
			
			if(i==(sv.length-1)){
				ret=v;
			}else{
				if(v!=null){
					m=v;
				}else{
					break;
				}
			}
		}

		@SuppressWarnings("unchecked")
		T r = (T)ret;
		return r;
	}
	
	public <T> T getByPath(String paths,T defaultValue){
		T r=getByPath(paths);
		
		if(r==null){
			r=defaultValue;
		}
		
		return r;
	}
	
	private Object getValue(Object v,String fieldName){
		// name[0]
		String name = fieldName;
		
		Integer index = null;
		
		int x=name.indexOf('[');
		if(x>0 && name.endsWith("]")){
			index=Integer.parseInt(name.substring(x+1,name.length()-1).trim());
			name=name.substring(0,x);
		}
		
		Object ret=null;
		if(v instanceof Map){
			ret= ((Map<?,?>)v).get(name);
		}else if(v instanceof JsonObject){
			ret= ((JsonObject)v).get(name);
		}else if(v instanceof Model){
			ret= ((Model<?>)v).get(name);
		}else{
			ClassHelper mc=MelpClass.getClassHelper(v);
			FGS fgs=mc.getField(name);
			if(fgs!=null){
				ret= fgs.getObject(v);
			}
		}	
		 
		return getFromList(ret,index);
	}
	
	private Object getFromList(Object obj,Integer index){
		if(index!=null && obj instanceof List){
			List<?> list=(List<?>)obj;
			if(index < 0){
				return list.get(list.size()+index);
			}else{
				return list.get(index-1);
			}
		}
		
		return obj;
	}
	
	
	protected Object getOne(String key) {
		Object v=get(key);
		
		if(v!=null && v.getClass().isArray() ) {
			Object[] vs = null;
			if(v.getClass().getComponentType().isPrimitive()){
				ArrayTypeConversion conversion = new ArrayTypeConversion();
				
				vs = (Object[])conversion.convert(v, Object[].class);
			}else {
				vs = (Object[]) v;
			}
			 
			if(vs!=null && vs.length>0){
				return vs[0];
			}else{
				return "";
			}
		}else{
			return v;
		}
	}
	 
	
	public String getString(String key,String defaultValue){
		String v=getString(key);
		if(v==null){
			v= defaultValue;
		}
		return v;
	}
	
	public String getString(String key){
		Object v=getOne(key);
		if(v==null){
			return null;
		}else {
			return v.toString();
		}
	}
	 
	public Integer[] getIntegerValues(String key){
		String[] xs=getStringValues(key);
		if(xs!=null){
			Integer[] rs=new Integer[xs.length];
			for(int i=0;i<xs.length;i++){
				rs[i]=Integer.parseInt(xs[i]);			
			}
			return rs;
		}else{
			return new Integer[0];
		}
	}
	
	public String[] getStringValues(String key){
		Object v=get(key);
		
		if(v==null){
			v=get(key+"[]");
		}
		
		if(v==null){
			return new String[0];
		}else {
			if( v.getClass().isArray() ) {
				Object[] os=(Object[])v;
				
				String[] r=new String[os.length];
				for(int i=0;i<r.length;i++){
					r[i]=os[i]==null?null:os[i].toString();
				}
				return r;
			}else{
				return new String[]{v.toString()};
			}
		}
	}
	
	public boolean getBool(String key,boolean defaultValue){
		Boolean v=getBoolean(key);
		if(v==null){
			v=defaultValue;
		}
		return v;
	}
	 
	public Boolean getBoolean(String key){
		Object v=getOne(key);
		if(v==null){
			return (Boolean)v;
		}else{
			if("".equals(v)){
				return Boolean.FALSE;
			}
			
			if(v instanceof Boolean){
				return (Boolean)v;
			}else{
				return Boolean.valueOf(""+v);
			}
		}
	}
	 
	
	public int getInt(String key, int defaultValue){
		Integer v=getInteger(key);
		if(v==null){
			v=defaultValue;
		}
		return v;
	}
	 
	
	public Integer getInteger(String key){
		Object v=getOne(key);
		if(v==null){
			return null;
		}else{
			if("".equals(v)){
				return null;
			}
			
			if(v instanceof Integer){
				return (Integer)v;
			}else if(v instanceof Double){
				return ((Double)v).intValue();
			}else{
				return Integer.parseInt(""+v);
			}
		}
	}
	 

	public long getLong(String key, long defaultValue){
		Long v=getLong(key);
		if(v==null){
			v=defaultValue;
		}
		return v;
	}
	 
	
	public Long getLong(String key){
		Object v=getOne(key);
		if(v==null){
			return null;
		}else{
			if("".equals(v)){
				return null;
			}
			
			if(v instanceof Long){
				return (Long)v;
			}else if(v instanceof Double){
				return ((Double)v).longValue();
			}else{
				return Long.parseLong(""+v);
			}
		}
	}
	 
	
	public float getFloat(String key, float defaultValue){
		Float v=getFloat(key);
		if(v==null){
			v=defaultValue;
		}
		return v;
	}
	
	 
	
	public Float getFloat(String key){
		Object v=getOne(key);
		if(v==null){
			return null;
		}else{
			if("".equals(v)){
				return null;
			}
			
			if(v instanceof Float){
				return (Float)v;
			}else if(v instanceof Double){
				return ((Double)v).floatValue();
			}else{
				return Float.parseFloat(""+v);
			}
		}
	}
	  
	
	public double getDouble(String key, double defaultValue){
		Double v=getDouble(key);
		if(v==null){
			v=defaultValue;
		}
		return v;
	}
	
	public Double getDouble(String key){
		Object v=getOne(key);
		if(v==null){
			return null;
		}else{
			if("".equals(v)){
				return null;
			}
			
			if(v instanceof Double){
				return (Double)v;
			}else{
				return Double.parseDouble(""+v);
			}
		}
	}
	   
	
	/**
	 * Auto detect the date format:<br>
	 * yyyy-MM-dd<br>
	 * yyyy-MM-dd HH<br>
	 * yyyy-MM-dd HH:mm<br>
	 * yyyy-MM-dd HH:mm:ss<br>
	 * yyyy-MM-dd HH:mm:ss.SSS<br>
	 *   
	 * @param key label of the column
	 * 
	 * @return date value
	 */
	public Date getDate(String key){
		return getDate(key,null,null);
	}
	
	/**
	 * @param key label of the column
	 * @param defaultValue return this value if null 
	 * @return date value
	 * 
	 * @see #getDate(String)
	 */
	public Date getDate(String key, Date defaultValue){
		return getDate(key,null,defaultValue);		  
	}
	
	/**
	 * 
	 * @param key label of the column
	 * @param format  new SimpleDateFormat(format): auto detect date format if null or ''
	 * @param defaultValue return this value if null
	 * @return date value
	 * 
	 * @see #getDate(String)
	 */
	public Date getDate(String key,String format,Date defaultValue){
		return MelpDate.getDate(getOne(key), format, defaultValue); 
	}
}