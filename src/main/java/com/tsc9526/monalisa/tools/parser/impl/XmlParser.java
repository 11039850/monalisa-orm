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
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import com.tsc9526.monalisa.tools.parser.Parser;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class XmlParser implements Parser<String>{			 
	public boolean parse(Object target, String xml, String... mappings) {
		try {
            XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(xml));
            String attr = null;
            String chars = null;
            Map<String, Object> data = new HashMap<String, Object>();
            while (reader.hasNext()) {
                int event = reader.next();
                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        attr = reader.getLocalName();
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        chars = reader.getText().trim();
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        if (attr != null && chars!=null) {
                            data.put(attr, chars);
                        }
                        attr = chars = null;
                        break;
                }
            }
            
            return new MapParser().parse(target, data, mappings);
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
	}
}