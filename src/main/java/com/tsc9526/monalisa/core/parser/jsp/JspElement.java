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
package com.tsc9526.monalisa.core.parser.jsp;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public abstract class JspElement{
	protected int pos;
	protected int length;
	
	protected Jsp jsp;
	
	protected int index;
	
	protected String code;
	
	public JspElement(Jsp jsp,int pos,int length) {
		this.jsp=jsp;
		this.pos=pos;
		this.length=length;
	}
	
	public String toString(){
		return jsp.getBody().substring(pos,pos+length);
	}

	public String getCode(){
		return this.code;
	}	
	
	public JspElement parseCode(String code){
		this.code=code;
		return this;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}