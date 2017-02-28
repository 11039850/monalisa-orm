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
package com.tsc9526.monalisa.tools.parser.impl;

import java.io.StringReader;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.tsc9526.monalisa.tools.datatable.DataMap;
import com.tsc9526.monalisa.tools.parser.Parser;
import com.tsc9526.monalisa.tools.string.MelpString;
import com.tsc9526.monalisa.tools.xml.XMLDocument;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class XmlParser implements Parser<String>{			 
	public boolean parse(Object target, String xml, String... mappings) {
		xml=xml.trim();
		
		String xpath=null;
		if(mappings.length>0 && mappings[0].startsWith("/")){
			xpath=mappings[0];
		}else if(!xml.startsWith("<?xml")){
			xpath="/root";
			
			xml="<root>"+xml+"</root>";
		}
		
		xml=MelpString.normalizeXml(xml);
		try {
			XMLDocument parser=new XMLDocument();
            Document doc=parser.parseDocument(new InputSource(new StringReader(xml)));
            
            Node root=doc;
            if(xpath!=null){
            	root=parser.selectSingleNode(doc,xpath);
            }
            
            DataMap data=parser.toMap(root);
            
            return new MapParser().parse(target, data, mappings);
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
	}
}