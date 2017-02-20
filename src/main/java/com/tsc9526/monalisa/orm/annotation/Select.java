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
package com.tsc9526.monalisa.orm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  First of all, please setup eclipse: <br>
 *  Window -&gt; Preferences -&gt; Java -&gt; Editor -&gt; Save Action -&gt; Configure -&gt; monalisa<br>
 *  Enable this option: @Select<br>
 *  <br>
 *  Annotation: @Select indicating that the method will generate a DTO. <br>
 *  <br>
 *  Optional parameter: <b>name</b>  <br>
 *  &nbsp;&nbsp;  specifies the name of the class generated DTO, <br>
 *  &nbsp;&nbsp;  if not specified, using the default: "Result" + the method's name<br>
 *  Optional parameter: <b>build</b><br>
 *  &nbsp;&nbsp;  a Java snippet for initializing the method parameters, <br>
 *  &nbsp;&nbsp;  replace the default initialization rule<br>
 *  <br>   
 *  For example: @Select(name = "UserBlog")<br>
 *	After saved, the plugin will automatically modify the return value: List -&gt; List &lt;UserBlog&gt; <br>
 *  <br> 
 *  The first time, DTO class does not exist. In order to compile the code correctly, <br>
 *  the return value and the result of the query must be replaced by a generic value. <br>
 *  If saved, the plugin will automatically modify the results to the corresponding values.<br>
 *  <br>
 *  Here is the corresponding relationship between return value and results of the query:<br>
 *  1. List query<br>
 *  Public DataTable method_name (...) {... return query.getList ();} or<br>
 *  Public List      method_name (...) {... return query.getList ();}<br>
 *  <br>
 *  2. Page query<br>
 *  Public Page      method_name (...) {... return query.Page ();   }<br>
 *  <br>
 *  3. Single record<br>
 *  Public Object    method_name (...) {... return query.getResult ();}
 *   
 * @author zzg.zhou(11039850@qq.com)
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Select{
	/**
	 * @return the DTO class name. default: Result + method name
	 */
	String name()  default "";
	
	/**
	 * @return Init running parameters
	 */
	String build() default "";
}